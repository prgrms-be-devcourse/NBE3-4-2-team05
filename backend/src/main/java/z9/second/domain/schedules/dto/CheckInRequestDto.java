package z9.second.domain.schedules.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CheckInRequestDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestData {
        @NotNull(message = "Schedule ID must not be null")
        private Long scheduleId;

        @NotNull(message = "User ID must not be null")
        private Long userId;

        @NotNull(message = "Check-in status must not be null")
        private Boolean checkIn;
    }
}
