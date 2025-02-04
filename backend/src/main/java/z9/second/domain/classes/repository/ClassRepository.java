package z9.second.domain.classes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import z9.second.domain.classes.entity.ClassEntity;

import java.util.List;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findByMasterId(Long userId);
}
