package z9.second.domain.authentication.service;

import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_CATEGORY;
import static z9.second.global.security.constant.JWTConstant.REFRESH_TOKEN_CATEGORY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.authentication.dto.AuthenticationRequest;
import z9.second.domain.authentication.dto.AuthenticationResponse;
import z9.second.domain.favorite.entity.FavoriteEntity;
import z9.second.domain.favorite.repository.FavoriteRepository;
import z9.second.domain.kakao.KakaoAuthFeignClient;
import z9.second.domain.kakao.KakaoResourceFeignClient;
import z9.second.global.exception.CustomException;
import z9.second.global.redis.RedisRepository;
import z9.second.global.response.ErrorCode;
import z9.second.global.security.constant.OAuthConstant;
import z9.second.global.security.jwt.JWTUtil;
import z9.second.global.security.jwt.JwtProperties;
import z9.second.global.security.oauth.OAuthProperties;
import z9.second.global.security.oauth.OAuthToken;
import z9.second.global.security.oauth.user.KakaoUserInfo;
import z9.second.global.security.oauth.user.OAuth2UserInfo;
import z9.second.global.security.user.CustomUserDetails;
import z9.second.global.utils.ControllerUtils;
import z9.second.model.oauthuser.OAuthUser;
import z9.second.model.oauthuser.OAuthUserRepository;
import z9.second.model.user.User;
import z9.second.model.user.UserRepository;
import z9.second.model.userfavorite.UserFavorite;
import z9.second.model.userfavorite.UserFavoriteRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtProperties jwtProperties;
    private final OAuthProperties oAuthProperties;
    private final JWTUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final KakaoAuthFeignClient kakaoAuthFeignClient;
    private final KakaoResourceFeignClient kakaoResourceFeignClient;

    private final OAuthUserRepository oAuthUserRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final RedisRepository redisRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFavoriteRepository userFavoriteRepository;

    @Transactional(readOnly = true)
    @Override
    public AuthenticationResponse.UserToken login(AuthenticationRequest.Login dto) {
        Authentication authentication = authenticateUser(dto);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (userDetails.isOAuthUser()) {
            throw new CustomException(ErrorCode.OAUTH_USER_LOGIN_FAIL);
        }

        return generateUserTokens(
                authentication.getAuthorities().iterator().next().getAuthority(),
                userDetails.getUsername());
    }

    @Transactional
    @Override
    public AuthenticationResponse.UserToken oauthLogin(String provider, String authCode) {
        OAuth2UserInfo oauth2UserInfo = getOauth2UserInfo(provider, authCode);

        User user = getUserByOAuth(oauth2UserInfo);

        return generateUserTokens(
                user.getRole().name(),
                user.getId().toString());
    }

    @Transactional
    @Override
    public void signup(AuthenticationRequest.Signup signupDto) {
        // 1. favorite 가 등록되어있는건지 확인
        // todo : 관심사는 자주 등록되어 바뀌지 않으니 캐싱 처리해두면 좋을 듯.
        List<String> favorite = signupDto.getFavorite();
        List<FavoriteEntity> findFavorites = favoriteRepository.findByNameIn(favorite);
        if(findFavorites.size() != favorite.size()) {
            throw new CustomException(ErrorCode.NOT_EXIST_FAVORITE);
        }

        // 2. userId,nickname 중복 검사
        Optional<User> findOptionalUser = userRepository.findByLoginIdOrNickname(
                signupDto.getLoginId(), signupDto.getNickname());
        if(findOptionalUser.isPresent()) {
            User findUser = findOptionalUser.get();
            if(findUser.getLoginId().equals(signupDto.getLoginId())){
                throw new CustomException(ErrorCode.DUPLICATED_LOGIN_ID);
            }
            if(findUser.getNickname().equals(signupDto.getNickname())) {
                throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
            }
        }

        // 3. 회원가입 진행
        User newUser = User.createNewUser(
                signupDto.getLoginId(),
                passwordEncoder.encode(signupDto.getPassword()),
                signupDto.getNickname());
        User savedUser = userRepository.save(newUser);

        List<UserFavorite> userFavoriteList = new ArrayList<>();
        for (FavoriteEntity findFavorite : findFavorites) {
            UserFavorite newUserFavorite = UserFavorite.createNewUserFavorite(savedUser, findFavorite);
            userFavoriteList.add(newUserFavorite);
        }
        userFavoriteRepository.saveAll(userFavoriteList);
    }

    @Transactional
    @Override
    public void logout(String userId) {
        redisRepository.deleteRefreshToken(userId);
        //todo : 추후, 여유 생기면 accessToken 또한 블랙리스트 추가하여 추가보안 설정.
    }

    private User getUserByOAuth(OAuth2UserInfo oauth2UserInfo) {
        Optional<OAuthUser> findOptionalUser = oAuthUserRepository.findByProviderAndUid(
                oauth2UserInfo.getProvider(), oauth2UserInfo.getProviderId());

        if (findOptionalUser.isEmpty()) {
            return createNewUserAndOAuthUser(oauth2UserInfo);
        }
        return findOptionalUser.get().getUser();
    }

    private OAuth2UserInfo getOauth2UserInfo(String provider, String authCode) {
        OAuth2UserInfo oauth2UserInfo;
        switch (provider) {
            case OAuthConstant.OAUTH_PROVIDER_KAKAO -> {
                OAuthToken oAuthToken = kakaoAuthFeignClient.getKakaoToken(
                        "authorization_code",
                        oAuthProperties.getClientId(),
                        oAuthProperties.getRedirectUri(),
                        authCode,
                        oAuthProperties.getClientSecret(),
                        oAuthProperties.getContentType());

                Map<String, Object> userInfoMap = kakaoResourceFeignClient.getUserInfo(
                        ControllerUtils.makeBearerToken(oAuthToken.getAccessToken()),
                        oAuthProperties.getContentType());

                oauth2UserInfo = new KakaoUserInfo(userInfoMap);
            }
            default -> {
                throw new CustomException(ErrorCode.INVALID_OAUTH_PROVIDER);
            }
        }
        return oauth2UserInfo;
    }

    private User createNewUserAndOAuthUser(OAuth2UserInfo oauth2UserInfo) {
        User savedUser = userRepository.save(
                User.createNewOAuthUser(
                        oauth2UserInfo.getName(),
                        oauth2UserInfo.getProviderId().substring(0, 5)));
        oAuthUserRepository.save(OAuthUser.createNewOAuthUser(
                oauth2UserInfo.getProviderId(),
                oauth2UserInfo.getProvider(),
                savedUser));
        return savedUser;
    }

    private Authentication authenticateUser(AuthenticationRequest.Login dto) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.getLoginId(), dto.getPassword());
        return authenticationManager.authenticate(authToken);
    }

    private AuthenticationResponse.UserToken generateUserTokens(String role, String userId) {
        String access = jwtUtil.createJwt(ACCESS_TOKEN_CATEGORY, userId, role,
                jwtProperties.getAccessExpiration());
        String refresh = jwtUtil.createJwt(REFRESH_TOKEN_CATEGORY, userId, role,
                jwtProperties.getRefreshExpiration());

        saveRefreshToken(userId, refresh, jwtProperties.getRefreshExpiration());

        return AuthenticationResponse.UserToken.of(access, refresh);
    }

    private void saveRefreshToken(String userId, String refreshToken, Long expirationMs) {
        redisRepository.saveRefreshToken(userId, refreshToken, expirationMs);
    }
}
