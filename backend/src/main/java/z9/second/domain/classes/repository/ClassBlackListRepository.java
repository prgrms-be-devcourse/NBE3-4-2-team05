package z9.second.domain.classes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import z9.second.domain.classes.entity.ClassBlackListEntity;

public interface ClassBlackListRepository extends JpaRepository<ClassBlackListEntity, Long> {
}
