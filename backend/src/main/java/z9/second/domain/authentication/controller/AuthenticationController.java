package z9.second.domain.authentication.controller;

import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_HEADER;
import static z9.second.global.security.constant.JWTConstant.REFRESH_TOKEN_HEADER;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import z9.second.domain.authentication.dto.AuthenticationRequest;
import z9.second.domain.authentication.dto.AuthenticationResponse;
import z9.second.domain.authentication.service.AuthenticationService;
import z9.second.global.response.BaseResponse;
import z9.second.global.response.SuccessCode;
import z9.second.global.security.jwt.JwtProperties;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@Tag(name = "Authentication Controller", description = "회원 인증 컨트롤러")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtProperties jwtProperties;

    @PostMapping("/login")
    @Operation(summary = "회원 로그인")
    public BaseResponse<Void> login(
            @Valid @RequestBody AuthenticationRequest.Login dto,
            HttpServletResponse response
    ) {
        AuthenticationResponse.UserToken token = authenticationService.login(dto);
        addTokenToResponse(token, response);
        return BaseResponse.ok(SuccessCode.LOGIN_SUCCESS);
    }

    private void addTokenToResponse(
            AuthenticationResponse.UserToken token, HttpServletResponse response) {
        response.setHeader(ACCESS_TOKEN_HEADER, token.getAccessToken());

        Cookie cookie = new Cookie(REFRESH_TOKEN_HEADER, token.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(parseMsToSec(jwtProperties.getRefreshExpiration()));
        response.addCookie(cookie);
    }

    private int parseMsToSec(Long ms) {
        return (int) (ms / 1000);
    }
}
