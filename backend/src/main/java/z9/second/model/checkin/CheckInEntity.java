package z9.second.model.checkin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import z9.second.model.BaseEntity;
import z9.second.model.schedules.SchedulesEntity;

@Entity
@Table(name = "schedules_checkin")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sc_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedules_id")
    private SchedulesEntity schedules;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "check_in", nullable = false)
    private boolean checkIn;
}
