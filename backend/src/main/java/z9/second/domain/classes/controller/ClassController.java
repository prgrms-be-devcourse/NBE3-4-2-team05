package z9.second.domain.classes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import z9.second.domain.classes.dto.ClassRequest;
import z9.second.domain.classes.dto.ClassResponse;
import z9.second.domain.classes.service.ClassService;
import z9.second.global.response.BaseResponse;
import z9.second.global.response.SuccessCode;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/classes")
@Tag(name = "Class Controller", description = "모임 컨트롤러")
public class ClassController {
    private final ClassService classService;

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "모임 생성")
    public BaseResponse<ClassResponse.ClassResponseData> create(
            @RequestBody @Valid ClassRequest.ClassRequestData requestData,
            Principal principal
    ) {
        Long userId = Long.parseLong(principal.getName());

        ClassResponse.ClassResponseData responseData = classService.save(requestData, userId);

        return BaseResponse.ok(SuccessCode.CLASS_CREATE_SUCCESS, responseData);
    }

    @PostMapping("/{classId}/membership")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "모임 가입")
    public BaseResponse<ClassResponse.JoinResponseData> membership(
            @PathVariable Long classId,
            Principal principal
    ) {
        Long userId = Long.parseLong(principal.getName());

        ClassResponse.JoinResponseData responseData = classService.joinMembership(classId, userId);

        return BaseResponse.ok(SuccessCode.CLASS_JOIN_SUCCESS, responseData);
    }

    @GetMapping("/{classId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "모임방 입장")
    public BaseResponse<ClassResponse.EntryResponseData> entry(
            @PathVariable("classId") Long classId
    ){
        ClassResponse.EntryResponseData responseData = classService.getClassInfo(classId);
        return BaseResponse.ok(SuccessCode.SUCCESS, responseData);
    }
}