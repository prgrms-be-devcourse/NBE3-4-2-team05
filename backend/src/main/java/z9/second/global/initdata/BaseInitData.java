package z9.second.global.initdata;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import z9.second.model.sample.SampleEntity;
import z9.second.model.sample.SampleRepository;
import z9.second.model.user.User;
import z9.second.model.user.UserRepository;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class BaseInitData {

    private final SampleRepository sampleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    void init() {
        List<SampleEntity> sampleData = createSampleData(10);
        List<User> savedUserData = createUserData(10);
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
}
