package z9.second.domain.classes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import z9.second.domain.classes.entity.ClassEntity;

import java.util.List;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findByMasterId(Long userId);

    boolean existsByMasterId(Long userId);

    @Query("SELECT c FROM ClassEntity c JOIN ClassUserEntity cu ON c.id = cu.classes.id WHERE cu.userId = :userId")
    List<ClassEntity> findByUserId(@Param("userId") Long userId);
}
