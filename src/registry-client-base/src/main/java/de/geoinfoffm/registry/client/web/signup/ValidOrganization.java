package de.geoinfoffm.registry.client.web.signup;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = OrganizationValidator.class)
@Documented
public @interface ValidOrganization
{
    String message() default "Invalid organization";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}