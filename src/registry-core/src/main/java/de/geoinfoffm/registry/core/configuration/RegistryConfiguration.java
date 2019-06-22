/**
 * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * <p>
 * * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * <p>
 * * The names "German Federal Agency for Cartography and Geodesy",
 * "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE",
 * "GDI-DE Registry" and the names of other contributors must not
 * be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE GERMAN
 * FEDERAL AGENCY FOR CARTOGRAPHY AND GEODESY BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.geoinfoffm.registry.core.configuration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;

import de.geoinfoffm.registry.core.CoreConfiguration;

public class RegistryConfiguration {
    private static final String CONFIG_FILE = "registry.configuration.xml";
    private static final String PROPERTY_BASE_PACKAGES = "basePackages";
    private static final String PROPERTY_MAIL_ENABLED = "mail.enabled";
    private static final String PROPERTY_MAIL_FROM_NAME = "mail.from.name";
    private static final String PROPERTY_MAIL_FROM_ADDRESS = "mail.from.address";
    private static final String PROPERTY_MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String PROPERTY_MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    private static final String PROPERTY_MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String PROPERTY_MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String PROPERTY_MAIL_BASE_URL = "mail.baseUrl";
    private static final String PROPERTY_ADMIN_EMAIL = "admin.email";
    private static final String PROPERTY_GML_IDENTIFIER_BASE_URL = "gml.identifier.baseurl";
    private static final String PROPERTY_GML_IDENTIFIER_PATH_PATTERN = "gml.identifier.pathpattern";
    private static final String PROPERTY_REGISTER_ALIASES = "register.aliases";
    private static final String PROPERTY_REGISTRY_DEMOMODE = "registry.demoMode";

    private static final String PROPERTY_RECAPTCHA_ENABLED = "recaptcha.enabled";
    private static final String PROPERTY_RECAPTCHA_URL = "recaptcha.url";
    private static final String PROPERTY_RECAPTCHA_KEY = "recaptcha.key";
    private static final String PROPERTY_RECAPTCHA_SECRET = "recaptcha.secret";

    private static RegistryConfiguration instance = null;

    private Properties configuration;

    public RegistryConfiguration() {
        this.configuration = CoreConfiguration.loadPropertiesFromFile(CONFIG_FILE);

        if (this.configuration == null) {
            configuration = createDefaultProperties();
            saveProperties(configuration, CONFIG_FILE);
        }
    }

    public static RegistryConfiguration getInstance() {
        if (instance == null) {
            instance = new RegistryConfiguration();
        }

        return instance;
    }

    /**
     * @return a default set of properties.
     */
    private Properties createDefaultProperties() {
        Properties properties = new Properties();
        properties.setProperty(PROPERTY_BASE_PACKAGES, "de.geoinfoffm.registry,de.bespire.registry");
        properties.setProperty(PROPERTY_MAIL_ENABLED, "false");
        properties.setProperty(PROPERTY_MAIL_FROM_NAME, "Registry");
        properties.setProperty(PROPERTY_MAIL_FROM_ADDRESS, "registry@example.org");
        properties.setProperty(PROPERTY_MAIL_SMTP_AUTH, "false");
        properties.setProperty(PROPERTY_MAIL_SMTP_STARTTLS_ENABLE, "false");
        properties.setProperty(PROPERTY_MAIL_SMTP_HOST, "smtp.example.org");
        properties.setProperty(PROPERTY_MAIL_SMTP_PORT, "25");

        return properties;
    }

    /**
     * @param properties
     * @param propertiesFile
     */
    private void saveProperties(Properties properties, String propertiesFile) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(propertiesFile);
            properties.storeToXML(fos, "", "UTF-8");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                fos.close();
            }
            catch (IOException e) {
                // Ignore
            }
        }
    }

    public boolean isDemoMode() {
        // Use inverted logic to activate demo mode as a fallback for wrong configuration
        // values
        return !this.configuration.getProperty(PROPERTY_REGISTRY_DEMOMODE, "true").equals("false");
    }

    public boolean isMailEnabled() {
        return this.configuration.getProperty(PROPERTY_MAIL_ENABLED, "false").equalsIgnoreCase("true");
    }

    public void setMailEnabled(boolean enabled) {
        this.configuration.setProperty(PROPERTY_MAIL_ENABLED, Boolean.toString(enabled));
    }

    public InternetAddress getSenderAddress() throws UnsupportedEncodingException, AddressException {
        String address = this.configuration.getProperty(PROPERTY_MAIL_FROM_ADDRESS);
        String name = this.configuration.getProperty(PROPERTY_MAIL_FROM_NAME);

        if (StringUtils.isEmpty(name)) {
            return new InternetAddress(address);
        }
        else {
            return new InternetAddress(address, name);
        }
    }

    public String[] getBasePackages() {
        String basePackages = this.configuration.getProperty(PROPERTY_BASE_PACKAGES);

        if (basePackages != null && !StringUtils.isEmpty(basePackages)) {
            String[] packages = basePackages.split(",");
            List<String> result = new ArrayList<String>();
            for (String pkg : packages) {
                result.add(pkg.trim());
            }

            return result.toArray(packages);
        }

        return null;
    }

    public String getMailBaseUrl() {
        String baseUrl = this.configuration.getProperty(PROPERTY_MAIL_BASE_URL, PROPERTY_MAIL_BASE_URL + ".undefined");
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }

        return baseUrl;
    }

    public boolean isAdministrator(String emailAddress) {
        String adminEmail = this.configuration.getProperty(PROPERTY_ADMIN_EMAIL, null);
        return !StringUtils.isEmpty(emailAddress) && emailAddress.equals(adminEmail);
    }

    public String getGmlIdentifierBaseUrl() {
        String baseUrl = this.configuration.getProperty(PROPERTY_GML_IDENTIFIER_BASE_URL);

        if (StringUtils.isEmpty(baseUrl)) {
            baseUrl = this.getMailBaseUrl();
        }

        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }

        return baseUrl;
    }

    public String getGmlIdentifierPathPattern() {
        return this.configuration.getProperty(PROPERTY_GML_IDENTIFIER_PATH_PATTERN, "def/%d");
    }

    public Map<String, String> getRegisterAliases() {
        return extractAliases(PROPERTY_REGISTER_ALIASES);
    }

    public String getRegisterNameByAlias(String alias) {
        return getRegisterAliases().get(alias);
    }

    public String getRegisterAliasByName(String name) {
        if (getRegisterAliases().containsValue(name)) {
            for (Entry<String, String> entry : getRegisterAliases().entrySet()) {
                if (entry.getValue().equals(name)) {
                    return entry.getKey();
                }
            }
        }

        return null;
    }

    private Map<String, String> extractAliases(String property) {
        Map<String, String> result = new HashMap<>();

        String aliases = this.configuration.getProperty(property, "");
        if (StringUtils.isEmpty(aliases)) {
            return result;
        }

        String[] pairs = aliases.split(";");
        if (pairs == null) {
            return result;
        }

        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue != null && keyValue.length == 2 && !StringUtils.isEmpty(keyValue[0]) && !StringUtils.isEmpty(keyValue[1])) {
                result.put(keyValue[0], keyValue[1]);
            }
        }

        return result;
    }

    public boolean isCaptchaEnabled() {
        return Boolean.parseBoolean(this.configuration.getProperty(PROPERTY_RECAPTCHA_ENABLED, "false"));
    }

    public String getRecaptchaUrl() {
        String url = this.configuration.getProperty(PROPERTY_RECAPTCHA_URL);
        return url.replaceAll("/*$", "");
    }

    public String getRecaptchaKey() {
        return this.configuration.getProperty(PROPERTY_RECAPTCHA_KEY);
    }

    public String getRecaptchaSecret() {
        return this.configuration.getProperty(PROPERTY_RECAPTCHA_SECRET);
    }
}
