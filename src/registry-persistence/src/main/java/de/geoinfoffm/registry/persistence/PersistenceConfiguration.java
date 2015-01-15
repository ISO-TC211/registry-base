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
package de.geoinfoffm.registry.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import de.geoinfoffm.registry.core.Repository;
import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;
import de.geoinfoffm.registry.persistence.jpa.HibernateConfiguration;

/**
 * Spring configuration class for the persistence layer.
 * 
 * @author Florian Esser
 * 
 */
@ComponentScan(basePackages = { "de.geoinfoffm.registry" })
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "de.geoinfoffm.registry",
				       repositoryFactoryBeanClass = EntityBackendFactoryBean.class)
public class PersistenceConfiguration
{
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private Environment env;

	/**
	 * @return the {@link EntityManager}
	 */
	@Bean
	public EntityManager entityManager() {
		return em;
	}
		
	/**
	 * Creates a {@link LocalContainerEntityManagerFactoryBean} that will handle the creation
	 * of {@link EntityManager}s used by the {@link Repository}s of domain object {@link Repository}s.
	 */
	@Autowired
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(HibernateConfiguration hibernateConfiguration, RegistryConfiguration registryConfiguration) {
		
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource(hibernateConfiguration));
		em.setPackagesToScan(registryConfiguration.getBasePackages());
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(hibernateConfiguration.additionalParameters());

		return em;
	}
	
	@Autowired
	@Bean
	public LocalSessionFactoryBean sessionFactory(HibernateConfiguration hibernateConfiguration, RegistryConfiguration registryConfiguration) {
		LocalSessionFactoryBean result = new LocalSessionFactoryBean();
		result.setDataSource(dataSource(hibernateConfiguration));
		result.setPackagesToScan(registryConfiguration.getBasePackages());
		
		return result;
	}

	/**
	 * @return the {@link DataSource} for the application, based on the connection properties
	 */
	@Autowired
	@Bean
	public DataSource dataSource(HibernateConfiguration hibernateConfiguration) {
		return hibernateConfiguration.dataSource();
	}

	/**
	 * @return the {@link PlatformTransactionManager} for the application
	 */
	@Autowired
	@Bean
	public PlatformTransactionManager transactionManager(HibernateConfiguration hibernateConfiguration, RegistryConfiguration registryConfiguration) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory(hibernateConfiguration, registryConfiguration).getObject());

		return transactionManager;
	}

	/**
	 * @return the {@link PersistenceExceptionTranslationPostProcessor} for the application
	 */
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
}
