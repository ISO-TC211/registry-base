package de.geoinfoffm.registry.client.web.signup;

import de.geoinfoffm.registry.client.web.SignupFormBean;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OrganizationValidator implements ConstraintValidator<ValidOrganization, SignupFormBean> {

    public void initialize(ValidOrganization constraint) {
    }

    public boolean isValid(SignupFormBean bean, ConstraintValidatorContext context) {
        if (bean.isOrganizationNotListed() || StringUtils.isBlank(bean.getOrganizationUuid())) {
            return true;
        }

        boolean valid = !"%%new%%".equalsIgnoreCase(bean.getOrganizationUuid());
        if (!valid) {
            String template = context.getDefaultConstraintMessageTemplate();
            context.buildConstraintViolationWithTemplate(template)
                    .addPropertyNode("organizationUuid")
                    .addConstraintViolation();
//         context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addNode("organizationUuid").addConstraintViolation();
        }
        return valid;
    }
}
