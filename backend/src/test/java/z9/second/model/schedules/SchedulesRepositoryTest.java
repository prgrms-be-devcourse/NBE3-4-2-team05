package z9.second.model.schedules;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.favorite.entity.FavoriteEntity;
import z9.second.integration.SpringBootTestSupporter;
import z9.second.model.checkIn.CheckInEntity;
import z9.second.model.user.User;
import z9.second.model.userfavorite.UserFavorite;

@Transactional
class SchedulesRepositoryTest extends SpringBootTestSupporter {

    @DisplayName("회원 아이디로, 회원이 참석하기로 한 모임 일정을 찾습니다.")
    @Test
    void findUserSchedulesInfoByUserId1() {
        // given
        //사용자 등록
        String loginId = "test1@email.com";
        String password = "!test1234";
        String nickname = "test";
        User newUser = User.createNewUser(loginId, password, nickname);
        User saveUser = userRepository.save(newUser);

        //관심사 등록
        FavoriteEntity fe1 = FavoriteEntity.createNewFavorite("관심사1");
        FavoriteEntity fe2 = FavoriteEntity.createNewFavorite("관심사2");
        List<FavoriteEntity> saveFavoriteEntities = favoriteRepository.saveAll(List.of(fe1, fe2));
        userFavoriteRepository.save(UserFavorite.createNewUserFavorite(saveUser, saveFavoriteEntities.get(0)));
        userFavoriteRepository.save(UserFavorite.createNewUserFavorite(saveUser, saveFavoriteEntities.get(1)));

        //방 생성
        ClassEntity newClass = ClassEntity.builder()
                .name("새로운 모임")
                .favorite(fe1.getName())
                .description("모임 설명 글 입니다!!")
                .masterId(saveUser.getId())
                .build();
        newClass.addMember(saveUser.getId());
        ClassEntity saveClass = classRepository.save(newClass);

        //스케줄 생성
        SchedulesEntity schedules = SchedulesEntity.builder()
                .classes(saveClass)
                .meetingTime(LocalDateTime.now().toString())
                .meetingTitle("정기모임 1회차")
                .build();
        SchedulesEntity saveSchedule = schedulesRepository.save(schedules);

        SchedulesEntity schedules2 = SchedulesEntity.builder()
                .classes(saveClass)
                .meetingTime(LocalDateTime.now().toString())
                .meetingTitle("정기모임 2회차")
                .build();
        SchedulesEntity saveSchedule2 = schedulesRepository.save(schedules2);

        //체크인 등록
        CheckInEntity newCheckin = CheckInEntity
                .builder()
                .schedules(saveSchedule)
                .userId(saveUser.getId())
                .checkIn(true)
                .build();
        checkInEntityRepository.save(newCheckin);

        CheckInEntity newCheckin2 = CheckInEntity
                .builder()
                .schedules(saveSchedule)
                .userId(saveUser.getId())
                .checkIn(false)
                .build();
        checkInEntityRepository.save(newCheckin2);

        em.flush();
        em.clear();

        // when
        List<SchedulesEntity> findData = schedulesRepository.findUserSchedulesInfoByUserId(
                saveUser.getId());

        // then
        assertThat(findData)
                .hasSize(1);

        assertThat(findData.getFirst())
                .extracting("meetingTitle")
                .isEqualTo("정기모임 1회차");
    }

    @DisplayName("회원 아이디로, 회원이 참석하기로 한 모임 일정을 찾습니다. 없으면 빈 배열이 반환됩니다.")
    @Test
    void findUserSchedulesInfoByUserId2() {
        // given
        //사용자 등록
        String loginId = "test1@email.com";
        String password = "!test1234";
        String nickname = "test";
        User newUser = User.createNewUser(loginId, password, nickname);
        User saveUser = userRepository.save(newUser);

        //관심사 등록
        FavoriteEntity fe1 = FavoriteEntity.createNewFavorite("관심사1");
        FavoriteEntity fe2 = FavoriteEntity.createNewFavorite("관심사2");
        List<FavoriteEntity> saveFavoriteEntities = favoriteRepository.saveAll(List.of(fe1, fe2));
        userFavoriteRepository.save(UserFavorite.createNewUserFavorite(saveUser, saveFavoriteEntities.get(0)));
        userFavoriteRepository.save(UserFavorite.createNewUserFavorite(saveUser, saveFavoriteEntities.get(1)));

        //방 생성
        ClassEntity newClass = ClassEntity.builder()
                .name("새로운 모임")
                .favorite(fe1.getName())
                .description("모임 설명 글 입니다!!")
                .masterId(saveUser.getId())
                .build();
        newClass.addMember(saveUser.getId());
        ClassEntity saveClass = classRepository.save(newClass);

        em.flush();
        em.clear();

        // when
        List<SchedulesEntity> findData = schedulesRepository.findUserSchedulesInfoByUserId(
                saveUser.getId());

        // then
        assertThat(findData)
                .hasSize(0);
    }
}