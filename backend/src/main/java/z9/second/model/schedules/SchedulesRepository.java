package z9.second.model.schedules;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SchedulesRepository extends JpaRepository<SchedulesEntity, Long> {
    // 클래스에 속한 모든 일정 조회
    List<SchedulesEntity> findSchedulesByClassesId(Long classId);

    // 특정 클래스의 특정 일정 조회, 일정 수정
    Optional<SchedulesEntity> findScheduleByIdAndClassesId(Long scheduleId, Long classId);

    @Query("SELECT s FROM SchedulesEntity s " +
            "JOIN FETCH s.checkins sc " +
            "WHERE sc.userId = :userId AND sc.checkIn = true " +
            "ORDER BY s.meetingTime DESC")
    List<SchedulesEntity> findUserSchedulesInfoByUserId(@Param("userId") Long userId);
}
