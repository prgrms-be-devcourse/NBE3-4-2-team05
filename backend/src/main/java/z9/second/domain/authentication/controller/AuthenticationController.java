package z9.second.domain.authentication.controller;

import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_HEADER;
import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_PREFIX;
import static z9.second.global.security.constant.JWTConstant.REFRESH_TOKEN_HEADER;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import z9.second.domain.authentication.dto.AuthenticationRequest;
import z9.second.domain.authentication.dto.AuthenticationResponse;
import z9.second.domain.authentication.service.AuthenticationService;
import z9.second.global.response.BaseResponse;
import z9.second.global.response.SuccessCode;
import z9.second.global.security.jwt.JwtProperties;
import z9.second.global.utils.ControllerUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
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
        addJwtTokenResponse(response, token);
        return BaseResponse.ok(SuccessCode.LOGIN_SUCCESS);
    }

    @GetMapping("/login/{provider}")
    @Operation(summary = "소셜 로그인 리타이렉트 주소")
    public BaseResponse<Void> kakaoLogin(
            @PathVariable(value = "provider") String provider,
            @RequestParam(value = "code") String code,
            HttpServletResponse response) {
        AuthenticationResponse.UserToken token =
                authenticationService.oauthLogin(provider, code);
        addJwtTokenResponse(response, token);
        return BaseResponse.ok(SuccessCode.LOGIN_SUCCESS);
    }

    @PostMapping("/signup")
    @Operation(summary = "일반 회원 가입")
    public BaseResponse<Void> signup(
            @Valid @RequestBody AuthenticationRequest.Signup signupDto
    ) {
        authenticationService.signup(signupDto);
        return BaseResponse.ok(SuccessCode.SIGNUP_SUCCESS);
    }

    private void addJwtTokenResponse(HttpServletResponse response, AuthenticationResponse.UserToken token) {
        ControllerUtils.addHeaderResponse(
                ACCESS_TOKEN_HEADER,
                String.format("%s %s", ACCESS_TOKEN_PREFIX, token.getAccessToken()),
                response);
        ControllerUtils.addCookieResponse(
                REFRESH_TOKEN_HEADER,
                token.getRefreshToken(),
                ControllerUtils.parseMsToSec(jwtProperties.getRefreshExpiration()),
                response);
    }
}
