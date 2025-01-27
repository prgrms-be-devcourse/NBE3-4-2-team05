package z9.second.model.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import z9.second.model.BaseEntity;
import z9.second.model.schedules.SchedulesEntity;

import java.util.List;

@Entity
@Table(name = "classes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClassesEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classes_id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "classes") // 양방향 매핑 설정
    private List<SchedulesEntity> schedules;
}
