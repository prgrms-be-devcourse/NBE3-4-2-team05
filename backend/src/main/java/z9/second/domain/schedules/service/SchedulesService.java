package z9.second.domain.schedules.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.classes.repository.ClassRepository;
import z9.second.domain.schedules.dto.SchedulesRequestDto;
import z9.second.domain.schedules.dto.SchedulesResponseDto;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;
import z9.second.model.schedules.SchedulesEntity;
import z9.second.model.schedules.SchedulesRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulesService {
    private final SchedulesRepository schedulesRepository;
    private final ClassRepository classesRepository;

    //생성
    @Transactional
    public SchedulesResponseDto.ResponseData create(SchedulesRequestDto.RequestData requestData, Long userId) {
        ClassEntity classes = classesRepository.findById(requestData.getClassId())
                .orElseThrow(() -> new CustomException(ErrorCode.CLASS_NOT_FOUND));

        // 모임장 권한 체크
        if (!classes.getMasterId().equals(userId)) {
            throw new CustomException(ErrorCode.CLASS_ACCESS_DENIED);
        }

        try {
            // 날짜 형식 검증 및 미래 날짜 검증
            LocalDateTime meetingDateTime = LocalDateTime.parse(
                    requestData.getMeetingTime(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );

            // 현재 시간과 비교하여 과거인지 확인
            if (meetingDateTime.isBefore(LocalDateTime.now())) {
                throw new CustomException(ErrorCode.INVALID_MEETING_TIME);
            }

            // 새로운 일정 엔티티 생성
            SchedulesEntity schedules = SchedulesEntity.builder()
                    .classes(classes)
                    .meetingTime(requestData.getMeetingTime())
                    .meetingTitle(requestData.getMeetingTitle())
                    .build();

            // DB에 저장
            SchedulesEntity savedSchedule = schedulesRepository.save(schedules);

            // 저장된 결과를 ResponseDto로 변환해서 반환
            return SchedulesResponseDto.ResponseData.from(savedSchedule);
        } catch (DateTimeParseException e) {
            throw new CustomException(ErrorCode.INVALID_MEETING_TIME_FORMAT);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.SCHEDULE_CREATE_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public List<SchedulesResponseDto.ResponseData> getSchedulesList(Long classId, Long userId) {
        ClassEntity classes = classesRepository.findById(classId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLASS_NOT_FOUND));

        if (!classes.getMasterId().equals(userId)) {
            throw new CustomException(ErrorCode.CLASS_ACCESS_DENIED);
        }

        // ResponseDto로 변환하여 반환
        return schedulesRepository.findSchedulesByClassesId(classId).stream()
                .map(SchedulesResponseDto.ResponseData::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SchedulesResponseDto.ResponseData getScheduleDetail(Long scheduleId, Long classId, Long userId) {
        ClassEntity classes = classesRepository.findById(classId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLASS_NOT_FOUND));

        if (!classes.getMasterId().equals(userId)) {
            throw new CustomException(ErrorCode.CLASS_ACCESS_DENIED);
        }

        // 특정 일정 조회
        SchedulesEntity schedule = schedulesRepository.findScheduleByIdAndClassesId(scheduleId, classId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        // ResponseDto로 변환하여 반환
        return SchedulesResponseDto.ResponseData.from(schedule);
    }
}
