package z9.second.domain.user.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.favorite.entity.FavoriteEntity;
import z9.second.domain.user.dto.UserRequest;
import z9.second.domain.user.dto.UserResponse;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;
import z9.second.integration.SpringBootTestSupporter;
import z9.second.model.user.User;
import z9.second.model.user.UserRole;
import z9.second.model.user.UserType;
import z9.second.model.userfavorite.UserFavorite;

@Transactional
class UserServiceImplTest extends SpringBootTestSupporter {

    @DisplayName("회원 정보를 찾습니다.")
    @Test
    void findUserInfo() {
        // given
        String loginId = "test1@email.com";
        String password = "!test1234";
        String nickname = "test";
        User newUser = User.createNewUser(loginId, password, nickname);
        User saveUser = userRepository.save(newUser);

        List<String> favorite = List.of("관심사1", "관심사2");
        FavoriteEntity fe1 = FavoriteEntity.createNewFavorite("관심사1");
        FavoriteEntity fe2 = FavoriteEntity.createNewFavorite("관심사2");
        List<FavoriteEntity> saveFavoriteEntities = favoriteRepository.saveAll(List.of(fe1, fe2));

        userFavoriteRepository.save(
                UserFavorite.createNewUserFavorite(saveUser, saveFavoriteEntities.get(0)));
        userFavoriteRepository.save(UserFavorite.createNewUserFavorite(saveUser, saveFavoriteEntities.get(1)));

        em.flush();
        em.clear();

        // when
        UserResponse.UserInfo findData = userService.findUserInfo(saveUser.getId());

        // then
        assertThat(findData)
                .extracting("nickname", "type", "role")
                .containsExactly(nickname, UserType.NORMAL.getValue(), UserRole.ROLE_USER.getValue());
        assertThat(findData.getCreatedAt()).matches("\\d{4}-\\d{2}-\\d{2}");
        assertThat(findData.getFavorite())
                .hasSize(2)
                .containsExactlyInAnyOrder("관심사1", "관심사2");
    }

    @DisplayName("회원 정보를 찾습니다. 관심사가 없을 경우, 빈 배열이 출력되어야 합니다.")
    @Test
    void findUserInfo2() {
        // given
        String loginId = "test1@email.com";
        String password = "!test1234";
        String nickname = "test";
        User newUser = User.createNewUser(loginId, password, nickname);
        User saveUser = userRepository.save(newUser);

        em.flush();
        em.clear();

        // when
        UserResponse.UserInfo findData = userService.findUserInfo(saveUser.getId());

        // then
        assertThat(findData)
                .extracting("nickname", "type", "role")
                .containsExactly(nickname, UserType.NORMAL.getValue(), UserRole.ROLE_USER.getValue());
        assertThat(findData.getCreatedAt()).matches("\\d{4}-\\d{2}-\\d{2}");
        assertThat(findData.getFavorite())
                .hasSize(0);
    }

