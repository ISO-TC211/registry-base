package de.geoinfoffm.registry.client.web.recaptcha;


import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;
import org.springframework.stereotype.Component;

@Component
public class CaptchaSettings {

    private final RegistryConfiguration registryConfiguration;

    public CaptchaSettings(RegistryConfiguration registryConfiguration) {
        this.registryConfiguration = registryConfiguration;
    }

    public boolean isCaptchaEnabled() {
        return this.registryConfiguration.isCaptchaEnabled();
    }

    public String getUrl() {
        return this.registryConfiguration.getRecaptchaUrl();
    }

    public String getKey() {
        return this.registryConfiguration.getRecaptchaKey();
    }

    public String getSecret() {
        return this.registryConfiguration.getRecaptchaSecret();
    }
}
