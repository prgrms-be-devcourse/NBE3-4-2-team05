package z9.second.domain.sample.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import z9.second.domain.sample.service.SampleService;
import z9.second.domain.sample.dto.SampleRequest;
import z9.second.domain.sample.dto.SampleResponse;
import z9.second.global.response.BaseResponse;
import z9.second.global.response.SuccessCode;

@RestController
@RequestMapping("/api/v1/sample")
@Tag(name = "Sample Controller", description = "API 샘플 컨트롤러")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    /**
    //sample ID로 단건 찾기
    /*
        {
            "isSuccess": true,
            "message": "샘플 데이터 찾기 성공!",
            "code": 200,
            "data": {
                "fullName": "김-아무개1"
            }
        }
    */
    @GetMapping("/{sampleId}")
    @Operation(summary = "회원가입")
    public BaseResponse<SampleResponse.SampleDataInfo> getSampleData(
            @PathVariable("sampleId") Long sampleId
    ) {
        SampleResponse.SampleDataInfo data = sampleService.findSampleById(sampleId);
        return BaseResponse.ok(SuccessCode.FIND_SAMPLE_DATA_SUCCESS, data);
    }

    /**
    //저장된 모든 sample data 들 읽어오기
    /*
        {
            "isSuccess": true,
            "message": "샘플 전체 데이터 찾기 성공!",
            "code": 200,
            "data": [
                {
                    "fullName": "김-아무개1",
                    "id": 1
                },
                {
                    "fullName": "김-아무개2",
                    "id": 2
                }...
            ]
        }
    */
    @GetMapping
    public BaseResponse<List<SampleResponse.SampleDataList>> getSampleDataList() {
        List<SampleResponse.SampleDataList> findList = sampleService.findAllSampleData();
        return BaseResponse.ok(SuccessCode.FIND_SAMPLE_DATA_LIST_SUCCESS, findList);
    }

    /**
    //Sample 데이터 저장하기
    /*
        request :
         {
         "firstName" : "강",
         "secondName" : "성욱",
         "age" : 20
         }
        response :
         {
         "isSuccess": true,
         "message": "샘플 데이터 저장 성공!",
         "code": 200,
         "data": {
         "id": 11
         }
         }
    */
    @PostMapping
    public BaseResponse<SampleResponse.SavedSampleData> registerNewSample(
            @RequestBody(required = true) SampleRequest.NewSampleData newSampleData
    ) {
        SampleResponse.SavedSampleData savedSampleData
                = sampleService.saveNewSampleData(newSampleData);
        return BaseResponse.ok(SuccessCode.SAVE_SAMPLE_DATA_SUCCESS, savedSampleData);
    }
}
