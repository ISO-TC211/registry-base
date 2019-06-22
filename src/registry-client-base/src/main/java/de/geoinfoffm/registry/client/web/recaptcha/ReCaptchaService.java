package de.geoinfoffm.registry.client.web.recaptcha;

public interface ReCaptchaService {

    boolean validate(String reCaptchaResponse);
}
