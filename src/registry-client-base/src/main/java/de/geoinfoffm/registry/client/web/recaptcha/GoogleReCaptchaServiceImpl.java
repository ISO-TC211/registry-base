package de.geoinfoffm.registry.client.web.recaptcha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;


@Service
public class GoogleReCaptchaServiceImpl implements ReCaptchaService {

    private static final Logger log = LoggerFactory.getLogger(GoogleReCaptchaServiceImpl.class);

    private final RestOperations restTemplate;

    private final HttpServletRequest request;

    private final CaptchaSettings captchaSettings;

    public GoogleReCaptchaServiceImpl(RestOperations restTemplate, HttpServletRequest request, CaptchaSettings captchaSettings) {
        this.restTemplate = restTemplate;
        this.request = request;
        this.captchaSettings = captchaSettings;
    }

    public boolean validate(String reCaptchaResponse) {
        UriComponentsBuilder verifyUriBuilder = UriComponentsBuilder.fromHttpUrl(this.captchaSettings.getUrl())
                .queryParam("secret", this.captchaSettings.getSecret())
                .queryParam("response", reCaptchaResponse)
                .queryParam("remoteip", this.request.getRemoteAddr());

        URI verifyUri = URI.create(verifyUriBuilder.toUriString());

        try {
            ReCaptchaResponse response = restTemplate.getForObject(verifyUri, ReCaptchaResponse.class);
            return response.isSuccess();
        }
        catch (Exception e){
            log.error("Captcha error", e);
        }

        return false;
    }
}
