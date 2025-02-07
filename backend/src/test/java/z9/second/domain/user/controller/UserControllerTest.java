package z9.second.domain.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.favorite.entity.FavoriteEntity;
import z9.second.domain.user.dto.UserRequest;
import z9.second.global.response.SuccessCode;
import z9.second.integration.SpringBootTestSupporter;
import z9.second.integration.security.WithCustomUser;
import z9.second.model.checkIn.CheckInEntity;
import z9.second.model.schedules.SchedulesEntity;
import z9.second.model.user.User;
import z9.second.model.user.UserRole;
import z9.second.model.user.UserType;
import z9.second.model.userfavorite.UserFavorite;

@Transactional
class UserControllerTest extends SpringBootTestSupporter {

    @BeforeEach
    void setUp() {
        em.createNativeQuery("ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1").executeUpdate();
    }

    @WithCustomUser
    @DisplayName("로그인 정보로 회원 정보를 반환합니다.")
    @Test
    void findUserInfo() throws Exception {
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

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/users"));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(SuccessCode.FIND_USER_INFO_SUCCESS.getIsSuccess()))
                .andExpect(jsonPath("$.message").value(SuccessCode.FIND_USER_INFO_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.code").value(SuccessCode.FIND_USER_INFO_SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.nickname").value(nickname))
                .andExpect(jsonPath("$.data.type").value(UserType.NORMAL.getValue()))
                .andExpect(jsonPath("$.data.role").value(UserRole.ROLE_USER.getValue()))
                .andExpect(jsonPath("$.data.createdAt").value(Matchers.matchesPattern("\\d{4}-\\d{2}-\\d{2}")))
                .andExpect(jsonPath("$.data.favorite").isArray())
                .andExpect(jsonPath("$.data.favorite.length()").value(2))
                .andExpect(jsonPath("$.data.favorite").value(Matchers.containsInAnyOrder("관심사1", "관심사2")));
    }

    @WithCustomUser
    @DisplayName("회원의 정보를 수정 합니다. 관심사와 닉네임을 수정할 수 있습니다.")
    @Test
    void modifyUserInfo() throws Exception {
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

        // when
        ResultActions result = mockMvc.perform(patch("/api/v1/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(SuccessCode.PATCH_USER_INFO_SUCCESS.getIsSuccess()))
                .andExpect(jsonPath("$.message").value(SuccessCode.PATCH_USER_INFO_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.code").value(SuccessCode.PATCH_USER_INFO_SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.nickname").doesNotExist());
    }

    @WithCustomUser
    @DisplayName("로그인 된 회원의 참석할 모임 정보를 모두 찾습니다.")
    @Test
    void findUserSchedules() throws Exception {
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

        //스케줄 생성 2개
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        SchedulesEntity schedules = SchedulesEntity.builder()
                .classes(saveClass)
                .meetingTime(formattedTime)
                .meetingTitle("정기모임 1회차")
                .build();
        SchedulesEntity saveSchedule = schedulesRepository.save(schedules);

        SchedulesEntity schedules2 = SchedulesEntity.builder()
                .classes(saveClass)
                .meetingTime(formattedTime)
                .meetingTitle("정기모임 2회차")
                .build();
        SchedulesEntity saveSchedule2 = schedulesRepository.save(schedules2);

        //체크인 등록 2개
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
        ResultActions result = mockMvc.perform(get("/api/v1/users/schedules"));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(SuccessCode.FIND_USER_SCHEDULES_SUCCESS.getIsSuccess()))
                .andExpect(jsonPath("$.message").value(SuccessCode.FIND_USER_SCHEDULES_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.code").value(SuccessCode.FIND_USER_SCHEDULES_SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.schedule").isArray())
                .andExpect(jsonPath("$.data.schedule.length()").value(1))
                .andExpect(jsonPath("$.data.schedule[0].classId").isNotEmpty())
                .andExpect(jsonPath("$.data.schedule[0].meetingTime").value(Matchers.matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")))
                .andExpect(jsonPath("$.data.schedule[0].meetingTitle").value("정기모임 1회차"));
    }
}