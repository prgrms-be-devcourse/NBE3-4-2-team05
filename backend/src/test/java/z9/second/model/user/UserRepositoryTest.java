package z9.second.model.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import z9.second.integration.SpringBootTestSupporter;

@Transactional
class UserRepositoryTest extends SpringBootTestSupporter {

    @DisplayName("로그인 아이디로, 사용자 정보를 조회합니다.")
    @Test
    void findByLoginId() {
        // given
        String loginId = "test@email.com";
        String password = "!asdf1234";
        String nickname ="테스터";
        User savedUser = userRepository.save(User.createNewUser(loginId, password, nickname));
        em.flush();
        em.clear();

        // when
        Optional<User> findOptionalData = userRepository.findByLoginId(loginId);

        // then
        assertThat(findOptionalData).isPresent();
        User findData = findOptionalData.get();
        assertThat(findData)
                .extracting("loginId", "nickname", "status", "role", "id")
                .containsExactly(loginId, nickname, UserStatus.ACTIVE, UserRole.ROLE_USER, savedUser.getId());
    }
}