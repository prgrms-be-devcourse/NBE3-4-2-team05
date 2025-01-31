package z9.second.domain.schedules.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import z9.second.domain.schedules.dto.SchedulesRequestDto;
import z9.second.domain.schedules.dto.SchedulesResponseDto;
import z9.second.domain.schedules.service.SchedulesService;

@RestController
@RequestMapping("/api/v1/schedules")
@Tag(name = "Schedules Controller", description = "모임 일정 컨트롤러")
@RequiredArgsConstructor
public class SchedulesController {
    private final SchedulesService schedulesService;

    @PostMapping("/classes")
    @Operation(summary = "Schedule 등록")
    public ResponseEntity<SchedulesResponseDto.ResponseData> createSchedules(@RequestBody @Valid SchedulesRequestDto.RequestData request) {
        schedulesService.create(request);
        return ResponseEntity.ok().build();
    }
}
