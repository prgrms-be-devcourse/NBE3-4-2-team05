package z9.second.domain.schedules.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import z9.second.domain.schedules.dto.SchedulesRequestDto;
import z9.second.domain.schedules.dto.SchedulesResponseDto;
import z9.second.domain.schedules.service.SchedulesService;
import z9.second.global.response.BaseResponse;
import z9.second.global.response.SuccessCode;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@Tag(name = "Schedules Controller", description = "모임 일정 컨트롤러")
@RequiredArgsConstructor
public class SchedulesController {
    private final SchedulesService schedulesService;

    @PostMapping("/classes")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "모임 일정 생성")
    public BaseResponse<SchedulesResponseDto.ResponseData> create(
            @RequestBody @Valid SchedulesRequestDto.RequestData requestData,
            Principal principal
    ) {
        Long userId = Long.parseLong(principal.getName());
        // 모임장 권한 체크를 Service에서 처리하도록 userId 전달
        SchedulesResponseDto.ResponseData response = schedulesService.create(requestData, userId);
        return BaseResponse.ok(SuccessCode.SCHEDULE_CREATE_SUCCESS, response);
    }

    @GetMapping("/classes/{classId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "모임 전체 일정 조회")
    public BaseResponse<List<SchedulesResponseDto.ResponseData>> getSchedulesList(
            @Parameter(description = "모임 ID", required = true)
            @PathVariable Long classId,
            Principal principal
    ) {
        Long userId = Long.parseLong(principal.getName());
        List<SchedulesResponseDto.ResponseData> schedules = schedulesService.getSchedulesList(classId, userId);
        return BaseResponse.ok(SuccessCode.SCHEDULE_READ_SUCCESS, schedules);
    }

    @GetMapping("/{scheduleId}/classes/{classId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "모임 일제 상세 조회")
    public BaseResponse<SchedulesResponseDto.ResponseData> getScheduleDetail(
            @Parameter(description = "일정 ID", required = true)
            @PathVariable Long scheduleId,
            @Parameter(description = "모임 ID", required = true)
            @PathVariable Long classId,
            Principal principal
    ) {
        Long userId = Long.parseLong(principal.getName());
        SchedulesResponseDto.ResponseData schedule = schedulesService.getScheduleDetail(scheduleId,classId,userId);
        return BaseResponse.ok(SuccessCode.SCHEDULE_READ_SUCCESS, schedule);
    }
}
