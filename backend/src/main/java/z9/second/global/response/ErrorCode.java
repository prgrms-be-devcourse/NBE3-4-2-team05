package z9.second.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //0000 ~ 0001
    // 오류 종류 : 샘플 도메인 에러 (추후 삭제 예정)
    NOT_EXIST_SAMPLE_DATA(HttpStatus.BAD_REQUEST, Boolean.FALSE, 0001, "샘플 데이터를 찾을 수 없습니다."),

    //1000 ~ 1999
    // 오류 종류 : 인증/인가 에러 ex) token expired
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, Boolean.FALSE, 1000, "잘못된 이메일 혹은 패스워드 입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, Boolean.FALSE, 1001, "유효하지 않은 Access Token 입니다."),
    NEED_LOGIN(HttpStatus.UNAUTHORIZED, Boolean.FALSE, 1002, "로그인이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, Boolean.FALSE, 1003, "접근 권한이 부족합니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, Boolean.FALSE, 1004, "Access Token 이 만료되었습니다."),
    OAUTH_USER_LOGIN_FAIL(HttpStatus.BAD_REQUEST, Boolean.FALSE, 1005, "소셜 로그인 회원 입니다. 소셜 로그인으로 진행 해 주세요."),
    INVALID_OAUTH_PROVIDER(HttpStatus.BAD_REQUEST, Boolean.FALSE, 1006, "지원하지 않는 로그인 방식 입니다."),

    //2000 ~ 2999
    // 오류 종류 : 회원 도메인 에러

    //3000 ~ 3999
    // 오류 종류 : 모임

    //9000 ~ 9999
    //오류 종류 : 공통 에러
    VALIDATION_FAIL_ERROR(HttpStatus.BAD_REQUEST, Boolean.FALSE, 9000, "(exception error 메세지에 따름)"),
    NOT_SUPPORTED_METHOD(HttpStatus.METHOD_NOT_ALLOWED, Boolean.FALSE, 9001, "(exception error 메세지에 따름"),
    NOT_FOUND_URL(HttpStatus.NOT_FOUND, Boolean.FALSE, 9002, "요청하신 URL 을 찾을 수 없습니다."),
    INVALID_REQUEST_DATA(HttpStatus.BAD_REQUEST, Boolean.FALSE, 9003, "데이터 저장 실패, 재시도 혹은 관리자에게 문의해주세요."),
    FAIL(HttpStatus.BAD_REQUEST, Boolean.FALSE, 9999, "요청 응답 실패, 관리자에게 문의해주세요."),
    ;

    private final HttpStatus httpStatus;
    private final Boolean isSuccess;
    private final int code;
    private final String message;
}
