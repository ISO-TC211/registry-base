package de.geoinfoffm.registry.validators;

import de.geoinfoffm.registry.validators.impl.UniqueUserEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Documented
@Constraint(validatedBy = UniqueUserEmailValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface UniqueUserEmail
{
    String message() default "This email is already in use";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}