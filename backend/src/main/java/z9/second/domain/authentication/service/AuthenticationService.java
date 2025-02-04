package z9.second.domain.authentication.service;

import z9.second.domain.authentication.dto.AuthenticationRequest;
import z9.second.domain.authentication.dto.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse.UserToken login(AuthenticationRequest.Login dto);

    AuthenticationResponse.UserToken oauthLogin(String provider, String authCode);

    void signup(AuthenticationRequest.Signup signupDto);

    void logout(String userId);
}
