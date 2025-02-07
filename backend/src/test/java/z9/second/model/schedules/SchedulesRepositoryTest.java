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
import z9.second.model.user.User;

@Transactional
class SchedulesRepositoryTest extends SpringBootTestSupporter {

    @DisplayName("회원 아이디로, 회원이 참석하기로 한 모임 일정을 찾습니다.")
    @Test
    void findUserSchedulesInfoByUserId1() {
        // given
        //사용자 등록
        List<User> saveUserList = userFactory.saveAndCreateUserData(1);
        User saveUser = saveUserList.getFirst();

        // 관심사 등록
        List<FavoriteEntity> saveFavoriteList = favoriteFactory.saveAndCreateFavoriteData(2);
        FavoriteEntity saveFavorite = saveFavoriteList.getFirst();

        // 회원-관심사 등록
        userFactory.saveUserFavorite(saveUser, saveFavoriteList);

        //방 생성
        ClassEntity newClass = ClassEntity.builder()
                .name("새로운 모임")
                .favorite(saveFavorite.getName())
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
        SchedulesCheckInEntity newCheckin = SchedulesCheckInEntity
                .builder()
                .schedules(saveSchedule)
                .userId(saveUser.getId())
                .checkIn(true)
                .build();
        schedulesCheckInEntityRepository.save(newCheckin);

        SchedulesCheckInEntity newCheckin2 = SchedulesCheckInEntity
                .builder()
                .schedules(saveSchedule)
                .userId(saveUser.getId())
                .checkIn(false)
                .build();
        schedulesCheckInEntityRepository.save(newCheckin2);

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
        List<User> saveUserList = userFactory.saveAndCreateUserData(1);
        User saveUser = saveUserList.getFirst();

        // 관심사 등록
        List<FavoriteEntity> saveFavoriteList = favoriteFactory.saveAndCreateFavoriteData(2);
        FavoriteEntity saveFavorite = saveFavoriteList.getFirst();

        // 회원-관심사 등록
        userFactory.saveUserFavorite(saveUser, saveFavoriteList);

        //방 생성
        ClassEntity newClass = ClassEntity.builder()
                .name("새로운 모임")
                .favorite(saveFavorite.getName())
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