package z9.second.domain.checkin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import z9.second.domain.checkin.dto.CheckInRequestDto;
import z9.second.domain.checkin.service.CheckInService;
import z9.second.global.response.BaseResponse;
import z9.second.global.response.SuccessCode;

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
            @Valid @RequestBody CheckInRequestDto.CreateCheckIn requestDto
    ) {
        Long userId = Long.parseLong(principal.getName());
        checkInService.createCheckIn(userId, requestDto);

        return BaseResponse.ok(SuccessCode.CHECK_IN_CREATE_SUCCESS);
    }
}
