package z9.second.domain.checkin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import z9.second.model.checkIn.CheckInEntity;

public class CheckInResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ResponseData {
        private final Long checkInId;
        private final Long scheduleId;
        private final Long userId;
        private final Boolean checkIn;

        public static ResponseData from(CheckInEntity schedulesCheckInEntity) {
            return ResponseData.builder()
                    .checkInId(schedulesCheckInEntity.getId())
                    .scheduleId(schedulesCheckInEntity.getSchedules().getId())
                    .userId(schedulesCheckInEntity.getUserId())
                    .checkIn(schedulesCheckInEntity.isCheckIn())
                    .build();
        }
    }
}
