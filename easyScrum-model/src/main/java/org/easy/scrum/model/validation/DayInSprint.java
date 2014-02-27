package org.easy.scrum.model.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = DayInSprintValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DayInSprint {

    String message() default "{org.easy.scrum.model.validation.DayInSprint}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}