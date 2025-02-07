package z9.second.model.checkin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import z9.second.model.BaseEntity;
import z9.second.model.schedules.SchedulesEntity;
@Entity
@Table(name = "schedules_checkin") // 테이블 이름 확인
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sc_id", nullable = false) // 컬럼 이름 확인
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedules_id") // 컬럼 이름 확인
    private SchedulesEntity schedules;

    @Column(name = "user_id", nullable = false) // 컬럼 이름 확인
    private Long userId;

    @Column(name = "check_in", nullable = false) // 컬럼 이름 확인
    private boolean checkIn;
}
