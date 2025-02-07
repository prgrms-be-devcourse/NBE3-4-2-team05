package z9.second.domain.checkin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import z9.second.domain.checkin.dto.CheckInRequestDto;
import z9.second.domain.checkin.dto.CheckInResponseDto;
import z9.second.domain.checkin.service.CheckInService;
import z9.second.global.response.BaseResponse;
import z9.second.global.response.SuccessCode;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/checkin")
@Tag(name = "Checkin controller", description = "모임 참석 유무 컨트롤러")
public class CheckInController {
    private final CheckInService checkInService;

    @PostMapping
    @Operation(summary = "참석 여부 생성")
    @SecurityRequirement(name = "bearerAuth")
    public BaseResponse<Void> createCheckIn(
            Principal principal,
            @Valid @RequestBody CheckInRequestDto.CheckInDto requestDto
    ) {
        Long userId = Long.parseLong(principal.getName());
        checkInService.CreateCheckIn(userId, requestDto);
        return BaseResponse.ok(SuccessCode.CHECK_IN_CREATE_SUCCESS);
    }

    @PutMapping
    @Operation(summary = "참석 여부 변경")
    @SecurityRequirement(name="bearerAuth")
    public BaseResponse<Void> updateCheckIn(
            Principal principal,
            @Valid @RequestBody CheckInRequestDto.CheckInDto requestDto
    ){
        Long userId = Long.parseLong(principal.getName());
        checkInService.UpdateCheckIn(userId, requestDto);
        return BaseResponse.ok(SuccessCode.CHECK_IN_UPDATE_SUCCESS);
    }

    // 스케줄 ID로 체크인 정보 조회
    @GetMapping("/schedules/{scheduleId}")
    @Operation(summary = "모임 내 투표 현황 보여주기")
    @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<List<CheckInResponseDto.ResponseData>> getAllCheckIns(@PathVariable Long scheduleId) {
        List<CheckInResponseDto.ResponseData> checkIns = checkInService.GetAllCheckIns(scheduleId);
        return ResponseEntity.ok(checkIns);
    }

}
