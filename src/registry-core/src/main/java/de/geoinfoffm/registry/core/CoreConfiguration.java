package de.geoinfoffm.registry.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;

/**
 * Spring configuration for the Registry core.
 * 
 * @author Florian Esser
 * 
 */
//@ComponentScan(basePackages = { "de.geoinfoffm.registry" })
@ComponentScan
@Configuration
@PropertySource("classpath:registry.configuration.xml")
public class CoreConfiguration
{
	private static final Logger logger = LoggerFactory.getLogger(CoreConfiguration.class);
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public RegistryConfiguration registryConfiguration() {
		return new RegistryConfiguration();
	}
	
	@Autowired
	@Bean
	public ItemClassRegistry itemClassRegistry(ApplicationContext context) {
		return new ItemClassRegistry(context);
	}

	@Autowired
	@Bean
	public JavaMailSender javaMailSender(Environment env) {
		String enable = env.getProperty("mail.enabled");
		if (StringUtils.isEmpty(enable) || !"true".equals(enable)) {
			logger.debug("{{JavaMailSender}} Mail feature disabled: Parameter 'mail.enabled' != 'true' [is: '{}']", enable);
			return new NullMailSender();
			
		}
		else {
			logger.debug("{{JavaMailSender}} Mail feature enabled");
		}
		
		String host = env.getProperty("mail.smtp.host");
		String port = env.getProperty("mail.smtp.port");
		
		if (StringUtils.isEmpty(host) || StringUtils.isEmpty(port)) {
			logger.debug("{{JavaMailSender}} Mail feature disabled: Parameters 'mail.smtp.host' and 'mail.smtp.port' must be set [mail.smtp.host='{}', mail.smtp.port='{}']", host, port);
			return new NullMailSender();
		}

		String fromName = env.getProperty("mail.from.name");
		String fromAddress = env.getProperty("mail.from.address");
		
		if (StringUtils.isEmpty(fromName) || StringUtils.isEmpty(fromName)) {
			logger.debug("{{JavaMailSender}} Mail feature disabled: Parameters 'mail.from.name' and 'mail.from.address' must be set [mail.from.name='{}', mail.from.address='{}']", fromName, fromAddress);
			return new NullMailSender();
		}

		JavaMailSenderImpl result = new JavaMailSenderImpl();
		result.setHost(env.getProperty("mail.smtp.host"));
		result.setPort(Integer.parseInt(env.getProperty("mail.smtp.port")));

		if ("true".equals(env.getProperty("mail.smtp.auth"))) {
			String username = env.getProperty("mail.smtp.user");
			String password = env.getProperty("mail.smtp.password");

			logger.debug("{{JavaMailSender}} SMTP authentication activated [user='{}']", username);

			result.setUsername(username);
			result.setPassword(password);
		}
		else {
			logger.debug("{{JavaMailSender}} SMTP authentication disabled");
		}
		
		if ("true".equals(env.getProperty("mail.smtp.starttls.enable"))) {
			result.getJavaMailProperties().put("mail.smtp.starttls.enable", true);
			result.getJavaMailProperties().put("mail.smtp.ssl.trust", "*");
			logger.debug("{{JavaMailSender}} TLS is enabled");
		}
		else {
			logger.debug("{{JavaMailSender}} TLS not enabled");
		}
		
		return result;
	}
	
	@Bean
	public VelocityEngine velocityEngine() throws VelocityException, IOException {
		VelocityEngineFactoryBean factory = new VelocityEngineFactoryBean();
		
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("resource.loader", "class");
		properties.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		factory.setVelocityPropertiesMap(properties);
		
		return factory.createVelocityEngine();
	}
	
	/**
	 * Loads properties from the specified file.
	 */
	public static Properties loadPropertiesFromFile(String fileName) {
		Properties properties = new Properties();

		File propertiesFile;
		ClassPathResource cpr = new ClassPathResource(fileName);
		try {
			propertiesFile = cpr.getFile();
		}
		catch (IOException e) {
			return null;
		}

		if (!propertiesFile.exists()) {
			return null;
		}

		InputStream fis = null;
		try {
			fis = cpr.getInputStream();
			properties.loadFromXML(fis);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if (fis != null) {
					fis.close();
				}
			}
			catch (IOException e) {
				// Ignore
			}
		}

		return properties;
	}

}
