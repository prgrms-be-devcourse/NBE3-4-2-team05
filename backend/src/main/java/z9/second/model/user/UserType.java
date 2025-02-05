package z9.second.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    NORMAL("일반 회원"),
    OAUTH("소셜 회원"),
    ;

    private final String value;
}
