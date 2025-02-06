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
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class SchedulesController {
    private final SchedulesService schedulesService;

    @PostMapping("/classes")
    @Operation(
            summary = "모임 일정 생성",
            description = "새로운 모임 일정을 생성합니다. 모임장만 생성 가능합니다."
    )
    public BaseResponse<SchedulesResponseDto.ResponseData> create(
            @RequestBody @Valid SchedulesRequestDto.RequestData requestData,
            Principal principal
    ) {
        // 모임장 권한 체크를 Service에서 처리하도록 userId 전달
        SchedulesResponseDto.ResponseData response = schedulesService.create(requestData, extractUserId(principal));
        return BaseResponse.ok(SuccessCode.SCHEDULE_CREATE_SUCCESS, response);
    }

    @PutMapping("/{scheduleId}/classes/{classId}")
    @Operation(
            summary = "모임 일정 수정",
            description = "{classId}모임의 {scheduleId}번 일정을 수정합니다."
    )
    public BaseResponse<SchedulesResponseDto.ResponseData> modify(
            @PathVariable Long scheduleId,
            @PathVariable Long classId,
            @RequestBody @Valid SchedulesRequestDto.RequestData requestData,
            Principal principal
    ) {
        SchedulesResponseDto.ResponseData response =
                schedulesService.modify(scheduleId, classId, requestData, extractUserId(principal));
        return BaseResponse.ok(SuccessCode.SCHEDULE_MODIFY_SUCCESS, response);
    }

    @GetMapping("/classes/{classId}")
    @Operation(summary = "모임 전체 일정 조회")
    public BaseResponse<List<SchedulesResponseDto.ResponseData>> getSchedulesList(
            @Parameter(description = "모임 ID", required = true)
            @PathVariable Long classId,
            Principal principal
    ) {
        List<SchedulesResponseDto.ResponseData> schedules = schedulesService.getSchedulesList(classId, extractUserId(principal));
        return BaseResponse.ok(SuccessCode.SCHEDULE_READ_SUCCESS, schedules);
    }

    @GetMapping("/{scheduleId}/classes/{classId}")
    @Operation(summary = "모임 일제 상세 조회")
    public BaseResponse<SchedulesResponseDto.ResponseData> getScheduleDetail(
            @Parameter(description = "일정 ID", required = true)
            @PathVariable Long scheduleId,
            @Parameter(description = "모임 ID", required = true)
            @PathVariable Long classId,
            Principal principal
    ) {
        SchedulesResponseDto.ResponseData schedule = schedulesService.getScheduleDetail(scheduleId,classId, extractUserId(principal));
        return BaseResponse.ok(SuccessCode.SCHEDULE_READ_SUCCESS, schedule);
    }

    // userId 추출 공통 메소드
    private Long extractUserId(Principal principal) {
        return Long.parseLong(principal.getName());
    }
}