    @DisplayName("회원 정보를 찾습니다. 없는 회원으로 조회 시, 오류 메세지가 서빙됩니다.")
    @Test
    void findUserInfo3() {
        // given

        em.flush();
        em.clear();

        // when // then
        assertThatThrownBy(() -> userService.findUserInfo(1L))
                .isInstanceOf(CustomException.class)
                .extracting("code")
                .isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @DisplayName("회원 정보를 수정 합니다.")
    @Test
    void patchUserInfo1() {
        // given
        String loginId = "test1@email.com";
        String password = "!test1234";
        String nickname = "test";
        User newUser = User.createNewUser(loginId, password, nickname);
        User saveUser = userRepository.save(newUser);

        List<String> favorite = List.of("관심사1", "관심사2");
        FavoriteEntity fe1 = FavoriteEntity.createNewFavorite("관심사1");
        FavoriteEntity fe2 = FavoriteEntity.createNewFavorite("관심사2");
        List<FavoriteEntity> saveFavoriteEntities = favoriteRepository.saveAll(List.of(fe1, fe2));

        userFavoriteRepository.save(UserFavorite.createNewUserFavorite(saveUser, saveFavoriteEntities.get(0)));

        String changeNickname = "변경된닉네임";
        UserRequest.PatchUserInfo request = UserRequest.PatchUserInfo.of(changeNickname, favorite);

        em.flush();
        em.clear();

        // when
        userService.patchUserInfo(request, saveUser.getId());

        // then
        User findUser = userRepository.findById(saveUser.getId()).get();
        List<String> findFavoriteList = userFavoriteRepository.findFavoriteNamesByUserId(saveUser.getId());
        assertThat(findUser)
                .extracting("nickname")
                .isEqualTo(changeNickname);
        assertThat(findFavoriteList)
                .hasSize(2)
                .containsExactlyInAnyOrder("관심사1", "관심사2");
    }

    @DisplayName("회원 정보를 수정 합니다. 원래 있던 관심사가 입력되지 않으면 그전 데이터는 삭제 됩니다.")
    @Test
    void patchUserInfo2() {
        // given
        String loginId = "test1@email.com";
        String password = "!test1234";
        String nickname = "test";
        User newUser = User.createNewUser(loginId, password, nickname);
        User saveUser = userRepository.save(newUser);

        List<String> favorite = List.of("관심사1", "관심사2");
        FavoriteEntity fe1 = FavoriteEntity.createNewFavorite("관심사1");
        FavoriteEntity fe2 = FavoriteEntity.createNewFavorite("관심사2");
        List<FavoriteEntity> saveFavoriteEntities = favoriteRepository.saveAll(List.of(fe1, fe2));

        userFavoriteRepository.save(UserFavorite.createNewUserFavorite(saveUser, saveFavoriteEntities.get(0)));
        userFavoriteRepository.save(UserFavorite.createNewUserFavorite(saveUser, saveFavoriteEntities.get(1)));

        String changeNickname = "변경된닉네임";
        UserRequest.PatchUserInfo request = UserRequest.PatchUserInfo.of(changeNickname, List.of(fe1.getName()));

        em.flush();
        em.clear();

        // when
        userService.patchUserInfo(request, saveUser.getId());

        // then
        User findUser = userRepository.findById(saveUser.getId()).get();
        List<String> findFavoriteList = userFavoriteRepository.findFavoriteNamesByUserId(saveUser.getId());
        assertThat(findUser)
                .extracting("nickname")
                .isEqualTo(changeNickname);
        assertThat(findFavoriteList)
                .hasSize(1)
                .containsExactlyInAnyOrder("관심사1");
    }

    @DisplayName("회원 정보를 수정 합니다. 서버에 등록된 관심사가 아니라면, 오류가 발생합니다.")
    @Test
    void patchUserInfo3() {
        // given
        String loginId = "test1@email.com";
        String password = "!test1234";
        String nickname = "test";
        User newUser = User.createNewUser(loginId, password, nickname);
        User saveUser = userRepository.save(newUser);

        List<String> favorite = List.of("관심사1", "관심사2");
        FavoriteEntity fe1 = FavoriteEntity.createNewFavorite("관심사1");
        List<FavoriteEntity> saveFavoriteEntities = favoriteRepository.saveAll(List.of(fe1));

        userFavoriteRepository.save(UserFavorite.createNewUserFavorite(saveUser, saveFavoriteEntities.get(0)));

        String changeNickname = "변경된닉네임";
        UserRequest.PatchUserInfo request = UserRequest.PatchUserInfo.of(changeNickname, favorite);

        em.flush();
        em.clear();

        // when // then
        assertThatThrownBy( () -> userService.patchUserInfo(request, saveUser.getId()))
                .isInstanceOf(CustomException.class)
                .extracting("code")
                .isEqualTo(ErrorCode.NOT_EXIST_FAVORITE);
    }
}