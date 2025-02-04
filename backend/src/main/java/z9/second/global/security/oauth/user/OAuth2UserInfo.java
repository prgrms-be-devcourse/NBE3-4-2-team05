package z9.second.global.security.oauth.user;

import z9.second.model.oauthuser.OAuthProvider;

public interface OAuth2UserInfo {
    String getProviderId();
    OAuthProvider getProvider();
    String getName();
}
