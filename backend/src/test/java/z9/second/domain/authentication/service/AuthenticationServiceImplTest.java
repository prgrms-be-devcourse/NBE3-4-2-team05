package z9.second.domain.authentication.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_PREFIX;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.authentication.dto.AuthenticationRequest;
import z9.second.domain.authentication.dto.AuthenticationResponse;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;
import z9.second.integration.SpringBootTestSupporter;
import z9.second.model.user.User;

@Transactional
class AuthenticationServiceImplTest extends SpringBootTestSupporter {

    @DisplayName("로그인 아이디로, 로그인을 진행 합니다. 성공되면, access/refresh token 을 반환합니다.")
    @Test
    void login() {
        // given
        String loginId = "test@email.com";
        String password = "!test1234";
        String nickName = "테스터";
        userRepository.save(
                User.createNewUser(loginId, passwordEncoder.encode(password), nickName));
        em.flush();
        em.clear();

        // when
        AuthenticationResponse.UserToken userToken = authenticationService.login(
                AuthenticationRequest.Login.of(loginId, password));

        // then
        assertThat(userToken.getAccessToken())
                .isNotNull();
        assertThat(userToken.getRefreshToken())
                .isNotNull();
    }

    @DisplayName("로그인 아이디가 틀리거나, 등등 로그인이 불가능하다면 오류 메세지가 출력됩니다!")
    @Test
    void login2() {
        // given
        String loginId = "test@email.com";
        String password = "!test1234";

        // when // then
        assertThatThrownBy(() -> authenticationService.login(AuthenticationRequest.Login.of(loginId, password)))
                .isInstanceOf(CustomException.class)
                .extracting("code")
                .isEqualTo(ErrorCode.LOGIN_FAIL);
    }
}