package z9.second.domain.schedules.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.classes.repository.ClassRepository;
import z9.second.domain.schedules.dto.SchedulesRequestDto;
import z9.second.domain.schedules.dto.SchedulesResponseDto;
import z9.second.model.schedules.SchedulesEntity;
import z9.second.model.schedules.SchedulesRepository;

@Service
@RequiredArgsConstructor
public class SchedulesService {
    private final SchedulesRepository schedulesRepository;
    private final ClassRepository classesRepository;

    //생성
    public SchedulesResponseDto.ResponseData create(SchedulesRequestDto.RequestData requestData) {
        // 1. 요청된 classId로 실제 클래스가 있는지 확인
        ClassEntity classes = classesRepository.findById(requestData.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));

        // 2. 새로운 일정 엔티티 생성
        SchedulesEntity schedules = SchedulesEntity.builder()
                .classes(classes)                          // 찾은 클래스 정보
                .meetingTime(requestData.getMeetingTime()) // 모임 시간
                .meetingTitle(requestData.getMeetingTitle()) // 모임 제목
                .build();

        // 3. DB에 저장하고
        SchedulesEntity savedSchedule = schedulesRepository.save(schedules);

        // 4. 저장된 결과를 ResponseDto로 변환해서 반환
        return SchedulesResponseDto.ResponseData.from(savedSchedule);
    }
}
