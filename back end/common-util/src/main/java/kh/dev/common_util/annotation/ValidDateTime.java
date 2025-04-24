package kh.dev.common_util.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import kh.dev.common_util.annotation.validator.DateTimeValidator;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateTimeValidator.class)
public @interface ValidDateTime {

  boolean isFuture() default false;

  boolean isPast() default false;

  String message() default "invalid date time";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
