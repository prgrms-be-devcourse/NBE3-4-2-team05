package z9.second.global.security.filter;

import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_HEADER;
import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_PREFIX;
import static z9.second.global.security.constant.JWTConstant.REFRESH_TOKEN_HEADER;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import z9.second.global.response.BaseResponse;
import z9.second.global.response.ErrorCode;

public abstract class FilterUtil {
    public static String extractAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);
        if (accessToken != null && accessToken.startsWith(ACCESS_TOKEN_PREFIX)) {
            return accessToken.substring(7);
        }
        return null;
    }

    public static String extractRefreshToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_HEADER))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    public static void handleJwtException(HttpServletResponse response, JwtException e, ObjectMapper objectMapper) throws IOException {
        if (e instanceof ExpiredJwtException) {
            writeErrorResponse(response, ErrorCode.TOKEN_EXPIRED, objectMapper, ErrorCode.TOKEN_EXPIRED.getMessage());
        } else {
            writeErrorResponse(response, ErrorCode.INVALID_TOKEN, objectMapper, e.getMessage());
        }
    }

    private static void writeErrorResponse(
            HttpServletResponse response, ErrorCode errorCode, ObjectMapper objectMapper, String message) throws IOException {
        BaseResponse<Void> errorResponse = BaseResponse.fail(errorCode, message);
        response.setContentType("application/json");
        response.setStatus(errorCode.getHttpStatus().value());
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
