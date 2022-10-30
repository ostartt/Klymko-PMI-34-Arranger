package com.arranger.eurekaclient.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PermutationValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermutationConstraint {
    String message() default "The length of the string must bu up to 10 symbols";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
