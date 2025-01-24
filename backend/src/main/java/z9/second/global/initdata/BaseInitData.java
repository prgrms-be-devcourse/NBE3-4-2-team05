package z9.second.global.initdata;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import z9.second.model.sample.SampleEntity;
import z9.second.model.sample.SampleRepository;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class BaseInitData {

    private final SampleRepository sampleRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    void init() {
        List<SampleEntity> sampleData = createSampleData(10);
    }

    private List<SampleEntity> createSampleData(final int count) {
        if(sampleRepository.count() != 0) {
            return sampleRepository.findAll();
        }
        if(count == 0) return null;

        List<SampleEntity> savedDataList = new ArrayList<>();

        for(int i=1; i<=count; i++) {
            String firstName = "김";
            String secondName = String.format("%s%d", "아무개", i);
            SampleEntity sample = SampleEntity.builder().firstName(firstName).secondName(secondName).build();
            savedDataList.add(sampleRepository.save(sample));
        }

        return savedDataList;
    }
}
