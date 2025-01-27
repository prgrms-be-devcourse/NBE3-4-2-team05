package z9.second.global.security.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import z9.second.global.response.BaseResponse;
import z9.second.global.response.ErrorCode;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedEntryPoint implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        BaseResponse<Void> errorResponse = BaseResponse.fail(ErrorCode.FORBIDDEN);
        response.setContentType("application/json");
        response.setStatus(ErrorCode.FORBIDDEN.getHttpStatus().value());
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
