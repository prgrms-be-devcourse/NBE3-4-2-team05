package z9.second.domain.checkin.service;

import z9.second.domain.checkin.dto.CheckInRequestDto;
import z9.second.domain.checkin.dto.CheckInResponseDto;

import java.util.List;


public interface CheckInService {
    void createCheckIn(Long userId, CheckInRequestDto.CheckInDto requestDto);
    void updateCheckIn(Long userId, CheckInRequestDto.CheckInDto requestDto);
    List<CheckInResponseDto.ResponseData> getAllCheckIns(Long scheduleId);
}