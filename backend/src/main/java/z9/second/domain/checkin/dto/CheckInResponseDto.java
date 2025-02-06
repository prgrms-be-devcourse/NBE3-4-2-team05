package z9.second.domain.checkin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import z9.second.model.schedules.SchedulesCheckInEntity;

public class CheckInResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ResponseData {
        private final Long checkInId;
        private final Long scheduleId;
        private final Long userId;
        private final Boolean checkIn;

        public static ResponseData from(SchedulesCheckInEntity schedulesCheckInEntity) {
            return ResponseData.builder()
                    .checkInId(schedulesCheckInEntity.getId())
                    .scheduleId(schedulesCheckInEntity.getSchedules().getId())
                    .userId(schedulesCheckInEntity.getUserId())
                    .checkIn(schedulesCheckInEntity.isCheckIn())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ListResponseData {   // 참석자 목록 조회를 위한 응답 DTO
        private final Long userId;
        private final Boolean checkIn;

        public static ListResponseData from(SchedulesCheckInEntity entity) {
            return ListResponseData.builder()
                    .userId(entity.getUserId())
                    .checkIn(entity.isCheckIn())
                    .build();
        }
    }
}
