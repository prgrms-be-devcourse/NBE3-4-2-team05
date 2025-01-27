package z9.second.domain.classes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import z9.second.domain.classes.repository.ClassRepository;

@Service
@RequiredArgsConstructor
public class ClassService {
    private final ClassRepository classRepository;
}
