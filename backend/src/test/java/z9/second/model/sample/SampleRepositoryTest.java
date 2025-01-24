package z9.second.model.sample;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SampleRepositoryTest {

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("데이터 저장 테스트")
    @Test
    void testSaveTest() {
        // given
        SampleEntity newSample = SampleEntity.builder().firstName("강")
                .secondName("백과").age(10).build();

        // when
        SampleEntity savedData = sampleRepository.save(newSample);
        em.flush();
        em.clear();

        // then
        Optional<SampleEntity> findData = sampleRepository.findById(savedData.getId());
        assertThat(findData.isPresent()).isTrue();
    }
}