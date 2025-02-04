package z9.second.domain.classes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import z9.second.global.exception.CustomException;
import z9.second.global.response.ErrorCode;

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

    @OneToMany(mappedBy = "classes", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ClassUserEntity> users = new ArrayList<>();

    @OneToMany(mappedBy = "classes", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ClassBlackListEntity> blackLists = new ArrayList<>();

    public ClassUserEntity addMember(Long userId) {
        // 이미 가입되어 있는지 체크
        if (users.stream().anyMatch((user) -> user.getUserId().equals(userId))) {
            throw new CustomException(ErrorCode.CLASS_EXISTS_MEMBER);
        }

        ClassUserEntity user = ClassUserEntity.builder()
                .classes(this)
                .userId(userId)
                .build();

        users.add(user);

        return user;
    }
}
