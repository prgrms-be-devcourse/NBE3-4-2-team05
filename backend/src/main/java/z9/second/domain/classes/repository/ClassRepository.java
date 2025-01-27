package z9.second.domain.classes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import z9.second.domain.classes.entity.ClassEntity;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
}
