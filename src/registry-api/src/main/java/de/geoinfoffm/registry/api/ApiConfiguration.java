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
package de.geoinfoffm.registry.api;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.EhCacheBasedAclCache;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import de.geoinfoffm.registry.core.ItemClassRegistry;
import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;
import de.geoinfoffm.registry.core.model.ProposalRepoository;
import de.geoinfoffm.registry.core.security.RegistryLookupStrategy;
import de.geoinfoffm.registry.core.security.RegistryMutableAclService;
import de.geoinfoffm.registry.core.security.RegistryPermission;
import de.geoinfoffm.registry.persistence.RegisterRepository;
import de.geoinfoffm.registry.persistence.jpa.HibernateConfiguration;

/**
 * Spring configuration for the Registry API.
 * 
 * @author Florian Esser
 *
 */
@ComponentScan(basePackages = { "de.geoinfoffm.registry" })
@Configuration
public class ApiConfiguration 
{
	@Autowired
	private HibernateConfiguration configuration;
	
	@Autowired
	@Bean
	public ProposalService proposalService(ProposalRepoository repository) {
		return new ProposalServiceImpl(repository);
	}
	
	@Bean
	public EhCacheFactoryBean ehCacheFactoryBean() {
		EhCacheFactoryBean result = new EhCacheFactoryBean();
		
		EhCacheManagerFactoryBean manager = new EhCacheManagerFactoryBean();
		manager.setCacheManagerName("aclCache");
		
		result.setCacheManager(manager.getObject());
		
		return result;
	}
	
	@Bean
	public AclCache aclCache() {
		return new EhCacheBasedAclCache(ehCacheFactoryBean().getObject(), permissionGrantingStrategy(), aclAuthorizationStrategy());		
	}
	
	@Bean
	public PermissionGrantingStrategy permissionGrantingStrategy() {
		AuditLogger auditLogger = new ConsoleAuditLogger();
		return new DefaultPermissionGrantingStrategy(auditLogger);
	}
	
	@Bean
	public AclAuthorizationStrategy aclAuthorizationStrategy() {
		GrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");
		return new AclAuthorizationStrategyImpl(admin, admin, admin);
	}
	
	@Bean
	public LookupStrategy lookupStrategy() {
		DataSource dataSource = configuration.dataSource();
		return new RegistryLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), permissionGrantingStrategy());
	}
	
	@Bean
	public MutableAclService mutableAclService() {
		return new RegistryMutableAclService(configuration.dataSource(), lookupStrategy(), aclCache());
	}
	
	@Bean
	public AclService aclService() {
		return mutableAclService();
	}

	@Bean
	public AclPermissionEvaluator aclPermissionEvaluator() {
		AclPermissionEvaluator result = new AclPermissionEvaluator(aclService());
		result.setPermissionFactory(permissionFactory());
		
		return result;
	}
	
	@Bean
	public PermissionFactory permissionFactory() {
		return new DefaultPermissionFactory(RegistryPermission.class);
	}
	
	@Autowired
	@Bean
	public ItemFactoryRegistry itemFactoryRegistry(ApplicationContext context, RegistryConfiguration configuration) {
		return new ItemFactoryRegistry(context, configuration);
	}

	@Bean
	public Validator validator() {
		return new LocalValidatorFactoryBean();
	}

	@Autowired
	@Bean
	public RegisterService registerService(RegisterRepository registerRepository) {
		return new RegisterServiceImpl(registerRepository);
	}
	
	@Autowired
	@Bean
	public ProposalDtoFactory proposalDtoFactory(ItemClassRegistry registry, EntityManager entityManager) {
		return new ProposalDtoFactory(registry, entityManager);
	}
}