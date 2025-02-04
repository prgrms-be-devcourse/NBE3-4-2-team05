package z9.second.domain.schedules.service;

import org.junit.jupiter.api.*;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.schedules.dto.SchedulesRequestDto;
import z9.second.domain.schedules.dto.SchedulesResponseDto;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;
import z9.second.integration.SpringBootTestSupporter;
import z9.second.model.schedules.SchedulesEntity;
import z9.second.model.user.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SchedulesServiceTest extends SpringBootTestSupporter {

    @DisplayName("일정 생성")
    @Test
    @Order(1)
    void create() {
        // given
        String loginId = "test@email.com";
        String password = "!test1234";
        String nickname = "테스터";
        User user = userRepository.save(User.createNewUser(loginId, passwordEncoder.encode(password), nickname));

        ClassEntity classEntity = classRepository.save(ClassEntity.builder()
                .masterId(user.getId())
                .name("테스트 모임")
                .favorite("취미")
                .description("테스트 모임입니다")
                .build());

        SchedulesRequestDto.RequestData request = SchedulesRequestDto.RequestData.builder()
                .classId(classEntity.getId())
                .meetingTime("2025-02-05 14:00:00")
                .meetingTitle("테스트 일정")
                .build();

        // when
        SchedulesResponseDto.ResponseData response = schedulesService.create(request, user.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMeetingTitle()).isEqualTo("테스트 일정");
    }

    @DisplayName("일정 생성 실패 - 존재하지 않는 모임")
    @Test
    @Order(2)
    void create_ClassNotFound() {
        // given
        SchedulesRequestDto.RequestData request = SchedulesRequestDto.RequestData.builder()
                .classId(999L)
                .meetingTime("2025-02-05 14:00:00")
                .meetingTitle("테스트 일정")
                .build();

        // when & then
        assertThatThrownBy(() -> schedulesService.create(request, 1L))
                .isInstanceOf(CustomException.class)
                .extracting("code")
                .isEqualTo(ErrorCode.CLASS_NOT_FOUND);
    }

    @DisplayName("전체 일정 조회")
    @Test
    @Order(3)
    void getSchedulesList() {
        // given
        User user = userRepository.save(User.createNewUser("test@email.com",
                passwordEncoder.encode("!test1234"), "테스터"));

        ClassEntity classEntity = classRepository.save(ClassEntity.builder()
                .masterId(user.getId())
                .name("테스트 모임")
                .favorite("취미")
                .description("테스트 모임입니다")
                .build());

        schedulesRepository.save(SchedulesEntity.builder()
                .classes(classEntity)
                .meetingTime("2025-02-05 14:00:00")
                .meetingTitle("테스트 일정1")
                .build());

        // when
        List<SchedulesResponseDto.ResponseData> schedules =
                schedulesService.getSchedulesList(classEntity.getId(), user.getId());

        // then
        assertThat(schedules).hasSize(1);
        assertThat(schedules.get(0).getMeetingTitle()).isEqualTo("테스트 일정1");
    }

    @DisplayName("특정 일정 상세 조회")
    @Test
    @Order(4)
    void getScheduleDetail() {
        // given
        User user = userRepository.save(User.createNewUser("test@email.com",
                passwordEncoder.encode("!test1234"), "테스터"));

        ClassEntity classEntity = classRepository.save(ClassEntity.builder()
                .masterId(user.getId())
                .name("테스트 모임")
                .favorite("취미")
                .description("테스트 모임입니다")
                .build());

        SchedulesEntity schedule = schedulesRepository.save(SchedulesEntity.builder()
                .classes(classEntity)
                .meetingTime("2025-02-05 14:00:00")
                .meetingTitle("테스트 일정")
                .build());

        // when
        SchedulesResponseDto.ResponseData response =
                schedulesService.getScheduleDetail(schedule.getId(), classEntity.getId(), user.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMeetingTitle()).isEqualTo("테스트 일정");
    }
}