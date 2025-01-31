package z9.second.domain.schedules.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import z9.second.domain.schedules.dto.SchedulesRequestDto;
import z9.second.domain.schedules.service.SchedulesService;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class SchedulesController {
    private final SchedulesService schedulesService;

    @PostMapping("/classes")
    public ResponseEntity<Void> createSchedules(@RequestBody SchedulesRequestDto schedulesRequestDto) {
        //schedulesService.create(schedulesRequestDto);
        return ResponseEntity.ok().build();
    }
}
