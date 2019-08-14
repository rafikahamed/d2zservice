package com.d2z.d2zservice.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConsignmentValidator.class)
@Documented
public @interface ValidConsignment {
    String message () default "Invalid Data";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};
}

