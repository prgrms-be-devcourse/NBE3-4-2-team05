package z9.second.domain.classes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import z9.second.domain.classes.entity.ClassBlackListEntity;

import java.util.Optional;

public interface ClassBlackListRepository extends JpaRepository<ClassBlackListEntity, Long> {
    boolean existsByClasses_IdAndUserId(Long classId, Long currentUserId);

    Optional<ClassBlackListEntity> findByClassesIdAndUserId(Long classId, Long userId);
}
