package z9.second.domain.classes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cu_id", nullable = false)
    private Long cuId;

    @Column(name = "class_id", nullable = false)
    private Long classId;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
