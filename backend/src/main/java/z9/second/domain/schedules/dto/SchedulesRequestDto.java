package z9.second.domain.schedules.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    public static class CreateRequest {
        @NotNull(message = "Class ID must not be null")
        private Long classId; // 모임의 ID

        @NotNull(message = "meeting_time must not be null")
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                message = "날짜 형식은 yyyy-MM-dd 이어야 합니다."
        )
        private String meetingTime;

        @NotNull(message = "meeting_title must not be null")
        @Size(min = 2, max = 100, message = "모임 제목은 2글자 이상 100자 이하이어야 합니다.")
        @Pattern(
                regexp = "^[\\p{L}\\p{N}\\s,.!?()-]+$",
                message = "모임 제목에는 문자, 숫자, 기본 특수문자만 사용할 수 있습니다"
        )
        private String meetingTitle;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateRequest {
        // classId 제거 - PathVariable로 받기 때문
        @NotNull(message = "meeting_time must not be null")
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                message = "날짜 형식은 yyyy-MM-dd 이어야 합니다."
        )
        private String meetingTime;

        @NotNull(message = "meeting_title must not be null")
        @Size(min = 2, max = 100, message = "모임 제목은 2글자 이상 100자 이하이어야 합니다.")
        @Pattern(
                regexp = "^[\\p{L}\\p{N}\\s,.!?()-]+$",
                message = "모임 제목에는 문자, 숫자, 기본 특수문자만 사용할 수 있습니다"
        )
        private String meetingTitle;
    }
}
