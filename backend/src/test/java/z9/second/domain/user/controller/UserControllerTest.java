package z9.second.domain.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.favorite.entity.FavoriteEntity;
import z9.second.domain.user.dto.UserRequest;
import z9.second.global.response.SuccessCode;
import z9.second.integration.SpringBootTestSupporter;
import z9.second.integration.security.WithCustomUser;
import z9.second.model.user.User;
import z9.second.model.user.UserRole;
import z9.second.model.user.UserType;
import z9.second.model.userfavorite.UserFavorite;

@Transactional
class UserControllerTest extends SpringBootTestSupporter {



    @WithCustomUser
    @DisplayName("로그인 정보로 회원 정보를 반환합니다.")
    @Test
    void findUserInfo() throws Exception {
        // given
        String loginId = "test1@email.com";
        String password = "!test1234";
        String nickname = "test";
        User newUser = User.createNewUser(loginId, password, nickname);
        User saveUser = userRepository.save(newUser);

        List<String> favorite = List.of("관심사1", "관심사2");
        FavoriteEntity fe1 = FavoriteEntity.createNewFavorite("관심사1");
        FavoriteEntity fe2 = FavoriteEntity.createNewFavorite("관심사2");
        List<FavoriteEntity> saveFavoriteEntities = favoriteRepository.saveAll(List.of(fe1, fe2));

        userFavoriteRepository.save(
                UserFavorite.createNewUserFavorite(saveUser, saveFavoriteEntities.get(0)));
        userFavoriteRepository.save(UserFavorite.createNewUserFavorite(saveUser, saveFavoriteEntities.get(1)));

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/users"));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(SuccessCode.FIND_USER_INFO_SUCCESS.getIsSuccess()))
                .andExpect(jsonPath("$.message").value(SuccessCode.FIND_USER_INFO_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.code").value(SuccessCode.FIND_USER_INFO_SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.nickname").value(nickname))
                .andExpect(jsonPath("$.data.type").value(UserType.NORMAL.getValue()))
                .andExpect(jsonPath("$.data.role").value(UserRole.ROLE_USER.getValue()))
                .andExpect(jsonPath("$.data.createdAt").value(Matchers.matchesPattern("\\d{4}-\\d{2}-\\d{2}")))
                .andExpect(jsonPath("$.data.favorite").isArray())
                .andExpect(jsonPath("$.data.favorite.length()").value(2))
                .andExpect(jsonPath("$.data.favorite").value(Matchers.containsInAnyOrder("관심사1", "관심사2")));
    }

    @WithCustomUser
    @DisplayName("회원의 정보를 수정 합니다. 관심사와 닉네임을 수정할 수 있습니다.")
    @Test
    void modifyUserInfo() throws Exception {
        //test
        List<User> all = userRepository.findAll();

        // given
        String loginId = "test1@email.com";
        String password = "!test1234";
        String nickname = "test";
        User newUser = User.createNewUser(loginId, password, nickname);
        User saveUser = userRepository.save(newUser);

        List<String> favorite = List.of("관심사1", "관심사2");
        FavoriteEntity fe1 = FavoriteEntity.createNewFavorite("관심사1");
        FavoriteEntity fe2 = FavoriteEntity.createNewFavorite("관심사2");
        List<FavoriteEntity> saveFavoriteEntities = favoriteRepository.saveAll(List.of(fe1, fe2));

        userFavoriteRepository.save(UserFavorite.createNewUserFavorite(saveUser, saveFavoriteEntities.get(0)));

        String changeNickname = "변경된닉네임";
        UserRequest.PatchUserInfo request = UserRequest.PatchUserInfo.of(changeNickname, favorite);

        // when
        ResultActions result = mockMvc.perform(patch("/api/v1/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(SuccessCode.PATCH_USER_INFO_SUCCESS.getIsSuccess()))
                .andExpect(jsonPath("$.message").value(SuccessCode.PATCH_USER_INFO_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.code").value(SuccessCode.PATCH_USER_INFO_SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.nickname").doesNotExist());
    }
}