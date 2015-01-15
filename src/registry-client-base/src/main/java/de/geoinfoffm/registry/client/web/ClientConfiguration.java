package de.geoinfoffm.registry.client.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import de.geoinfoffm.registry.core.CoreConfiguration;

public class ClientConfiguration 
{
	private static final String CONFIG_FILE = "client.configuration.xml";
	
	private static Properties cachedConfiguration;
	
	public Properties configuration() {
		Properties properties = CoreConfiguration.loadPropertiesFromFile(CONFIG_FILE);
		
		if (properties == null) {
			properties = createDefaultProperties();
			saveProperties(properties, CONFIG_FILE);
		}

		return properties;
	}

	/**
	 * @return a default set of properties.
	 */
	private Properties createDefaultProperties() {
		Properties properties = new Properties();
		properties.setProperty("basePath", "");
		
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
	
	private static Properties instance() {
		if (cachedConfiguration == null) {
			ClientConfiguration instance = new ClientConfiguration();
			cachedConfiguration = instance.configuration();
		}

		return cachedConfiguration;
	}
	
	public static String getBasePath() {
		String basePath = instance().getProperty("basePath");
		if (basePath.startsWith("~")) {
			basePath = basePath.substring(1);
		}
		
		return basePath;
	}
	
	public static String getMailBaseUrl() {
		String baseUrl = instance().getProperty("mail.baseUrl", "mail.baseUrl.undefined");
		if (!baseUrl.endsWith("/")) {
			baseUrl = baseUrl + "/";
		}
		
		return baseUrl;		
	}
	
	public static boolean isSendConfirmationMails() {
		return instance().getProperty("signup.sendConfirmationMails", "false").equals("true");
	}

	public static String getSignupConfirmationUrl() {
		String confirmationUrl = instance().getProperty("signup.confirmationUrl");
		
		return confirmationUrl;
	}
}
