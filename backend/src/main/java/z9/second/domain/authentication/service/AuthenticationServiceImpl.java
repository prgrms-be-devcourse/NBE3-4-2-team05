package z9.second.domain.authentication.service;

import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_CATEGORY;
import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_PREFIX;
import static z9.second.global.security.constant.JWTConstant.REFRESH_TOKEN_CATEGORY;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.authentication.dto.AuthenticationRequest;
import z9.second.domain.authentication.dto.AuthenticationResponse;
import z9.second.domain.kakao.KakaoAuthFeignClient;
import z9.second.domain.kakao.KakaoResourceFeignClient;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;
import z9.second.global.security.constant.OAuthConstant;
import z9.second.global.security.jwt.JWTUtil;
import z9.second.global.security.jwt.JwtProperties;
import z9.second.global.security.oauth.OAuthProperties;
import z9.second.global.security.oauth.OAuthToken;
import z9.second.global.security.oauth.user.KakaoUserInfo;
import z9.second.global.security.oauth.user.OAuth2UserInfo;
import z9.second.global.security.user.CustomUserDetails;
import z9.second.model.oauthuser.OAuthUser;
import z9.second.model.oauthuser.OAuthUserRepository;
import z9.second.model.user.User;
import z9.second.model.user.UserRepository;

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

    private User getUserByOAuth(OAuth2UserInfo oauth2UserInfo) {
        Optional<OAuthUser> findOptionalUser = oAuthUserRepository.findByProviderAndUid(
                oauth2UserInfo.getProvider(), oauth2UserInfo.getProviderId());

        if (findOptionalUser.isEmpty()) {
            return createNewUserAndOAuthUser(oauth2UserInfo);
        }
        return findUserByOAuthUser(findOptionalUser);
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
                        String.format("%s %s", ACCESS_TOKEN_PREFIX, oAuthToken.getAccessToken()),
                        oAuthProperties.getContentType());

                oauth2UserInfo = new KakaoUserInfo(userInfoMap);
            }
            default -> {
                throw new CustomException(ErrorCode.INVALID_OAUTH_PROVIDER);
            }
        }
        return oauth2UserInfo;
    }

    private static User findUserByOAuthUser(Optional<OAuthUser> findOptionalUser) {
        return findOptionalUser.get().getUser();
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

        return AuthenticationResponse.UserToken.of(access, refresh);
    }
}
