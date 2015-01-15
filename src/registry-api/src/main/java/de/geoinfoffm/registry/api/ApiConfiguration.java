package de.geoinfoffm.registry.api;

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

import de.geoinfoffm.registry.core.ItemClassRegistry;
import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;
import de.geoinfoffm.registry.core.security.RegistryLookupStrategy;
import de.geoinfoffm.registry.core.security.RegistryMutableAclService;
import de.geoinfoffm.registry.core.security.RegistryPermission;
import de.geoinfoffm.registry.persistence.ProposalRepository;
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
	public ProposalService proposalService(ProposalRepository repository) {
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
	
}