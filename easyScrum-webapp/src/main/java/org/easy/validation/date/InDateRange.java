package org.easy.validation.date;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = InDateRangeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InDateRange {

    String message() default "{org.easy.validation.date.InDateRange.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    String min() default "1900-01-01";
    
    String max() default "9999-12-31";
}