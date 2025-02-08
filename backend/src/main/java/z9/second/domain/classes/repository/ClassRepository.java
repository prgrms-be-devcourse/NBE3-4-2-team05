package z9.second.domain.classes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import z9.second.domain.classes.entity.ClassEntity;

import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    Optional<ClassEntity> findFirstByOrderByIdDesc();

    List<ClassEntity> findByMasterId(Long userId);

    boolean existsByMasterId(Long userId);
}
