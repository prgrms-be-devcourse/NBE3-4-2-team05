package z9.second.global.security.oauth.user;

import java.util.Map;
import lombok.AllArgsConstructor;
import z9.second.model.oauthuser.OAuthProvider;

@AllArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public OAuthProvider getProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String getName() {
        return (String) ((Map) attributes.get("properties")).get("nickname");
    }
}
