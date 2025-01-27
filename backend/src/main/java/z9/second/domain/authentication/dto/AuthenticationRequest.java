package z9.second.domain.authentication.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import z9.second.global.annotation.validation.user.UserLoginId;
import z9.second.global.annotation.validation.user.UserPassword;

public class AuthenticationRequest {

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Login {
        @UserLoginId
        private String loginId;

        @UserPassword
        private String password;

        public static Login of(String loginId, String password) {
            return new Login(loginId, password);
        }
    }
}
