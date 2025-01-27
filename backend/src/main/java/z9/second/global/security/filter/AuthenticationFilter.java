package z9.second.global.security.filter;

import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_CATEGORY;
import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_HEADER;
import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_PREFIX;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
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
import z9.second.global.response.BaseResponse;
import z9.second.global.response.ErrorCode;
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

        String accessToken = extractAccessToken(request);

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            validateAccessToken(accessToken);
        } catch (JwtException e) {
            handleJwtException(response, e);
            return;
        }

        setAuthentication(accessToken);

        filterChain.doFilter(request, response);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);
        if (accessToken != null && accessToken.startsWith(ACCESS_TOKEN_PREFIX)) {
            return accessToken.substring(7);
        }
        return null;
    }

    private void validateAccessToken(String accessToken) throws JwtException {
        jwtUtil.isExpired(accessToken);

        String category = jwtUtil.getCategory(accessToken);
        if (!ACCESS_TOKEN_CATEGORY.equals(category)) {
            throw new JwtException("Invalid access token category");
        }
    }

    private void handleJwtException(HttpServletResponse response, JwtException e) throws IOException {
        if (e instanceof ExpiredJwtException) {
            writeErrorResponse(response, ErrorCode.ACCESS_TOKEN_EXPIRED);
        } else {
            writeErrorResponse(response, ErrorCode.INVALID_ACCESS_TOKEN);
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

    private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        BaseResponse<Void> errorResponse = BaseResponse.fail(errorCode);
        response.setContentType("application/json");
        response.setStatus(errorCode.getHttpStatus().value());
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
