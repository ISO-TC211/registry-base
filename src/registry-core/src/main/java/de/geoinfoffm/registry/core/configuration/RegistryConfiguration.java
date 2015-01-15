package de.geoinfoffm.registry.core.configuration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;

import de.geoinfoffm.registry.core.CoreConfiguration;

public class RegistryConfiguration 
{
	private static final String CONFIG_FILE = "registry.configuration.xml";
	private static final String PROPERTY_BASE_PACKAGES = "basePackages";
	private static final String PROPERTY_MAIL_ENABLED = "mail.enabled";
	private static final String PROPERTY_MAIL_FROM_NAME = "mail.from.name";
	private static final String PROPERTY_MAIL_FROM_ADDRESS = "mail.from.address";
	private static final String PROPERTY_MAIL_SMTP_AUTH = "mail.smtp.auth";
	private static final String PROPERTY_MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
	private static final String PROPERTY_MAIL_SMTP_HOST = "mail.smtp.host";
	private static final String PROPERTY_MAIL_SMTP_PORT = "mail.smtp.port";
	private static final String PROPERTY_ADMIN_EMAIL = "admin.email";
	
	private Properties configuration;
	
	public RegistryConfiguration() {
		this.configuration = CoreConfiguration.loadPropertiesFromFile(CONFIG_FILE);
		
		if (this.configuration == null) {
			configuration = createDefaultProperties();
			saveProperties(configuration, CONFIG_FILE);
		}
	}

	/**
	 * @return a default set of properties.
	 */
	private Properties createDefaultProperties() {
		Properties properties = new Properties();
		properties.setProperty(PROPERTY_BASE_PACKAGES, "de.geoinfoffm.registry");
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
	
	public boolean isAdministrator(String emailAddress) {
		String adminEmail = this.configuration.getProperty(PROPERTY_ADMIN_EMAIL, null);
		return !StringUtils.isEmpty(emailAddress) && emailAddress.equals(adminEmail);
	}
}
