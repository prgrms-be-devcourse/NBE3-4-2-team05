package z9.second.domain.authentication.controller;


import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_HEADER;
import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_PREFIX;
import static z9.second.global.security.constant.JWTConstant.REFRESH_TOKEN_HEADER;

import jakarta.servlet.http.Cookie;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.authentication.dto.AuthenticationRequest;
import z9.second.domain.favorite.entity.FavoriteEntity;
import z9.second.global.response.SuccessCode;
import z9.second.integration.SpringBootTestSupporter;
import z9.second.integration.factory.UserFactory;
import z9.second.integration.security.WithCustomUser;
import z9.second.model.user.User;

@Transactional
class AuthenticationControllerTest extends SpringBootTestSupporter {

    @DisplayName("로그인을 진행 합니다. access 는 헤더에, refresh 는 쿠키에 담겨서 response 됩니다")
    @Test
    void login() throws Exception {
        // given
        List<User> saveUserList = userFactory.saveAndCreateUserData(1);
        User saveUser = saveUserList.getFirst();
        AuthenticationRequest.Login request =
                AuthenticationRequest.Login.of(saveUser.getLoginId(), UserFactory.USER_LOGIN_PASSWORD);

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(SuccessCode.LOGIN_SUCCESS.getIsSuccess()))
                .andExpect(jsonPath("$.message").value(SuccessCode.LOGIN_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.code").value(SuccessCode.LOGIN_SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(header().exists(ACCESS_TOKEN_HEADER))
                .andExpect(header().string(ACCESS_TOKEN_HEADER, startsWith(ACCESS_TOKEN_PREFIX + " ")))
                .andExpect(cookie().exists(REFRESH_TOKEN_HEADER));
    }

    @DisplayName("회원가입을 진행합니다. 성공 시, 응답 메세지가 출력됩니다.")
    @Test
    void signup() throws Exception {
        // given
        String loginId = "test1@email.com";
        String password = "!test1234";
        List<String> favorite = List.of("관심사1", "관심사2");
        String nickname = "test1";

        FavoriteEntity fe1 = FavoriteEntity.createNewFavorite("관심사1");
        FavoriteEntity fe2 = FavoriteEntity.createNewFavorite("관심사2");
        favoriteRepository.saveAll(List.of(fe1, fe2));

        AuthenticationRequest.Signup request =
                AuthenticationRequest.Signup.of(loginId, password, favorite, nickname);

        em.flush();
        em.clear();

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess").value(SuccessCode.SIGNUP_SUCCESS.getIsSuccess()))
                .andExpect(jsonPath("$.message").value(SuccessCode.SIGNUP_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.code").value(SuccessCode.SIGNUP_SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @WithCustomUser
    @DisplayName("로그아웃을 진행합니다. 등록된 Refresh Cookie 를 삭제 합니다.")
    @Test
    void logout() throws Exception {
        // given
        String refreshToken = "임시RefreshToken";
        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_HEADER, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(1000);

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/logout")
                .cookie(refreshTokenCookie));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(SuccessCode.LOGOUT_SUCCESS.getIsSuccess()))
                .andExpect(jsonPath("$.message").value(SuccessCode.LOGOUT_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.code").value(SuccessCode.LOGOUT_SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(result1 -> {
                    List<String> setCookieHeaders = result1.getResponse().getHeaders("Set-Cookie");
                    assertTrue(setCookieHeaders.stream().anyMatch(cookie -> cookie.contains("RefreshToken=;")));
                    assertTrue(setCookieHeaders.stream().anyMatch(cookie -> cookie.contains("Max-Age=0")));
                });
    }
}