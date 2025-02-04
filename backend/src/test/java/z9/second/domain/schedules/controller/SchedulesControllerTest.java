package z9.second.domain.schedules.controller;

import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.authentication.dto.AuthenticationRequest;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.schedules.dto.SchedulesRequestDto;
import z9.second.integration.SpringBootTestSupporter;
import z9.second.model.schedules.SchedulesEntity;
import z9.second.model.user.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_HEADER;

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SchedulesControllerTest extends SpringBootTestSupporter {

    private String loginId = "test@email.com";
    private String password = "!test1234";
    private String nickname = "테스터";
    private User user;
    private ClassEntity classEntity;
    private String accessToken;

    @BeforeEach
    void setUp() throws Exception {
        // 테스트 유저 생성 및 로그인하여 토큰 받기
        user = userRepository.save(User.createNewUser(loginId, passwordEncoder.encode(password), nickname));

        AuthenticationRequest.Login request = AuthenticationRequest.Login.of(loginId, password);
        ResultActions result = mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        accessToken = result.andReturn().getResponse().getHeader(ACCESS_TOKEN_HEADER);

        // 테스트 모임 생성
        classEntity = classRepository.save(ClassEntity.builder()
                .masterId(user.getId())
                .name("테스트 모임")
                .favorite("취미")
                .description("테스트 모임입니다")
                .build());
    }

    @Test
    @Order(1)
    @DisplayName("모임 일정 생성")
    void createSchedule() throws Exception {
        // given
        SchedulesRequestDto.RequestData request = SchedulesRequestDto.RequestData.builder()
                .classId(classEntity.getId())
                .meetingTime("2025-02-05 14:00:00")
                .meetingTitle("테스트 일정")
                .build();

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/schedules/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", accessToken));

        // then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.meetingTitle").value("테스트 일정"));
    }

    @Test
    @Order(2)
    @DisplayName("모임 일정 생성 실패 - 유효성 검증 실패")
    void createSchedule_ValidationFail() throws Exception {
        // given
        SchedulesRequestDto.RequestData request = SchedulesRequestDto.RequestData.builder()
                .classId(classEntity.getId())
                .meetingTime("2025-02-05") // 잘못된 형식
                .meetingTitle("테") // 2글자 미만
                .build();

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/schedules/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header(ACCESS_TOKEN_HEADER, accessToken));

        // then
        result.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    @DisplayName("모임 전체 일정 조회")
    void getSchedulesList() throws Exception {
        // given
        schedulesRepository.save(SchedulesEntity.builder()
                .classes(classEntity)
                .meetingTime("2025-02-05 14:00:00")
                .meetingTitle("테스트 일정1")
                .build());

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/schedules/classes/" + classEntity.getId())
                .header(ACCESS_TOKEN_HEADER, accessToken));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].meetingTitle").value("테스트 일정1"));
    }

    @Test
    @Order(4)
    @DisplayName("모임 특정 일정 조회")
    void getScheduleDetail() throws Exception {
        // given
        SchedulesEntity schedule = schedulesRepository.save(SchedulesEntity.builder()
                .classes(classEntity)
                .meetingTime("2025-02-05 14:00:00")
                .meetingTitle("테스트 일정")
                .build());

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/schedules/" + schedule.getId() + "/classes/" + classEntity.getId())
                .header(ACCESS_TOKEN_HEADER, accessToken));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.meetingTitle").value("테스트 일정"));
    }
}