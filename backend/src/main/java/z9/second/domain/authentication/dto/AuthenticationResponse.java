package z9.second.domain.authentication.dto;

import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_PREFIX;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class AuthenticationResponse {

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UserToken {
        private String accessToken;
        private String refreshToken;

        public static UserToken of(String accessToken, String refreshToken) {
            return UserToken
                    .builder()
                    .accessToken(String.format("%s %s", ACCESS_TOKEN_PREFIX, accessToken))
                    .refreshToken(refreshToken)
                    .build();
        }
    }
}
