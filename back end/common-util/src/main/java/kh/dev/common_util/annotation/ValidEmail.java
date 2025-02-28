package kh.dev.common_util.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import kh.dev.common_util.annotation.validator.EmailValidator;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface ValidEmail {

  String regex() default "^(?!.*\\.\\.)([a-zA-Z0-9][a-zA-Z0-9._%+-]*[a-zA-Z0-9])@[a-zA-Z0-9.-]+[a-zA-Z0-9]\\.[a-zA-Z]{2,}$";

  boolean required() default false;

  String message() default "Invalid email address";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
