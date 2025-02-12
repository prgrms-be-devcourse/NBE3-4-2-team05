package z9.second.global.annotation.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Constraint(validatedBy = { })
@Retention(RetentionPolicy.RUNTIME)
@Email(message = "이메일 형식이 아닙니다.")
public @interface UserLoginId {
    String message() default "Invalid user email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
