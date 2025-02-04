package z9.second.global.annotation.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { })
@Size(min = 3, max = 10, message = "닉네임은 3자리 이상 10자리 미만 입니다.")
public @interface UserNickname {
    String message() default "Invalid user password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
