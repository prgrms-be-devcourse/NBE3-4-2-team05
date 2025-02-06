package z9.second.domain.checkin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.checkin.dto.CheckInRequestDto;
import z9.second.domain.classes.repository.ClassUserRepository;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;
import z9.second.model.checkIn.CheckInEntity;
import z9.second.model.checkIn.CheckInEntityRepository;
import z9.second.model.schedules.SchedulesEntity;
import z9.second.model.schedules.SchedulesRepository;

@Service
@RequiredArgsConstructor
public class CheckInServiceImpl implements CheckInService {

    private final CheckInEntityRepository checkInEntityRepository;
    private final SchedulesRepository schedulesRepository;
    private final ClassUserRepository classUserRepository;

    @Transactional
    @Override
    public void CheckIn(Long userId, CheckInRequestDto.CheckInDto requestDto) {
        SchedulesEntity findSchedulesEntity = schedulesRepository.findById(requestDto.getScheduleId())
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));
        if(!classUserRepository.existsByClassesAndUserId(findSchedulesEntity.getClasses(), userId)) {
            throw new CustomException(ErrorCode.CLASS_NOT_EXISTS_MEMBER);
        }

        // 모임에 가입된 회원만이, 모임 참석 유무를 결정할 수 있다.
        CheckInEntity newSchedulesCheckIn = CheckInEntity
                .builder()
                .schedules(findSchedulesEntity)
                .userId(userId)
                .checkIn(requestDto.getCheckIn())
                .build();

        checkInEntityRepository.save(newSchedulesCheckIn);
    }
}
