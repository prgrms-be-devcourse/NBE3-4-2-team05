package z9.second.domain.checkin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.checkin.dto.CheckInRequestDto;
import z9.second.domain.checkin.dto.CheckInResponseDto;
import z9.second.domain.classes.dto.ClassResponse;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.classes.repository.ClassRepository;
import z9.second.domain.classes.repository.ClassUserRepository;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;
import z9.second.model.checkIn.CheckInEntity;
import z9.second.model.checkIn.CheckInEntityRepository;
import z9.second.model.schedules.SchedulesEntity;
import z9.second.model.schedules.SchedulesRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckInServiceImpl implements CheckInService {

    private final CheckInEntityRepository checkInEntityRepository;
    private final SchedulesRepository schedulesRepository;
    private final ClassUserRepository classUserRepository;

    // 처음 투표 시 생성
    @Transactional
    @Override
    public void CreateCheckIn(Long userId, CheckInRequestDto.CheckInDto requestDto) {
        if (checkInEntityRepository.existsByUserIdAndSchedulesId(userId, requestDto.getScheduleId())) {
            throw new CustomException(ErrorCode.CHECK_IN_ALREADY_EXISTS);
        }
        checkInProcess(userId, requestDto);
    }
    // 투표 결과 변경
    @Transactional
    @Override
    public void UpdateCheckIn(Long userId, CheckInRequestDto.CheckInDto requestDto) {
        checkInProcess(userId, requestDto);
    }

    // 공통 부분
    private void checkInProcess(Long userId, CheckInRequestDto.CheckInDto requestDto) {
        SchedulesEntity findSchedulesEntity = schedulesRepository.findById(requestDto.getScheduleId())
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (!classUserRepository.existsByClassesAndUserId(findSchedulesEntity.getClasses(), userId)) {
            throw new CustomException(ErrorCode.CLASS_NOT_EXISTS_MEMBER);
        }
        LocalDateTime meetingDateTime = LocalDateTime.parse(
                findSchedulesEntity.getMeetingTime(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );

        if (meetingDateTime.isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.INVALID_PASSED_CHECK_IN);
        }
        CheckInEntity newSchedulesCheckIn = CheckInEntity
                .builder()
                .schedules(findSchedulesEntity)
                .userId(userId)
                .checkIn(requestDto.getCheckIn())
                .build();
        findSchedulesEntity.getCheckins().add(newSchedulesCheckIn);

        checkInEntityRepository.save(newSchedulesCheckIn);
    }

    @Transactional
    @Override
    public List<CheckInResponseDto.ResponseData> GetAllCheckIns(Long scheduleId) {
        List<CheckInEntity> response = checkInEntityRepository.findBySchedulesId(scheduleId);
         return response.stream()
                .map(CheckInResponseDto.ResponseData::from)
                .collect(Collectors.toList());
        }


}
