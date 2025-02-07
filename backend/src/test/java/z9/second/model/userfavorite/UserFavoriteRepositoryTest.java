package z9.second.model.userfavorite;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.favorite.entity.FavoriteEntity;
import z9.second.integration.SpringBootTestSupporter;
import z9.second.integration.factory.UserFactory;
import z9.second.model.user.User;

@Transactional
class UserFavoriteRepositoryTest extends SpringBootTestSupporter {

    @DisplayName("회원 아이디로, 관심사 목록을 조회합니다.")
    @Test
    void findFavoriteNamesByUserId1() {
        // given
        List<User> saveUserList = userFactory.saveAndCreateUserData(1);
        User saveUser = saveUserList.getFirst();

        List<String> favorite = List.of("관심사1", "관심사2");
        FavoriteEntity fe1 = FavoriteEntity.createNewFavorite("관심사1");
        FavoriteEntity fe2 = FavoriteEntity.createNewFavorite("관심사2");
        List<FavoriteEntity> saveFavoriteEntities = favoriteRepository.saveAll(List.of(fe1, fe2));

        userFavoriteRepository.save(UserFavorite.createNewUserFavorite(saveUser, saveFavoriteEntities.get(0)));
        userFavoriteRepository.save(UserFavorite.createNewUserFavorite(saveUser, saveFavoriteEntities.get(1)));

        em.flush();
        em.clear();

        // when
        List<String> findDataList = userFavoriteRepository.findFavoriteNamesByUserId(
                saveUser.getId());

        // then
        assertThat(findDataList).hasSize(2);
        assertThat(findDataList).containsExactlyInAnyOrder("관심사1", "관심사2");
    }
}