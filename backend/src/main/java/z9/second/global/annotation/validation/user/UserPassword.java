package z9.second.global.annotation.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { })
@Size(min = 8, max = 20, message = "비밀번호는 8자리 ~ 20자리 사이 입니다.")
@Pattern(regexp = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$",
        message = "비밀번호는 특수문자를 반드시 포함하여야 합니다.")
public @interface UserPassword {
    String message() default "Invalid user password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
