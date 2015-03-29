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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bespire.registry.core.ProposalRelatedRole;
import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.OrganizationRelatedRole;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.RegisterRelatedRole;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.RoleRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@Service
public class RoleServiceImpl extends AbstractApplicationService<Role, RoleRepository> implements RoleService 
{
	@Autowired
	private MutableAclService mutableAclService;

	@Autowired
	public RoleServiceImpl(RoleRepository repository) {
		super(repository);
	}

	@Override
	@Transactional
	public Role getOrCreateRole(String name) {
		Role role = repository().findByName(name); 
		if (role == null) {
			new Role(name);
			role = repository().save(role);
		}
		
		return role;
	}

	@Override
	@Transactional(readOnly = true)
	public Role findByName(String name) {
		return repository().findByName(name);
	}

	@Override
	@Transactional
	public RegisterRelatedRole getOrCreateRole(String name, RE_Register register) {
		return this.getOrCreateEntityRelatedRole(name, RegisterRelatedRole.class, register);
	}
	
	@Override
	@Transactional
	public OrganizationRelatedRole getOrCreateRole(String name, Organization organization) {
		return this.getOrCreateEntityRelatedRole(name, OrganizationRelatedRole.class, organization);
	}
	
	@Override
	@Transactional
	public ProposalRelatedRole getOrCreateRole(String name, Proposal proposal) {
		return this.getOrCreateEntityRelatedRole(name, ProposalRelatedRole.class, proposal);
	}
	
	@Transactional
	private <R extends Role, T extends Entity> R getOrCreateEntityRelatedRole(String name, Class<R> roleType, T entity) {
		Role role = repository().findByName(name);
		if (role == null) {
			try {
				role = (R)ConstructorUtils.invokeConstructor(roleType, new Object[] { name,  entity }, new Class<?>[] { String.class, entity.getClass() });
			} 
			catch (BeanInstantiationException | NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
//			role = repository().save(role);
		}
		else {
			if (!roleType.isAssignableFrom(role.getClass())) {
				throw new RuntimeException(String.format("Role '%s' already exists but has type '%s'", name, role.getClass().getCanonicalName()));
			}
		}
		
		return (R)role;		
	}
}
