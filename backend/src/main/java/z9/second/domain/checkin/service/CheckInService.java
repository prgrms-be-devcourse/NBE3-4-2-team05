package z9.second.domain.checkin.service;

import z9.second.domain.checkin.dto.CheckInRequestDto;

public interface CheckInService {
    void CreateCheckIn(Long userId, CheckInRequestDto.CheckInDto requestDto);
    void UpdateCheckIn(Long userId, CheckInRequestDto.CheckInDto requestDto);
}
