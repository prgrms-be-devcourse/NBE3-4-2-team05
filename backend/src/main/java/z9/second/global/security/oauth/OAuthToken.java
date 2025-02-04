package z9.second.global.security.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class OAuthToken {
    @JsonProperty("access_token")
    private final String accessToken;

    @JsonProperty("token_type")
    private final String tokenType;

    @JsonProperty("refresh_token")
    private final String refreshToken;

    @JsonProperty("expires_in")
    private final int expiresIn;

    @JsonProperty("scope")
    private final String scope;

    @JsonProperty("refresh_token_expires_in")
    private final int refreshTokenExpiresIn;
}
