package z9.second.domain.user.service;

import z9.second.domain.user.dto.UserRequest;
import z9.second.domain.user.dto.UserResponse;

public interface UserService {

    UserResponse.UserInfo findUserInfo(Long userId);

    void patchUserInfo(UserRequest.PatchUserInfo requestDto, Long userId);
}
