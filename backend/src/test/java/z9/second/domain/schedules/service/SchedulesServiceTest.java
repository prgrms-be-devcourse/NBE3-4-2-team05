package z9.second.domain.schedules.service;

import org.junit.jupiter.api.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.classes.entity.ClassUserEntity;
import z9.second.domain.schedules.dto.SchedulesRequestDto;
import z9.second.domain.schedules.dto.SchedulesResponseDto;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;
import z9.second.integration.SpringBootTestSupporter;
import z9.second.model.schedules.SchedulesEntity;
import z9.second.model.user.User;

import java.util.ArrayList;
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

    @DisplayName("일정 생성 실패 - 모임장이 아닌 멤버")
    @Test
    @Order(5)
    void create_NotMaster() {
        // given
        User master = userRepository.save(User.createNewUser("master@email.com",
                passwordEncoder.encode("!test1234"), "모임장"));
        User member = userRepository.save(User.createNewUser("member@email.com",
                passwordEncoder.encode("!test1234"), "멤버"));

        ClassEntity classEntity = classRepository.save(ClassEntity.builder()
                .masterId(master.getId())
                .name("테스트 모임")
                .favorite("취미")
                .description("테스트 모임입니다")
                .build());

        // 모임 멤버로 추가
        classUserRepository.save(ClassUserEntity.builder()
                .classes(classEntity)
                .userId(member.getId())
                .build());

        SchedulesRequestDto.RequestData request = SchedulesRequestDto.RequestData.builder()
                .classId(classEntity.getId())
                .meetingTime("2025-02-05 14:00:00")
                .meetingTitle("테스트 일정")
                .build();

        // when & then
        assertThatThrownBy(() -> schedulesService.create(request, member.getId()))
                .isInstanceOf(CustomException.class)
                .extracting("code")
                .isEqualTo(ErrorCode.CLASS_ACCESS_DENIED);
    }

    @DisplayName("전체 일정 조회 - 모임 멤버로 조회 성공")
    @Test
    @Order(6)
    void getSchedulesList_AsMember() {
        // given
        // 1. 테스트용 모임장과 멤버 생성
        User master = userRepository.save(User.createNewUser("master@email.com",
                passwordEncoder.encode("!test1234"), "모임장"));
        User member = userRepository.save(User.createNewUser("member@email.com",
                passwordEncoder.encode("!test1234"), "멤버"));

        // 2. 테스트용 모임 생성 (모임장: master)
        ClassEntity classEntity = classRepository.save(ClassEntity.builder()
                .masterId(master.getId())
                .name("테스트 모임")
                .favorite("취미")
                .description("테스트 모임입니다")
                .build());

        // 3. 모임의 멤버 목록 초기화 (테스트 환경 설정)
        ReflectionTestUtils.setField(classEntity, "users", new ArrayList<>());

        // 4. member를 모임 멤버로 추가
        ClassUserEntity classUser = ClassUserEntity.builder()
                .classes(classEntity)
                .userId(member.getId())
                .build();
        classUserRepository.save(classUser);

        // users 컬렉션에 멤버 추가
        classEntity.getUsers().add(classUser);

        schedulesRepository.save(SchedulesEntity.builder()
                .classes(classEntity)
                .meetingTime("2025-02-05 14:00:00")
                .meetingTitle("테스트 일정1")
                .build());

        // when
        List<SchedulesResponseDto.ResponseData> schedules =
                schedulesService.getSchedulesList(classEntity.getId(), member.getId());

        // then
        assertThat(schedules).hasSize(1);
        assertThat(schedules.get(0).getMeetingTitle()).isEqualTo("테스트 일정1");
    }

    @DisplayName("특정 일정 상세 조회 - 모임 멤버로 조회 성공")
    @Test
    @Order(7)
    void getScheduleDetail_AsMember() {
        // given
        User master = userRepository.save(User.createNewUser("master@email.com",
                passwordEncoder.encode("!test1234"), "모임장"));
        User member = userRepository.save(User.createNewUser("member@email.com",
                passwordEncoder.encode("!test1234"), "멤버"));

        ClassEntity classEntity = classRepository.save(ClassEntity.builder()
                .masterId(master.getId())
                .name("테스트 모임")
                .favorite("취미")
                .description("테스트 모임입니다")
                .build());

        // ClassEntity의 users 컬렉션 초기화
        ReflectionTestUtils.setField(classEntity, "users", new ArrayList<>());

        // 모임 멤버로 추가
        ClassUserEntity classUser = ClassUserEntity.builder()
                .classes(classEntity)
                .userId(member.getId())
                .build();
        classUserRepository.save(classUser);

        // users 컬렉션에 멤버 추가
        classEntity.getUsers().add(classUser);

        SchedulesEntity schedule = schedulesRepository.save(SchedulesEntity.builder()
                .classes(classEntity)
                .meetingTime("2025-02-05 14:00:00")
                .meetingTitle("테스트 일정")
                .build());

        // when
        SchedulesResponseDto.ResponseData response =
                schedulesService.getScheduleDetail(schedule.getId(), classEntity.getId(), member.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMeetingTitle()).isEqualTo("테스트 일정");
    }
}