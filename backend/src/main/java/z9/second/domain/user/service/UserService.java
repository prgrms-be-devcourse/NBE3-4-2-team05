package z9.second.domain.user.service;

import z9.second.domain.user.dto.UserResponse;

public interface UserService {

    UserResponse.UserInfo findUserInfo(Long userId);
}
