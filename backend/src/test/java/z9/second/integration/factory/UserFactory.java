package z9.second.integration.factory;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import z9.second.model.user.User;
import z9.second.model.user.UserRepository;

@Component
@RequiredArgsConstructor
public final class UserFactory {

    public static final String USER_LOGIN_ID_PREFIX = "test";
    public static final String USER_LOGIN_ID_END = "@email.com";
    public static final String USER_LOGIN_PASSWORD = "!test1234";
    public static final String USER_LOGIN_NICKNAME_PREFIX = "TEST";

    private final EntityManager em;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * count :  생성할 회원 수량. 1부터 시작
     *          0일 경우, 생성하지 않음.
     */
    public List<User> saveAndCreateUserData(final int count) {
        if(count == 0) return List.of();

        ArrayList<User> savedUserList = new ArrayList<>(count);

        for(int index=1; index<=count; index++) {
            String loginId = String.format("%s%d%s", USER_LOGIN_ID_PREFIX, index, USER_LOGIN_ID_END);
            String password = passwordEncoder.encode(USER_LOGIN_PASSWORD);
            String nickname = String.format("%s%d", USER_LOGIN_NICKNAME_PREFIX, index);

            User newUser = User.createNewUser(loginId, password, nickname);
            User saveUser = userRepository.save(newUser);

            savedUserList.add(saveUser);
        }

        em.flush();
        em.clear();

        return savedUserList;
    }
}
