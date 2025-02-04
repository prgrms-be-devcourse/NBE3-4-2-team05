package z9.second.global.security.filter;

import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_CATEGORY;
import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_HEADER;
import static z9.second.global.security.constant.JWTConstant.REFRESH_TOKEN_CATEGORY;
import static z9.second.global.security.constant.JWTConstant.REFRESH_TOKEN_HEADER;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import z9.second.global.redis.RedisRepository;
import z9.second.global.security.jwt.JWTUtil;
import z9.second.global.security.jwt.JwtProperties;
import z9.second.global.utils.ControllerUtils;

/**
 * AccessToken 이 expired 될 경우, RefreshToken 으로 재발급을 자동 진행해 줍니다.
 * 재발급 범위 : RefreshToken, AccessToken
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReissueFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final RedisRepository redisRepository;
    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. AccessToken 확인
        // 토큰이 없거나 (인증이 필요하지 않은 api 예상), 만료되지 않았다면 재발급 할 필요 없어, 넘어감
        String accessToken = FilterUtil.extractAccessToken(request);
        if (accessToken == null || !isExpired(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. RefreshToken 추출
        String refreshToken = FilterUtil.extractRefreshToken(request);

        // 3. RefreshToken 유무 검증 및 expired 검증
        // 토큰이 없거나, 토큰이 만료되었다면 재발급 불가로 다음 필터로 넘어감
        if (refreshToken == null || isExpired(refreshToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4. 저장된 RefreshToken 가져오기
        String userId = jwtUtil.getUserId(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        String savedRefreshToken = redisRepository.getRefreshToken(userId);

        // 5. RefreshToken 유효성 검증
        try {
            validateRefreshToken(refreshToken, savedRefreshToken);
        } catch (JwtException e) {
            FilterUtil.handleJwtException(response, e, objectMapper);
            return;
        }

        // 6. 새로운 Access Token 및 Refresh Token 발급
        String newAccessToken = jwtUtil.createJwt(ACCESS_TOKEN_CATEGORY, userId, role, jwtProperties.getAccessExpiration());
        String newRefreshToken = jwtUtil.createJwt(REFRESH_TOKEN_CATEGORY, userId, role, jwtProperties.getRefreshExpiration());
        redisRepository.deleteRefreshToken(userId);
        redisRepository.saveRefreshToken(userId, newRefreshToken, jwtProperties.getRefreshExpiration());

        // 7. 해당 api 요청 응답에 새로운 토큰 값 저장
        ControllerUtils.addHeaderResponse(
                ACCESS_TOKEN_HEADER,
                newAccessToken,
                response);
        ControllerUtils.addCookieResponse(
                REFRESH_TOKEN_HEADER,
                newRefreshToken,
                ControllerUtils.parseMsToSec(jwtProperties.getRefreshExpiration()),
                response);

        log.info(String.format("userId:%s 사용자, 토큰 재발급 되었습니다.", userId));
        log.info(String.format("Refresh Token = %s", newRefreshToken));
        log.info(String.format("Access Token = %s", newAccessToken));

        filterChain.doFilter(request, response);
    }

    private void validateRefreshToken(String refreshToken, String savedRefreshToken) {
        // 1. refresh Token Category 검증
        String category = jwtUtil.getCategory(refreshToken);
        if(!REFRESH_TOKEN_CATEGORY.equals(category)) {
            throw new JwtException("Invalid refresh token category");
        }

        // 2. 저장된 refreshToken 과 동일한지 확인
        if(!refreshToken.equals(savedRefreshToken)) {
            log.error("저장된 refresh Token 과 전달받은 refresh token 이 일치하지 않음.");
            throw new JwtException("Invalid refresh token");
        }
    }

    private boolean isExpired(String token) {
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }
}
