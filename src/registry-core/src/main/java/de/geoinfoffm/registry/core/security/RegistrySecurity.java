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
package de.geoinfoffm.registry.core.security;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.acls.model.Permission;

import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Delegation;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

public interface RegistrySecurity
{
	public static final String ADMIN_ROLE = "ROLE_ADMIN";

	public static final String OWNER_ROLE_PREFIX = "ROLE_OWNER_";
	public static final String MANAGER_ROLE_PREFIX = "ROLE_MANAGER_";
	public static final String SUBMITTER_ROLE_PREFIX = "ROLE_SUBMITTER_";
	public static final String CONTROLBODY_ROLE_PREFIX = "ROLE_CONTROLBODY_";
	public static final String ORGANIZATIONMEMBER_ROLE_PREFIX = "ROLE_ORGANIZATIONMEMBER_";
	public static final String POINTOFCONTACT_ROLE_PREFIX = "ROLE_POINTOFCONTACT_";

	String getOpenDelegationRequestCount(UUID userUuid);

	void denyDelegationRequest(UUID userUuid, UUID organizationUuid, String roleName);

	Delegation requestDelegation(UUID userUuid, UUID organizationUuid, String roleName) throws UnauthorizedException;

	boolean isDelegationRequested(List<Delegation> delegations, UUID userUuid, UUID organizationUuid, String roleName);

	boolean isDelegated(List<Delegation> delegations, UUID userUuid, UUID organizationUuid, String roleName);

	boolean isControlBody(UUID proposalUuid);
	
	boolean maySubmitTo(UUID targetRegisterUuid);
	boolean maySubmitTo(RE_Register register);
	boolean maySubmitToAll(Collection<RE_Register> registers);
	boolean isSubmitter();
	
	void assertMaySubmitTo(UUID targetRegisterUuid) throws UnauthorizedException;
	void assertMaySubmitTo(RE_Register register) throws UnauthorizedException;
	void assertMaySubmitToAll(RE_Register... register) throws UnauthorizedException;
	void assertIsSubmitter() throws UnauthorizedException;

	boolean mayAppeal(Proposal proposal);
	void assertMayAppeal(Proposal proposal) throws UnauthorizedException;

	void assertIsAdmin() throws UnauthorizedException;	
	void assertMayAdmin(Entity entity) throws UnauthorizedException;

	void assertMayDelete(Entity entity) throws UnauthorizedException;

	void assertMayWrite(Entity entity) throws UnauthorizedException;

	void assertMayRead(Entity entity) throws UnauthorizedException;

	void assertMay(Permission permission, Entity entity) throws UnauthorizedException;

	boolean mayActOnBehalf(RegistryUser user, Organization organization, Role role);

	boolean mayActOnBehalf(Organization organization, Role role);

	boolean mayActOnBehalf(UUID userUuid, UUID organizationUuid, String roleName);

	boolean mayDelete(Class<?> entityClass, UUID entityId);

	boolean mayDelete(Entity entity);

	boolean mayWrite(Class<?> entityClass, UUID entityId);

	boolean mayWrite(Entity entity);

	boolean mayRead(Class<?> entityClass, UUID entityId);

	boolean mayRead(Entity entity);

	boolean may(Permission permission, Class<?> entityClass, UUID entityId);

	boolean may(Permission permission, Entity entity);

	<E extends Entity> void assertHasAnyEntityRelatedRoleForAll(List<String> rolePrefices, List<E> entities)
			throws UnauthorizedException;

	<E extends Entity> void assertHasEntityRelatedRoleForAll(String rolePrefix, List<E> entities)
			throws UnauthorizedException;

	void assertHasEntityRelatedRole(String rolePrefix, Entity entity) throws UnauthorizedException;

	void assertHasAnyEntityRelatedRole(List<String> rolePrefices, Entity entity) throws UnauthorizedException;

	void assertHasEntityRelatedRole(String rolePrefix, UUID entityId) throws UnauthorizedException;

	void assertHasEntityRelatedRole(String rolePrefix, String entityId) throws UnauthorizedException;

	void assertHasAnyRoleWith(String... rolePrefices) throws UnauthorizedException;

	void assertHasRoleWith(String rolePrefix) throws UnauthorizedException;

	void assertHasAnyRole(String... roles) throws UnauthorizedException;

	void assertHasRole(String role) throws UnauthorizedException;

	void assertIsLoggedIn() throws UnauthorizedException;

	void assertIsTrue(boolean expression) throws UnauthorizedException;

	String submitter(UUID registerId);

	String entityRole(String rolePrefix, UUID entityId);

	<E extends Entity> boolean hasAnyEntityRelatedRoleForAll(List<String> rolePrefices, List<E> entities);
	<E extends Entity> boolean hasEntityRelatedRoleForAny(String rolePrefix, Collection<E> entities);

	<E extends Entity> boolean hasEntityRelatedRoleForAll(String rolePrefix, List<E> entities);

	boolean hasEntityRelatedRole(String rolePrefix, Entity entity);

	boolean hasEntityRelatedRole(String rolePrefix, UUID entityId);

	boolean hasAnyEntityRelatedRole(List<String> rolePrefices, Entity entity);

	boolean hasAnyRoleStartingWith(String... roles);

	boolean hasAnyManagementRole();

	boolean hasAnyRoleWith(String... rolePrefices);

	boolean hasRoleWith(String rolePrefix);

	boolean hasAnyRole(String... roles);

	boolean hasRole(String role);

	boolean isOwner(String entityId, String entityType);

	boolean isAdmin();

	void logout();

	RegistryUser getCurrentUser();

	boolean isLoggedIn();

	public abstract String getPointOfContactTodoCount();
	public abstract String getRegisterManagerTodoCount();
	public abstract String getControlBodyTodoCount();
}
