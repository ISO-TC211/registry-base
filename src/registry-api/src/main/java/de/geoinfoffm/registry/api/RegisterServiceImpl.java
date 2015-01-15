/**
 * 
 */
package de.geoinfoffm.registry.api;

import static de.geoinfoffm.registry.core.security.RegistrySecurity.*;
import static de.geoinfoffm.registry.core.security.RegistryPermission.*;
import static org.springframework.security.acls.domain.BasePermission.*;

import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.geoinfoffm.registry.core.ParameterizedRunnable;
import de.geoinfoffm.registry.core.RegistersChangedEvent;
import de.geoinfoffm.registry.core.model.Actor;
import de.geoinfoffm.registry.core.model.RegisterRelatedRole;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.iso19115.CI_ResponsibleParty;
import de.geoinfoffm.registry.core.model.iso19115.CI_RoleCode;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterManager;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterOwner;
import de.geoinfoffm.registry.persistence.RegisterRepository;
import de.geoinfoffm.registry.persistence.xml.StaxSerializationStrategy;
import de.geoinfoffm.registry.persistence.xml.StaxXmlSerializer;
import de.geoinfoffm.registry.persistence.xml.exceptions.XmlSerializationException;

/**
 * @author Florian.Esser
 *
 */
@Transactional @Service
public class RegisterServiceImpl
extends AbstractApplicationService<RE_Register, RegisterRepository> 
implements RegisterService, ApplicationListener<RegistersChangedEvent>
{
	@Autowired
	private RoleService roleService;
	
	private Map<UUID, String> registerNamesCache;
	
	/**
	 * @param repository
	 */
	@Autowired
	public RegisterServiceImpl(RegisterRepository repository) {
		super(repository);
	}

	@Override
	public void toXml(RE_Register register, Writer output) throws XmlSerializationException {
		StaxXmlSerializer<Writer> s = new StaxXmlSerializer<Writer>(new StaxSerializationStrategy(true));
		s.serialize(register, output);
	}

	@Override
	@Transactional(readOnly = true)
	public RE_Register findByName(String name) {
		RE_Register result = repository().findByName(name);
		return result;
	}

	@Override
	public Map<UUID, String> getCachedRegisterNames() {
		if (this.registerNamesCache == null) {
			this.refreshRegisterNamesCache();
		} 
		return Collections.unmodifiableMap(new LinkedHashMap<UUID,String>(this.registerNamesCache));
	}
	
	public void refreshRegisterNamesCache() {
		if (this.registerNamesCache == null) {
			this.registerNamesCache = new LinkedHashMap<UUID, String>();
		}
		
		List<Object[]> objects = repository().getRegisterNames();
		
		for (Object[] object : objects) {
			this.registerNamesCache.put((UUID)object[0], (String)object[1]);
		}
	}

	@Override
	@Transactional
	public RE_Register createRegister(String name, Actor registerOwner, Actor registerManager, Actor controlBody, RoleService roleService) {
		return createRegister(name, registerOwner, registerManager, controlBody, roleService, RE_Register.class, null);
	}

	@Override
	public <R extends RE_Register> R createRegister(String name, Actor registerOwner,
			Actor registerManager, Actor controlBody, RoleService roleService, Class<R> registerClass, ParameterizedRunnable<R> setter) {

		R result = BeanUtils.instantiateClass(registerClass);
		result.setName(name);
		
		RE_RegisterOwner owner = new RE_RegisterOwner();
		owner.setName(registerOwner.getName());
		CI_ResponsibleParty contact = new CI_ResponsibleParty(owner.getName(), registerOwner.getName(), "Register owner", CI_RoleCode.OWNER);
		owner.setContact(contact);
		result.setOwner(owner);
		
		RE_RegisterManager manager = new RE_RegisterManager();
		manager.setName(registerManager.getName());
		CI_ResponsibleParty managerContact = new CI_ResponsibleParty(manager.getName(), registerManager.getName(), "Register manager", CI_RoleCode.CUSTODIAN);
		manager.setContact(managerContact);
		result.setManager(manager);
		
		if (setter != null) {
			setter.run(result);
		}
		
		result = repository().save(result);

		// Create roles for new register
		Role managerRole = roleService.createRole(MANAGER_ROLE_PREFIX + result.getUuid().toString(), result);
		Role ownerRole = roleService.createRole(OWNER_ROLE_PREFIX + result.getUuid().toString(), result);
		Role submitterRole = roleService.createRole(SUBMITTER_ROLE_PREFIX + result.getUuid().toString(), result);
		Role controlBodyRole = roleService.createRole(CONTROLBODY_ROLE_PREFIX + result.getUuid().toString(), result);
		
		registerOwner.assignRole(ownerRole);
		registerManager.assignRole(managerRole);
		controlBody.assignRole(controlBodyRole);
		
		// Assign permissions to newly created roles
		
		// Admin right for register owner(s)
		repository().appendAces(result, Arrays.asList(ADMINISTRATION, READ, WRITE, DELETE), new GrantedAuthoritySid(ownerRole.getName()), true);
		// Manage right for register manager(s)
		repository().appendAces(result, Arrays.asList(READ, REGISTER_MANAGE), new GrantedAuthoritySid(managerRole.getName()), true);
		// Submit right for register submitter(s)
		repository().appendAces(result, Arrays.asList(READ, REGISTER_SUBMIT), new GrantedAuthoritySid(submitterRole.getName()), true);
		// Submit right for register submitter(s)
		repository().appendAces(result, Arrays.asList(READ, REGISTER_CONTROL), new GrantedAuthoritySid(controlBodyRole.getName()), true);

		return result;
	}
	
	@Override
	public RegisterRelatedRole getManagerRole(RE_Register register) {
		Assert.notNull(register, "Register must be provided");
		return (RegisterRelatedRole)roleService.findByName(MANAGER_ROLE_PREFIX + register.getUuid().toString());
	}

	@Override
	public RegisterRelatedRole getOwnerRole(RE_Register register) {
		Assert.notNull(register, "Register must be provided");

		return (RegisterRelatedRole)roleService.findByName(OWNER_ROLE_PREFIX + register.getUuid().toString());
	}

	@Override
	public RegisterRelatedRole getSubmitterRole(RE_Register register) {
		Assert.notNull(register, "Register must be provided");

		return (RegisterRelatedRole)roleService.findByName(SUBMITTER_ROLE_PREFIX + register.getUuid().toString());
	}

	@Override
	public RegisterRelatedRole getControlBodyRole(RE_Register register) {
		Assert.notNull(register, "Register must be provided");

		return (RegisterRelatedRole)roleService.findByName(CONTROLBODY_ROLE_PREFIX + register.getUuid().toString());
	}
	
	@Override
	public void onApplicationEvent(RegistersChangedEvent event) {
		this.refreshRegisterNamesCache();
	}

	@Override
	public Map<UUID, String> getContainedItemClasses(UUID registerUuid) {
		Map<UUID, String> result = new LinkedHashMap<UUID, String>();
		
		RE_Register register = repository().findOne(registerUuid);
		for (RE_ItemClass itemClass : register.getContainedItemClasses()) {
			result.put(itemClass.getUuid(), itemClass.getName());
		}
		
		return result;
	}
}
