package z9.second.domain.user.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.favorite.entity.FavoriteEntity;
import z9.second.domain.user.dto.UserRequest;
import z9.second.domain.user.dto.UserResponse;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;
import z9.second.integration.SpringBootTestSupporter;
import z9.second.integration.factory.UserFactory;
import z9.second.model.schedules.SchedulesCheckInEntity;
import z9.second.model.schedules.SchedulesEntity;
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
        List<User> saveUserList = userFactory.saveAndCreateUserData(1);
        User saveUser = saveUserList.getFirst();

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
                .containsExactly(saveUser.getNickname(), UserType.NORMAL.getValue(), UserRole.ROLE_USER.getValue());
        assertThat(findData.getCreatedAt()).matches("\\d{4}-\\d{2}-\\d{2}");
        assertThat(findData.getFavorite())
                .hasSize(2)
                .containsExactlyInAnyOrder("관심사1", "관심사2");
    }

    @DisplayName("회원 정보를 찾습니다. 관심사가 없을 경우, 빈 배열이 출력되어야 합니다.")
    @Test
    void findUserInfo2() {
        // given
        List<User> saveUserList = userFactory.saveAndCreateUserData(1);
        User saveUser = saveUserList.getFirst();

        em.flush();
        em.clear();

        // when
        UserResponse.UserInfo findData = userService.findUserInfo(saveUser.getId());

        // then
        assertThat(findData)
                .extracting("nickname", "type", "role")
                .containsExactly(saveUser.getNickname(), UserType.NORMAL.getValue(), UserRole.ROLE_USER.getValue());
        assertThat(findData.getCreatedAt()).matches("\\d{4}-\\d{2}-\\d{2}");
        assertThat(findData.getFavorite())
                .hasSize(0);
    }

    @DisplayName("회원 정보를 찾습니다. 없는 회원으로 조회 시, 오류 메세지가 서빙됩니다.")
    @Test
    void findUserInfo3() {
        // given

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
        List<User> saveUserList = userFactory.saveAndCreateUserData(1);
        User saveUser = saveUserList.getFirst();

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
        List<User> saveUserList = userFactory.saveAndCreateUserData(1);
        User saveUser = saveUserList.getFirst();

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
        List<User> saveUserList = userFactory.saveAndCreateUserData(1);
        User saveUser = saveUserList.getFirst();

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

    @DisplayName("회원이 참석 예정인 일정을 찾습니다.")
    @Test
    void findUserSchedules1() {
        // given
        //사용자 등록
        List<User> saveUserList = userFactory.saveAndCreateUserData(1);
        User saveUser = saveUserList.getFirst();

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

        //스케줄 생성 2개
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

        //체크인 등록 2개
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
        UserResponse.UserSchedule findData = userService.findUserSchedules(saveUser.getId());

        // then
        assertThat(findData.getSchedule())
                .hasSize(1);
        assertThat(findData.getSchedule().getFirst())
                .extracting("meetingTitle")
                .isEqualTo("정기모임 1회차");
    }
}