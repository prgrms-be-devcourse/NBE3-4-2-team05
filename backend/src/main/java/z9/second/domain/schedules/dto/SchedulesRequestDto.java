package z9.second.domain.schedules.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SchedulesRequestDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestData {
        @NotNull(message = "Class ID must not be null")
        private Long classId; // 일정을 생성할 클래스의 ID

        @NotNull(message = "meeting_time must not be null")
        private String meetingTime;

        @NotNull(message = "meeting_title must not be null")
        @Size(min = 2, message = "모임 제목은 2글자 이상이어야 합니다.")
        private String meetingTitle;
    }
}
