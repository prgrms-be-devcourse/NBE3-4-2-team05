package z9.second.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import z9.second.global.security.jwt.JWTUtil;

/**
 * AccessToken 이 expired 될 경우, RefreshToken 으로 재발급을 자동 진행해 줍니다.
 */
@Component
@RequiredArgsConstructor
public class ReissueFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        //todo : jwt 재발급 절차 진행!
        doFilter(request, response, filterChain);
        return;
    }
}
