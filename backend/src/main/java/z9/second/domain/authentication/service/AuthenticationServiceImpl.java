package z9.second.domain.authentication.service;

import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_CATEGORY;
import static z9.second.global.security.constant.JWTConstant.REFRESH_TOKEN_CATEGORY;

import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import z9.second.domain.authentication.dto.AuthenticationRequest;
import z9.second.domain.authentication.dto.AuthenticationResponse;
import z9.second.global.security.jwt.JWTUtil;
import z9.second.global.security.jwt.JwtProperties;
import z9.second.global.security.user.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final JwtProperties jwtProperties;

    @Override
    public AuthenticationResponse.UserToken login(AuthenticationRequest.Login dto) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        dto.getLoginId(), dto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authToken);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        String userId = userDetails.getUsername();

        String access = jwtUtil.createJwt(ACCESS_TOKEN_CATEGORY, userId, role, jwtProperties.getAccessExpiration());
        String refresh = jwtUtil.createJwt(REFRESH_TOKEN_CATEGORY, userId, role, jwtProperties.getRefreshExpiration());

        return AuthenticationResponse.UserToken.of(access, refresh);
    }
}
