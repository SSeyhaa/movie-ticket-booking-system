package kh.dev.common_util.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import kh.dev.common_util.annotation.validator.TimeSlotValidator;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeSlotValidator.class)
public @interface ValidTimeSlot {

  String message() default "Start time must be before end time";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
