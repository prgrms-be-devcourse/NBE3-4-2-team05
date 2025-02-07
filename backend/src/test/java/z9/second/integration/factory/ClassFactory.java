package z9.second.integration.factory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import z9.second.domain.classes.repository.ClassRepository;

@Component
@RequiredArgsConstructor
public final class ClassFactory {

    public static final String CLASS_NAME_PREFIX = "새로운 모임";
    public static final String CLASS_DESCRIPTION = "모임 설명 글 입니다!!";

    private final ClassRepository classRepository;
    private final EntityManager em;
}
