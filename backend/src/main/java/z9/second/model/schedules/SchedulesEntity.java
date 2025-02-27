package z9.second.model.schedules;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import z9.second.domain.classes.entity.ClassEntity;
import z9.second.model.BaseEntity;
import z9.second.model.checkIn.CheckInEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchedulesEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedules_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassEntity classes;

    @Column(name = "meeting_time", nullable = false)
    private String meetingTime;

    @Column(name = "meeting_title", nullable = false)
    private String meetingTitle;

    @Builder.Default
    @OneToMany(mappedBy = "schedules", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CheckInEntity> checkins = new ArrayList<>();

    public void updateSchedule(String meetingTime, String meetingTitle) {
        this.meetingTime = meetingTime;
        this.meetingTitle = meetingTitle;
    }
}
