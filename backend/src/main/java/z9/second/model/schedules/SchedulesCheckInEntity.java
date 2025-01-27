package z9.second.model.schedules;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "schedules_checkin")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulesCheckInEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sc_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedules_id")
    private SchedulesEntity schedules;

    @Column(name = "user_id", nullable = false)
    private Long userId;  // Entity 관계 매핑 대신 단순 컬럼으로 저장

    @Column(name = "check_in", nullable = false)
    private boolean checkIn;
}