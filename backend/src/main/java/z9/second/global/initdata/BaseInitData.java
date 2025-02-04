package z9.second.global.initdata;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.classes.entity.ClassUserEntity;
import z9.second.domain.classes.repository.ClassRepository;
import z9.second.domain.classes.repository.ClassUserRepository;
import z9.second.model.sample.SampleEntity;
import z9.second.model.sample.SampleRepository;
import z9.second.model.schedules.SchedulesEntity;
import z9.second.model.schedules.SchedulesRepository;
import z9.second.model.user.User;
import z9.second.model.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class BaseInitData {

    private final SampleRepository sampleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClassRepository classRepository;
    private final ClassUserRepository classUserRepository;
    private final SchedulesRepository schedulesRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    void init() {
        List<SampleEntity> sampleData = createSampleData(10);
        List<User> savedUserData = createUserData(10);
        List<ClassEntity> savedClassData = createClassData(10, savedUserData);
        createScheduleData(savedClassData);
    }

    private List<SampleEntity> createSampleData(final int count) {
        if (sampleRepository.count() != 0) {
            return sampleRepository.findAll();
        }
        if (count == 0) {
            return null;
        }

        List<SampleEntity> savedDataList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String firstName = "김";
            String secondName = String.format("%s%d", "아무개", i);
            SampleEntity sample = SampleEntity.builder().firstName(firstName).secondName(secondName)
                    .age(i).build();
            savedDataList.add(sampleRepository.save(sample));
        }

        return savedDataList;
    }

    private List<User> createUserData(final int count) {
        if (userRepository.count() != 0) {
            return userRepository.findAll();
        }
        if (count == 0) {
            return null;
        }

        List<User> savedUserList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String loginId = String.format("%s%d@email.com", "test", i);
            String password = passwordEncoder.encode("!test1234");
            String nickname = String.format("%s%d", "test", i);
            savedUserList.add(
                    userRepository.save(
                            User.createNewUser(loginId, password, nickname)));
        }

        return savedUserList;
    }

    private List<ClassEntity> createClassData(final int count, final List<User> users) {
        if (classRepository.count() != 0) {
            return classRepository.findAll();
        }

        List<ClassEntity> savedClassList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            // 각 클래스의 모임장을 users 리스트에서 순차적으로 설정
            Long masterId = users.get(i-1).getId();

            ClassEntity classEntity = ClassEntity.builder()
                    .name("테스트 모임" + i)
                    .favorite("취미" + i)
                    .description("테스트 모임" + i + "의 설명입니다.")
                    .masterId(masterId)
                    .build();

            ClassEntity savedClass = classRepository.save(classEntity);
            savedClassList.add(savedClass);

            // 모임장을 ClassUser로 추가
            ClassUserEntity classUser = ClassUserEntity.builder()
                    .classes(savedClass)
                    .userId(masterId)
                    .build();
            classUserRepository.save(classUser);
        }

        return savedClassList;
    }

    private void createScheduleData(List<ClassEntity> classes) {
        if (schedulesRepository.count() != 0) {
            return;
        }

        for (ClassEntity classEntity : classes) {
            // 각 클래스마다 3개의 일정 생성
            for (int i = 1; i <= 3; i++) {
                SchedulesEntity schedule = SchedulesEntity.builder()
                        .classes(classEntity)
                        .meetingTime("2025-02-" + String.format("%02d", i) + " 14:00:00")
                        .meetingTitle("모임 " + classEntity.getId() + "의 " + i + "번째 일정")
                        .build();

                schedulesRepository.save(schedule);
            }
        }
    }
}