package de.geoinfoffm.registry.client.web.recaptcha;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReCaptchaConstraintValidator implements ConstraintValidator<ValidReCaptcha, String> {

    @Autowired
    private ReCaptchaService reCaptchaService;

    @Autowired
    private CaptchaSettings captchaSettings;

    @Override
    public void initialize(ValidReCaptcha constraintAnnotation) {
    }

    @Override
    public boolean isValid(String reCaptchaResponse, ConstraintValidatorContext context) {
        if (!this.captchaSettings.isCaptchaEnabled()) {
            return true;
        }

        if (StringUtils.isBlank(reCaptchaResponse)) {//reCaptchaResponse == null || reCaptchaResponse.isEmpty()){
            return false;
        }

        return reCaptchaService.validate(reCaptchaResponse);
    }

}
