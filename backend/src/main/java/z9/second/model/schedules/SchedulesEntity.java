package z9.second.model.schedules;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import z9.second.model.BaseEntity;
import z9.second.model.classes.ClassesEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulesEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedules_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassesEntity classes;

    @Column(name = "meeting_time", nullable = false)
    private String meetingTime;

    @Column(name = "meeting_title", nullable = false)
    private String meetingTitle;

    @OneToMany(mappedBy = "schedules",cascade = CascadeType.ALL)
    private List<SchedulesCheckInEntity> checkins = new ArrayList<>();
}