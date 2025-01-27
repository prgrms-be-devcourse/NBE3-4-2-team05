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
public class ClassBlackListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cbl_id", nullable = false)
    private Long cblId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "class_id", nullable = false)
    private ClassEntity classId;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
