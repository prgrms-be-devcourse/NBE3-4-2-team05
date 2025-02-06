package z9.second.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import z9.second.domain.user.dto.UserRequest;
import z9.second.domain.user.dto.UserResponse;
import z9.second.domain.user.service.UserService;
import z9.second.global.response.BaseResponse;
import z9.second.global.response.SuccessCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller", description = "회원 관련 기능")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "회원 정보 조회")
    @SecurityRequirement(name = "bearerAuth")
    public BaseResponse<UserResponse.UserInfo> findUserInfo(
            Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        UserResponse.UserInfo findData = userService.findUserInfo(userId);
        return BaseResponse.ok(SuccessCode.FIND_USER_INFO_SUCCESS, findData);
    }

    @PatchMapping("/profile")
    @Operation(summary = "회원 정보 수정")
    @SecurityRequirement(name = "bearerAuth")
    public BaseResponse<Void> modifyUserInfo(
            @Valid @RequestBody UserRequest.PatchUserInfo requestDto,
            Principal principal) {
        userService.patchUserInfo(requestDto, Long.parseLong(principal.getName()));
        return BaseResponse.ok(SuccessCode.PATCH_USER_INFO_SUCCESS);
    }
}
