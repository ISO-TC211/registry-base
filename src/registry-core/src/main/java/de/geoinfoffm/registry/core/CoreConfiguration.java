/**
 * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *     * Redistributions of source code must retain the above copyright
 *     	 notice, this list of conditions and the following disclaimer.

 *     * Redistributions in binary form must reproduce the above
 *     	 copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials
 *       provided with the distribution.

 *     * The names "German Federal Agency for Cartography and Geodesy",
 *       "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE",
 *       "GDI-DE Registry" and the names of other contributors must not
 *       be used to endorse or promote products derived from this
 *       software without specific prior written permission.
 *
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
package de.geoinfoffm.registry.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.tools.ToolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.ui.velocity.VelocityEngineUtils;

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
	public ConversionService conversionService() {
		return new DefaultFormattingConversionService();
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

	@Bean
	public VelocityContext velocityContext() {
		ToolManager toolMgr = new ToolManager();
		toolMgr.configure("velocity-tools.xml");

		return new VelocityContext(toolMgr.createContext());
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

		//override properties from System-env
		Enumeration<?> names = properties.propertyNames();
		Properties result = new Properties();
		while (names.hasMoreElements()) {
			String name = names.nextElement().toString();
			String env = System.getenv(name);

			result.setProperty(name, properties.getProperty(name));
			if (StringUtils.isNotBlank(env)) {
				logger.debug(String.format(
						"Override property [%s] by ENV with value [%s]",
						name,
						env
						)
				);
				result.setProperty(name, env);
			}

		}

		return result;
	}

}
