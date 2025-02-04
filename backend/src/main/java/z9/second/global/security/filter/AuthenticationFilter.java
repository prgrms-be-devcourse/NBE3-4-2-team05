package z9.second.global.security.filter;

import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_CATEGORY;
import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_HEADER;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import z9.second.global.security.jwt.JWTUtil;
import z9.second.global.security.user.CustomUserDetails;
import z9.second.model.user.User;
import z9.second.model.user.UserRole;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. AccessToken 추출
        String accessToken = FilterUtil.extractAccessToken(request);

        // 2. AccessToken 유무 확인
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2-2. 만약 response 에 header 값이 있다면 token 이 재발급 된 것.
        // 해당 token 으로 인증 진행할 것
        String newAccessToken = response.getHeader(ACCESS_TOKEN_HEADER);
        if (newAccessToken != null) {
            accessToken = newAccessToken;
        }

        // 3. AccessToken 검증2
        try {
            validateAccessToken(accessToken);
        } catch (JwtException e) {
            FilterUtil.handleJwtException(response, e, objectMapper);
            return;
        }

        // 4. 토큰으로 회원 인증 진행.
        setAuthentication(accessToken);

        filterChain.doFilter(request, response);
    }

    /**
     * 토큰 유효성 검증
     * @param accessToken
     * @throws JwtException
     */
    private void validateAccessToken(String accessToken) throws JwtException {
        // 1. expired 확인
        jwtUtil.isExpired(accessToken);

        // 2. category 확인
        String category = jwtUtil.getCategory(accessToken);
        if (!ACCESS_TOKEN_CATEGORY.equals(category)) {
            throw new JwtException("Invalid access token category");
        }
    }

    private void setAuthentication(String accessToken) {
        Long userId = Long.parseLong(jwtUtil.getUserId(accessToken));
        String role = jwtUtil.getRole(accessToken);

        User authUser = User.createSecurityContextUser(userId, UserRole.valueOf(role));
        CustomUserDetails customUserDetails = new CustomUserDetails(authUser);
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
