package z9.second.global.security.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import z9.second.global.response.BaseResponse;
import z9.second.global.response.ErrorCode;

/**
 * 인증 오류 handler
 * token 으로 인증 없이, 인증이 필요한 endpoint 에 접근 시, 해당 entryPoint 로 공통 response 생성
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        BaseResponse<Void> errorResponse = BaseResponse.fail(ErrorCode.NEED_LOGIN);
        response.setContentType("application/json");
        response.setStatus(ErrorCode.NEED_LOGIN.getHttpStatus().value());
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
