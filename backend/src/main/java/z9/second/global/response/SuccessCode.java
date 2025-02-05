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
    LOGIN_SUCCESS(HttpStatus.OK, Boolean.TRUE, 200, "로그인 성공"),
    LOGOUT_SUCCESS(HttpStatus.OK, Boolean.TRUE, 200, "로그아웃 성공"),
    SIGNUP_SUCCESS(HttpStatus.CREATED, Boolean.TRUE, 201, "회원가입 성공"),
    RESIGN_SUCCESS(HttpStatus.OK, Boolean.TRUE, 200, "회원탈퇴 성공"),

    // User

    // Class
    CLASS_CREATE_SUCCESS(HttpStatus.CREATED, Boolean.TRUE, 201, "모임이 생성되었습니다."),
    CLASS_JOIN_SUCCESS(HttpStatus.OK, Boolean.TRUE, 200, "모임에 가입되었습니다."),
    CLASS_RESIGN_SUCCESS(HttpStatus.OK, Boolean.TRUE, 200, "모임에서 탈퇴되었습니다."),

    //Schedules
    SCHEDULE_CREATE_SUCCESS(HttpStatus.CREATED, Boolean.TRUE, 201, "모임 일정 생성되었습니다!"),
    SCHEDULE_READ_SUCCESS(HttpStatus.OK, Boolean.TRUE, 200, "모임 전체 일정 조회되었습니다!"),

    // Common
    SUCCESS(HttpStatus.OK, Boolean.TRUE, 200, "요청 응답 성공"),
    ;

    private final HttpStatus httpStatus;
    private final Boolean isSuccess;
    private final int code;
    private final String message;
}
