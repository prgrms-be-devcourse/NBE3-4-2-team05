package z9.second.domain.classes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import z9.second.domain.classes.dto.ClassRequest;
import z9.second.domain.classes.dto.ClassResponse;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.classes.repository.ClassRepository;
import z9.second.domain.classes.repository.ClassUserRepository;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;

@Service
@RequiredArgsConstructor
public class ClassService {
    private final ClassRepository classRepository;
    private final ClassUserRepository classUserRepository;

    @Transactional
    public ClassResponse.ClassResponseData save(ClassRequest.ClassRequestData requestData, Long userId) {
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

        return ClassResponse.ClassResponseData.from(classEntity);
    }

    @Transactional
    public ClassResponse.EntryResponseData getClassInfo(Long classId) {
        ClassEntity classEntity = classRepository.findById(classId)
              .orElseThrow(() -> new CustomException(ErrorCode.CLASS_NOT_FOUND));

        return ClassResponse.EntryResponseData.from(classEntity);
    }

    @Transactional
    public ClassResponse.JoinResponseData joinMembership(Long classId, Long userId) {
        // 해당 모임이 존재하는지 확인
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLASS_NOT_FOUND));

        // 이미 가입된 회원인지 검증
        if (classUserRepository.existsByClasses_IdAndUserId(classId, userId)) {
            throw new CustomException(ErrorCode.CLASS_EXISTS_MEMBER);
        }

        classEntity.addMember(userId);

        return ClassResponse.JoinResponseData.from(classEntity);
    }
}
