package de.geoinfoffm.registry.validators.impl;

import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.validators.UniqueUserEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {


    private RegistryUserService userService;

    public UniqueUserEmailValidator(RegistryUserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(final UniqueUserEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String email, final ConstraintValidatorContext context) {
        return userService.isEmailAddressAvailable(email);
    }
}
