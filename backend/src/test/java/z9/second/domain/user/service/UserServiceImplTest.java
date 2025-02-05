package z9.second.domain.user.service;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.favorite.entity.FavoriteEntity;
import z9.second.domain.user.dto.UserResponse;
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
}