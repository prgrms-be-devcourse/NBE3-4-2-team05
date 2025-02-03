package z9.second.domain.classes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.classes.dto.ClassRequest;
import z9.second.domain.classes.dto.ClassResponse;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.classes.repository.ClassRepository;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassService {
    private final ClassRepository classRepository;

    @Transactional
    public ClassResponse.ResponseData save(ClassRequest.RequestData requestData, Long userId) {
        // 회원 당 모임 생성 개수 제한 : 3개
        List<ClassEntity> existingClasses = classRepository.findByMasterId(userId);
        if (existingClasses.size() >= 3) {
            throw new CustomException(ErrorCode.CLASS_CREATE_LIMIT_EXCEEDED);
        }

        ClassEntity newClass = ClassEntity.builder()
                .name(requestData.getName())
                .favorite(requestData.getFavorite())
                .description(requestData.getDescription())
                .masterId(userId)
                .build();

        ClassEntity classEntity = classRepository.save(newClass);

        return ClassResponse.ResponseData.from(classEntity);
    }
}
