package z9.second.domain.schedules.service;

import org.junit.jupiter.api.*;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.schedules.base.SchedulesBaseTest;
import z9.second.domain.schedules.dto.SchedulesRequestDto;
import z9.second.domain.schedules.dto.SchedulesResponseDto;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;
import z9.second.model.schedules.SchedulesEntity;
import z9.second.model.user.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SchedulesServiceTest extends SchedulesBaseTest {
    private User masterUser;
    private User memberUser;
    private ClassEntity classEntity;
    private SchedulesEntity scheduleEntity;
    private SchedulesRequestDto.RequestData scheduleRequest;

    @BeforeEach
    void setUp() {
        // 마스터 유저와 멤버 유저 생성
        masterUser = createTestUser("master@email.com", "모임장");
        memberUser = createTestUser("member@email.com", "멤버");

        // 테스트 모임 생성 (마스터 기준)
        classEntity = createTestClass(masterUser.getId());

        // 테스트 스케줄 생성
        scheduleEntity = createTestSchedule(classEntity);

        // 요청 데이터 생성
        scheduleRequest = createScheduleRequest(classEntity.getId());
    }

    @Test
    @Order(1)
    @DisplayName("일정 생성")
    void create() {
        // when
        SchedulesResponseDto.ResponseData response = schedulesService.create(scheduleRequest, masterUser.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMeetingTitle()).isEqualTo(TEST_MEETING_TITLE);
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
        // when
        List<SchedulesResponseDto.ResponseData> schedules =
                schedulesService.getSchedulesList(classEntity.getId(), masterUser.getId());

        // then
        assertThat(schedules).hasSize(1);
        assertThat(schedules.getFirst().getMeetingTitle()).isEqualTo("테스트 일정");
    }

    @DisplayName("특정 일정 상세 조회")
    @Test
    @Order(4)
    void getScheduleDetail() {
        // when
        SchedulesResponseDto.ResponseData response =
                schedulesService.getScheduleDetail(scheduleEntity.getId(), classEntity.getId(), masterUser.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMeetingTitle()).isEqualTo("테스트 일정");
    }

    @DisplayName("일정 생성 실패 - 모임장이 아닌 멤버")
    @Test
    @Order(5)
    void create_NotMaster() {
        // given
        addMemberToClass(memberUser, classEntity);

        // when & then
        assertThatThrownBy(() -> schedulesService.create(scheduleRequest, memberUser.getId()))
                .isInstanceOf(CustomException.class)
                .extracting("code")
                .isEqualTo(ErrorCode.CLASS_ACCESS_DENIED);
    }

    @DisplayName("전체 일정 조회 - 모임 멤버로 조회 성공")
    @Test
    @Order(6)
    void getSchedulesList_AsMember() {
        // given
        addMemberToClass(memberUser, classEntity);

        // when
        List<SchedulesResponseDto.ResponseData> schedules =
                schedulesService.getSchedulesList(classEntity.getId(), memberUser.getId());

        // then
        assertThat(schedules).hasSize(1);
        assertThat(schedules.get(0).getMeetingTitle()).isEqualTo("테스트 일정");
    }

    @DisplayName("특정 일정 상세 조회 - 모임 멤버로 조회 성공")
    @Test
    @Order(7)
    void getScheduleDetail_AsMember() {
        // given
        addMemberToClass(memberUser, classEntity);

        // when
        SchedulesResponseDto.ResponseData response =
                schedulesService.getScheduleDetail(scheduleEntity.getId(), classEntity.getId(), memberUser.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMeetingTitle()).isEqualTo("테스트 일정");
    }
}