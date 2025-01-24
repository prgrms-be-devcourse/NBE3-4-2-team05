package z9.second.domain.sample.service;

import java.util.List;
import z9.second.domain.sample.dto.SampleRequest;
import z9.second.domain.sample.dto.SampleResponse;

public interface SampleService {

    SampleResponse.SampleDataInfo findSampleById(Long id);

    List<SampleResponse.SampleDataList> findAllSampleData();

    SampleResponse.SavedSampleData saveNewSampleData(SampleRequest.NewSampleData newSampleData);
}
