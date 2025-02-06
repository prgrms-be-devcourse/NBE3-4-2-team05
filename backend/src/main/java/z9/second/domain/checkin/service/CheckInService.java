package z9.second.domain.checkin.service;

import z9.second.domain.checkin.dto.CheckInRequestDto;

public interface CheckInService {

    void createCheckIn(Long userId, CheckInRequestDto.CreateCheckIn requestDto);
}
