package z9.second.domain.schedules.base;

import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.classes.entity.ClassUserEntity;
import z9.second.domain.schedules.dto.SchedulesRequestDto;
import z9.second.integration.SpringBootTestSupporter;
import z9.second.model.schedules.SchedulesEntity;
import z9.second.model.user.User;

import java.util.ArrayList;

@Transactional
public abstract class SchedulesBaseTest extends SpringBootTestSupporter {
    // 공통으로 사용되는 상수
    protected static final String TEST_PASSWORD = "!test1234";
    protected static final String TEST_MEETING_TIME = "2025-02-05 14:00:00";
    protected static final String TEST_MEETING_TITLE = "테스트 일정";

    protected User createTestUser(String email, String nickname) {
        return userRepository.save(User.createNewUser(
                email,
                passwordEncoder.encode(TEST_PASSWORD),
                nickname
        ));
    }

    protected ClassEntity createTestClass(Long masterId) {
        return classRepository.save(ClassEntity.builder()
                .masterId(masterId)
                .name("테스트 모임")
                .favorite("취미")
                .description("테스트 모임입니다")
                .build());
    }

    protected SchedulesEntity createTestSchedule(ClassEntity classEntity, String meetingTitle) {
        return schedulesRepository.save(SchedulesEntity.builder()
                .classes(classEntity)
                .meetingTime("2025-02-05 14:00:00")
                .meetingTitle(meetingTitle)
                .build());
    }

    // 멤버 추가 공통 메서드
    protected void addMemberToClass(User member, ClassEntity classEntity) {
        ReflectionTestUtils.setField(classEntity, "users", new ArrayList<>());
        ClassUserEntity classUser = ClassUserEntity.builder()
                .classes(classEntity)
                .userId(member.getId())
                .build();
        classUserRepository.save(classUser);
        classEntity.getUsers().add(classUser);
    }

    protected SchedulesRequestDto.RequestData createScheduleRequest(Long classId) {
        return SchedulesRequestDto.RequestData.builder()
                .classId(classId)
                .meetingTime(TEST_MEETING_TIME)
                .meetingTitle(TEST_MEETING_TITLE)
                .build();
    }
}
