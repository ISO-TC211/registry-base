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

import static de.geoinfoffm.registry.core.security.RegistrySecurity.*;
import static de.geoinfoffm.registry.core.security.RegistryPermission.*;
import static org.springframework.security.acls.domain.BasePermission.*;

import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.bespire.LoggerFactory;
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
 * @author Florian Esser
 *
 */
@Transactional @Service
public class RegisterServiceImpl
extends AbstractApplicationService<RE_Register, RegisterRepository> 
implements RegisterService, ApplicationListener<RegistersChangedEvent>
{
	private static final Logger logger = LoggerFactory.make();
	
	@Autowired
	private RoleService roleService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
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
		entityManager.persist(owner);
		
		RE_RegisterManager manager = new RE_RegisterManager();
		manager.setName(registerManager.getName());
		CI_ResponsibleParty managerContact = new CI_ResponsibleParty(manager.getName(), registerManager.getName(), "Register manager", CI_RoleCode.CUSTODIAN);
		manager.setContact(managerContact);
		result.setManager(manager);
		entityManager.persist(manager);
		
		if (setter != null) {
			setter.run(result);
		}
		
		result = repository().save(result);

		// Create roles for new register
		Role managerRole = roleService.getOrCreateRole(MANAGER_ROLE_PREFIX + result.getUuid().toString(), result);
		Role ownerRole = roleService.getOrCreateRole(OWNER_ROLE_PREFIX + result.getUuid().toString(), result);
		Role submitterRole = roleService.getOrCreateRole(SUBMITTER_ROLE_PREFIX + result.getUuid().toString(), result);
		Role controlBodyRole = roleService.getOrCreateRole(CONTROLBODY_ROLE_PREFIX + result.getUuid().toString(), result);
		
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
		RE_Register register = repository().findOne(registerUuid);
		if (register == null) {
			logger.error("No register with UUID {} exists", registerUuid);
			return new HashMap<>();
		}
		
		return this.getContainedItemClasses(register);
	}
	
	public Map<UUID, String> getContainedItemClasses(RE_Register register) {
		Map<UUID, String> result = new LinkedHashMap<UUID, String>();

		for (RE_ItemClass itemClass : register.getContainedItemClasses()) {
			result.put(itemClass.getUuid(), itemClass.getName());
		}
		
		return result;
	}
	
	@Override
	public boolean containsItemClass(RE_Register register, String itemClassName) {
		return this.getContainedItemClasses(register).containsValue(itemClassName);
	}
}
