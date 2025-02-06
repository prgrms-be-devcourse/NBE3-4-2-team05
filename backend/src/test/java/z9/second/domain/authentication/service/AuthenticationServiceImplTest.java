package z9.second.domain.authentication.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.authentication.dto.AuthenticationRequest;
import z9.second.domain.authentication.dto.AuthenticationResponse;
import z9.second.domain.favorite.entity.FavoriteEntity;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;
import z9.second.integration.SpringBootTestSupporter;
import z9.second.model.user.User;
import z9.second.model.user.UserRole;
import z9.second.model.user.UserStatus;
import z9.second.model.user.UserType;

@Transactional
class AuthenticationServiceImplTest extends SpringBootTestSupporter {

    @DisplayName("로그인 아이디로, 로그인을 진행 합니다. 성공되면, access/refresh token 을 반환합니다.")
    @Test
    void login() {
        // given
        String loginId = "test@email.com";
        String password = "!test1234";
        String nickName = "테스터";
        userRepository.save(
                User.createNewUser(loginId, passwordEncoder.encode(password), nickName));
        em.flush();
        em.clear();

        // when
        AuthenticationResponse.UserToken userToken = authenticationService.login(
                AuthenticationRequest.Login.of(loginId, password));

        // then
        assertThat(userToken.getAccessToken())
                .isNotNull();
        assertThat(userToken.getRefreshToken())
                .isNotNull();
    }

    @DisplayName("로그인 아이디가 틀리거나, 등등 로그인이 불가능하다면 오류 메세지가 출력됩니다!")
    @Test
    void login2() {
        // given
        String loginId = "test@email.com";
        String password = "!test1234";

        // when // then
        assertThatThrownBy(() -> authenticationService.login(AuthenticationRequest.Login.of(loginId, password)))
                .isInstanceOf(CustomException.class)
                .extracting("code")
                .isEqualTo(ErrorCode.LOGIN_FAIL);
    }

    @DisplayName("회원가입을 진행합니다.")
    @Test
    void signup1() {
        // given
        String loginId = "test1@email.com";
        String password = "!test1234";
        List<String> favorite = List.of("관심사1", "관심사2");
        String nickname = "test1";

        FavoriteEntity fe1 = FavoriteEntity.createNewFavorite("관심사1");
        FavoriteEntity fe2 = FavoriteEntity.createNewFavorite("관심사2");
        favoriteRepository.saveAll(List.of(fe1, fe2));

        AuthenticationRequest.Signup signupDto = AuthenticationRequest.Signup.of(loginId, password,
                favorite, nickname);

        em.flush();
        em.clear();

        // when
        authenticationService.signup(signupDto);

        // then
        Optional<User> findOptionalData = userRepository.findByLoginId(loginId);
        assertThat(findOptionalData).isPresent();
        User findData = findOptionalData.get();
        assertThat(findData)
                .extracting("loginId", "nickname", "type", "status", "role")
                .containsExactly(loginId, nickname, UserType.NORMAL, UserStatus.ACTIVE, UserRole.ROLE_USER);
    }

    @DisplayName("회원가입을 진행합니다. 중복된 회원 아이디는 회원가입 되지 않습니다. 오류메세지를 반환합니다.")
    @Test
    void signup2() {
        // given
        String loginId = "test1@email.com";
        String password = "!test1234";
        List<String> favorite = List.of("관심사1", "관심사2");
        String nickname = "test1";

        FavoriteEntity fe1 = FavoriteEntity.createNewFavorite("관심사1");
        FavoriteEntity fe2 = FavoriteEntity.createNewFavorite("관심사2");
        favoriteRepository.saveAll(List.of(fe1, fe2));

        AuthenticationRequest.Signup signupDto = AuthenticationRequest.Signup.of(loginId, password,
                favorite, nickname);

        User newUser = User.createNewUser(loginId, password, "test2");
        userRepository.save(newUser);

        em.flush();
        em.clear();

        // when // then
        assertThatThrownBy(() -> authenticationService.signup(signupDto))
                .isInstanceOf(CustomException.class)
                .extracting("code")
                .isEqualTo(ErrorCode.DUPLICATED_LOGIN_ID);
    }

    @DisplayName("회원가입을 진행합니다. 중복된 닉네임은 회원가입 되지 않습니다. 오류메세지를 반환합니다.")
    @Test
    void signup3() {
        // given
        String loginId = "test1@email.com";
        String password = "!test1234";
        List<String> favorite = List.of("관심사1", "관심사2");
        String nickname = "test1";

        FavoriteEntity fe1 = FavoriteEntity.createNewFavorite("관심사1");
        FavoriteEntity fe2 = FavoriteEntity.createNewFavorite("관심사2");
        favoriteRepository.saveAll(List.of(fe1, fe2));

        AuthenticationRequest.Signup signupDto = AuthenticationRequest.Signup.of(loginId, password,
                favorite, nickname);

        User newUser = User.createNewUser("test2@email.com", password, nickname);
        userRepository.save(newUser);

        em.flush();
        em.clear();

        // when // then
        assertThatThrownBy(() -> authenticationService.signup(signupDto))
                .isInstanceOf(CustomException.class)
                .extracting("code")
                .isEqualTo(ErrorCode.DUPLICATED_NICKNAME);
    }

    @DisplayName("회원가입을 진행합니다. 등록되지 않은 관심사로는 회원가입이 불가능합니다. 오류메세지를 반환합니다.")
    @Test
    void signup4() {
        // given
        String loginId = "test1@email.com";
        String password = "!test1234";
        List<String> favorite = List.of("미등록 관심사1", "미등록 관심사2");
        String nickname = "test1";

        FavoriteEntity fe1 = FavoriteEntity.createNewFavorite("관심사1");
        FavoriteEntity fe2 = FavoriteEntity.createNewFavorite("관심사2");
        favoriteRepository.saveAll(List.of(fe1, fe2));

        AuthenticationRequest.Signup signupDto = AuthenticationRequest.Signup.of(loginId, password,
                favorite, nickname);

        User newUser = User.createNewUser(loginId, password, nickname);
        userRepository.save(newUser);

        em.flush();
        em.clear();

        // when // then
        assertThatThrownBy(() -> authenticationService.signup(signupDto))
                .isInstanceOf(CustomException.class)
                .extracting("code")
                .isEqualTo(ErrorCode.NOT_EXIST_FAVORITE);
    }
}