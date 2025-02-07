package z9.second.model.checkIn;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckInEntityRepository extends JpaRepository<CheckInEntity, Long> {
    boolean existsByUserIdAndSchedulesId(Long userId, Long scheduleId);
    List<CheckInEntity> findBySchedulesId(Long scheduleId);
}
