package z9.second.domain.schedules.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import z9.second.domain.schedules.dto.SchedulesRequestDto;
import z9.second.domain.schedules.service.SchedulesService;
import z9.second.global.response.BaseResponse;
import z9.second.global.response.SuccessCode;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/schedules")
@Tag(name = "Schedules Controller", description = "모임 일정 컨트롤러")
@RequiredArgsConstructor
public class SchedulesController {
    private final SchedulesService schedulesService;

    @PostMapping("/classes")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "모임 일정 생성")
    public BaseResponse<Void> create(
            @RequestBody @Valid SchedulesRequestDto.RequestData requestData,
            Principal principal
    ) {
        Long userId = Long.parseLong(principal.getName());
        // 모임장 권한 체크를 Service에서 처리하도록 userId 전달
        schedulesService.create(requestData, userId);
        return BaseResponse.ok(SuccessCode.SCHEDULE_CREATE_SUCCESS);
    }
}
