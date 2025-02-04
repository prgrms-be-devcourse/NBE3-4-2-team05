package z9.second.domain.classes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import z9.second.model.schedules.SchedulesEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "favorite", nullable = false)
    private String favorite;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "master_id", nullable = false)
    private Long masterId;

    @OneToMany(mappedBy = "classes")
    private List<ClassUserEntity> users = new ArrayList<>();

    @OneToMany(mappedBy = "classes")
    private List<ClassBlackListEntity> blackLists = new ArrayList<>();
}
