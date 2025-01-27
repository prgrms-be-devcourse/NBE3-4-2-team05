package z9.second.domain.schedules.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import z9.second.model.schedules.SchedulesEntity;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchedulesRequestDto {
    @NotNull(message = "Class ID must not be null")
    private Long classId;

    @NotNull(message = "meeting_time must not be null")
    private String meetingTime;

    @NotNull(message = "meeting_title must not be null")
    private String meetingTitle;

    public static SchedulesRequestDto of(SchedulesEntity schedules) {
        return  SchedulesRequestDto.builder()
                .classId(schedules.getId())
                .meetingTime(schedules.getMeetingTime())
                .meetingTitle(schedules.getMeetingTitle())
                .build();
    }
}
