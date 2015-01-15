/**
 * 
 */
package de.geoinfoffm.registry.persistence.jpa;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import de.geoinfoffm.registry.core.CoreConfiguration;

/**
 * Hibernate configuration for the GDI-DE Registry Client.
 * 
 * @author Florian Esser
 * 
 */
public class HibernateConfigurationImpl implements HibernateConfiguration 
{
	private static final Logger logger = LoggerFactory.getLogger(HibernateConfigurationImpl.class);
	
	private static final String DATASOURCE_FILE = "hibernate.connection.xml";
	private static final String PROPERTIES_FILE = "hibernate.properties.xml";
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.persistence.jpa.HibernateConfiguration#additionalParameters()
	 */
	@Override
	public Properties additionalParameters() {
		Properties hibernateProperties = CoreConfiguration.loadPropertiesFromFile(PROPERTIES_FILE);
		
		if (hibernateProperties == null) {
			hibernateProperties = createDefaultProperties();
			saveProperties(hibernateProperties, PROPERTIES_FILE);
		}
		
		for (Object key : hibernateProperties.keySet()) {
			logger.debug("Additional Hibernate parameter '{}': {}", key.toString(), hibernateProperties.get(key).toString());
		}

		return hibernateProperties;
	}


	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.persistence.jpa.HibernateConfiguration#dataSource()
	 */
	@Override
	public DriverManagerDataSource dataSource() {
		Properties properties = CoreConfiguration.loadPropertiesFromFile(DATASOURCE_FILE);
		if (properties == null) {
			properties = createDefaultDataSourceProperties();
			saveProperties(properties, DATASOURCE_FILE);
		}
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(properties.getProperty("hibernate.connection.driver_class"));
		dataSource.setUrl(properties.getProperty("hibernate.connection.url"));
		dataSource.setUsername(properties.getProperty("hibernate.connection.username"));
		dataSource.setPassword(properties.getProperty("hibernate.connection.password"));
		
		return dataSource;
	}

	/**
	 * @return a default set of Hibernate properties.
	 */
	private Properties createDefaultProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.show_sql", "true");
		
		return properties;
	}
	
	/**
	 * @return a default set of Hibernate connection properties.
	 */
	private Properties createDefaultDataSourceProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
		properties.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/gdireg");
		properties.setProperty("hibernate.connection.username", "org.postgresql.Driver");
		properties.setProperty("hibernate.connection.password", "org.postgresql.Driver");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

		return properties;
	}

	/**
	 * @param hibernateProperties
	 * @param hibernatePropertiesFile
	 */
	private void saveProperties(Properties hibernateProperties, String hibernatePropertiesFile) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(hibernatePropertiesFile);
			hibernateProperties.storeToXML(fos, "", "UTF-8");
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
}
