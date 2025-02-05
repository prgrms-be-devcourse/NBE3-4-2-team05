package z9.second.domain.classes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import z9.second.domain.classes.dto.ClassRequest;
import z9.second.domain.classes.dto.ClassResponse;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.domain.classes.entity.ClassUserEntity;
import z9.second.domain.classes.repository.ClassRepository;
import z9.second.domain.classes.repository.ClassUserRepository;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;

import java.util.List;

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

        newClass.addMember(userId);

        ClassEntity classEntity = classRepository.save(newClass);

        return ClassResponse.ClassResponseData.from(classEntity);
    }

    @Transactional
    public ClassResponse.EntryResponseData getClassInfo(Long classId, Long userId) {
        // 1. 모임 존재 여부 확인
        ClassEntity classEntity = classRepository.findById(classId)
              .orElseThrow(() -> new CustomException(ErrorCode.CLASS_NOT_FOUND));

        // 2. 유저가 모임 멤버인지 확인
        if(!classUserRepository.existsByUserIdAndClassesId(userId, classId)) {
            throw new CustomException(ErrorCode.CLASS_ACCESS_DENIED);
        }
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

    @Transactional
    public void deleteMembership(Long classId, Long userId) {
        // 해당 모임이 존재하는지 확인
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLASS_NOT_FOUND));

        // 가입된 회원인지 검증
        ClassUserEntity user = classUserRepository.findByClassesIdAndUserId(classId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLASS_NOT_EXISTS_MEMBER));

        if (userId.equals(classEntity.getMasterId())) {
            throw new CustomException(ErrorCode.CLASS_MASTER_TRANSFER_REQUIRED);
        }

        classEntity.removeMember(user);
    }
}
