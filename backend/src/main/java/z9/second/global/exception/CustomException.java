package z9.second.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import z9.second.global.response.ErrorCode;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorCode code;
}
