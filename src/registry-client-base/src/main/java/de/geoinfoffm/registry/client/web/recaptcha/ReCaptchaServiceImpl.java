package de.geoinfoffm.registry.client.web.recaptcha;

import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;


@Service
public class ReCaptchaServiceImpl implements ReCaptchaService {

    private static final Logger log = LoggerFactory.getLogger(ReCaptchaServiceImpl.class);

    private final RestOperations restTemplate;

    private final HttpServletRequest request;

    private final CaptchaSettings captchaSettings;

    public ReCaptchaServiceImpl(RestOperations restTemplate, HttpServletRequest request, CaptchaSettings captchaSettings) {
        this.restTemplate = restTemplate;
        this.request = request;
        this.captchaSettings = captchaSettings;
    }

    public boolean validate(String reCaptchaResponse) {
        String url = String.format(this.captchaSettings.getUrl() + "?secret=%s&response=%s&remoteip=%s",
                this.captchaSettings.getSecret(),
                reCaptchaResponse,
                this.request.getRemoteAddr());

        URI verifyUri = URI.create(url);

        try {
            ReCaptchaResponse response = restTemplate.getForObject(verifyUri, ReCaptchaResponse.class);
            return response.isSuccess();
        }
        catch (Exception ignored){
            log.debug("ignore when google services are not available", ignored);
        }

        return true;
    }
}
