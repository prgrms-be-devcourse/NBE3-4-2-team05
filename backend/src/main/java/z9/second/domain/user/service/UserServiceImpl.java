package z9.second.domain.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.user.dto.UserResponse;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;
import z9.second.model.user.User;
import z9.second.model.user.UserRepository;
import z9.second.model.userfavorite.UserFavoriteRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserFavoriteRepository userFavoriteRepository;

    @Transactional(readOnly = true)
    @Override
    public UserResponse.UserInfo findUserInfo(Long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<String> favorites = userFavoriteRepository.findFavoriteNamesByUserId(userId);

        return UserResponse.UserInfo.of(findUser, favorites);
    }
}
