package z9.second.domain.checkin.service;

import z9.second.domain.checkin.dto.CheckInRequestDto;
import z9.second.domain.checkin.dto.CheckInResponseDto;

import java.util.List;

public interface CheckInService {
    void CreateCheckIn(Long userId, CheckInRequestDto.CheckInDto requestDto);
    void UpdateCheckIn(Long userId, CheckInRequestDto.CheckInDto requestDto);
    List<CheckInResponseDto.ResponseData> GetAllCheckIns(Long scheduleId);
}
