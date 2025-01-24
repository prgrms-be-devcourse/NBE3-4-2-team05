package z9.second.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    // Sample (추후 삭제 예정)
    FIND_SAMPLE_DATA_SUCCESS(HttpStatus.OK, Boolean.TRUE, 200, "샘플 데이터 찾기 성공!"),
    FIND_SAMPLE_DATA_LIST_SUCCESS(HttpStatus.OK, Boolean.TRUE, 200, "샘플 전체 데이터 찾기 성공!"),
    SAVE_SAMPLE_DATA_SUCCESS(HttpStatus.OK, Boolean.TRUE, 200, "샘플 데이터 저장 성공!"),

    // Authentication / Authorization

    // User

    // Common
    SUCCESS(HttpStatus.OK, Boolean.TRUE, 200, "요청 응답 성공"),
    ;

    private final HttpStatus httpStatus;
    private final Boolean isSuccess;
    private final int code;
    private final String message;
}
