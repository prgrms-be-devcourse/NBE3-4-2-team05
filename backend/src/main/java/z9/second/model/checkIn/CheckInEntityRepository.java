package z9.second.model.checkIn;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInEntityRepository extends JpaRepository<CheckInEntity, Long> {
    boolean existsByUserIdAndSchedulesId(Long userId, Long scheduleId);
}
