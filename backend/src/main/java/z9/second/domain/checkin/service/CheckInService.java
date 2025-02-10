package z9.second.domain.checkin.service;

import z9.second.domain.checkin.dto.CheckInRequestDto;
import z9.second.domain.checkin.dto.CheckInResponseDto;

import java.util.List;
import java.util.Optional;


public interface CheckInService {
    void createCheckIn(Long userId, CheckInRequestDto.CheckInDto requestDto);
    void updateCheckIn(Long userId, CheckInRequestDto.CheckInDto requestDto);
    List<CheckInResponseDto.ResponseData> getAllCheckIns(Long scheduleId);
    Optional<CheckInResponseDto.ResponseData> getMyCheckIn( Long userId,Long checkInId);
}