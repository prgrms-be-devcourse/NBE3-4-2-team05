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

import java.util.ArrayList;
import java.util.List;

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
            // 새로운 일정 엔티티 생성
            SchedulesEntity schedules = SchedulesEntity.builder()
                    .classes(classes)                          // 찾은 클래스 정보
                    .meetingTime(requestData.getMeetingTime()) // 모임 시간
                    .meetingTitle(requestData.getMeetingTitle()) // 모임 제목
                    .build();

            // DB에 저장
            SchedulesEntity savedSchedule = schedulesRepository.save(schedules);

            // 저장된 결과를 ResponseDto로 변환해서 반환
            return SchedulesResponseDto.ResponseData.from(savedSchedule);
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

        // 해당 클래스의 모든 일정 조회
        List<SchedulesEntity> schedules = schedulesRepository.findSchedulesByClassesId(classId);

        // ResponseDto로 변환하여 반환
        List<SchedulesResponseDto.ResponseData> result = new ArrayList<>();
        for (SchedulesEntity schedule : schedules) {
            result.add(SchedulesResponseDto.ResponseData.from(schedule));
        }
        return result;
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
