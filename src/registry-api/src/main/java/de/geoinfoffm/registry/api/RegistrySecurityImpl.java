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

import static org.springframework.security.acls.domain.BasePermission.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Authorization;
import de.geoinfoffm.registry.core.model.Delegation;
import de.geoinfoffm.registry.core.model.DelegationRepository;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.OrganizationRepository;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalRepository;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.RoleRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.model.iso19135.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.core.security.RegistrySecurity;

public class RegistrySecurityImpl implements RegistrySecurity 
{
	@Autowired
	private MutableAclService aclService;
	
	@Autowired
	private AclPermissionEvaluator permissionEvaluator;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private SubmittingOrganizationRepository suborgRepository;

	@Autowired
	private OrganizationRepository orgRepository;

	@Autowired
	protected OrganizationService orgService;

	@Autowired
	private RegistryUserRepository userRepository;

	@Autowired
	private DelegationRepository delegationRepository;
	
	@Autowired
	private ControlBodyDiscoveryStrategy cbStrategy;

	@Autowired
	private ProposalRepository proposalRepository;

	public RegistrySecurityImpl() {
	}
	
	protected AclPermissionEvaluator permissionEvaluator() {
		return this.permissionEvaluator;
	}
	
	@Override
	public boolean isLoggedIn() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication.getPrincipal() instanceof RegistryUser) {
			return true;
		}
			
		return false;
	}
	
	@Override
	public RegistryUser getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {
			return null;
		}
		
		Object principal = authentication.getPrincipal();
		if (principal instanceof RegistryUser) {
			return (RegistryUser)principal;
		}
		else {
			Principal p = (Principal)principal;
			RegistryUser user = userRepository.findByEmailAddress(p.getName());
			if (user == null) {
				throw new RuntimeException("Current principal is not a registry user");
			}
			return user;
		}
	}
	
	@Override
	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}
	
	@Override
	public boolean isAdmin() {
		return this.hasRole(ADMIN_ROLE);
	}

	@Override
	public boolean isOwner(String entityId, String entityType) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return false;
		}
		PrincipalSid principal = new PrincipalSid(authentication);
		
		Acl acl = aclService.readAclById(new ObjectIdentityImpl(entityType, entityId));
		return principal.equals(acl.getOwner());
	}
	
	@Override
	public boolean hasRole(String role) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			for (GrantedAuthority auth : authentication.getAuthorities()) {
				if (auth.getAuthority().equals(role)) {
					return true;
				}
			}
		}

        return false;		
	}
	
	@Override
	public boolean hasAnyRole(String... roles) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			for (GrantedAuthority auth : authentication.getAuthorities()) {
				for (String role : roles) {
					if (auth.getAuthority().equals(role)) {
						return true;
					}
				}
			}
		}

        return false;				
	}

	@Override
	public boolean hasRoleWith(String rolePrefix) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			for (GrantedAuthority auth : authentication.getAuthorities()) {
				if (auth.getAuthority().startsWith(rolePrefix)) {
					return true;
				}
			}
		}

        return false;
	}

	@Override
	public boolean hasAnyRoleWith(String... rolePrefices) {
		for (String rolePrefix : rolePrefices) {
			if (hasRoleWith(rolePrefix)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean hasAnyManagementRole() {
		return hasAnyRoleStartingWith(ADMIN_ROLE, MANAGER_ROLE_PREFIX, OWNER_ROLE_PREFIX, CONTROLBODY_ROLE_PREFIX, SUBMITTER_ROLE_PREFIX, POINTOFCONTACT_ROLE_PREFIX);
	}
	
	@Override
	public boolean hasAnyRoleStartingWith(String... roles) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication != null && authentication.isAuthenticated()) {
			for (GrantedAuthority auth : authentication.getAuthorities()) {
				if (startsWithAny(auth.getAuthority(), roles)) {
					return true;
				}
			}
		}
		
		return false;		
	}
	
	@Override
	public boolean hasAnyEntityRelatedRole(List<String> rolePrefices, Entity entity) {
		for (String rolePrefix : rolePrefices) {
			if (hasRole(rolePrefix + entity.getUuid().toString())) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean hasEntityRelatedRole(String rolePrefix, UUID entityId) {
		return hasRole(rolePrefix + entityId.toString());
	}
	
	@Override
	public boolean hasEntityRelatedRole(String rolePrefix, Entity entity) {
		return hasEntityRelatedRole(rolePrefix, entity.getUuid());
	}
	
	@Override
	public <E extends Entity> boolean hasEntityRelatedRoleForAll(String rolePrefix, List<E> entities) {
		if (entities == null || entities.isEmpty()) {
			return false;
		}
		
		for (E entity : entities) {
			if (!hasEntityRelatedRole(rolePrefix, entity)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public <E extends Entity> boolean hasAnyEntityRelatedRoleForAll(List<String> rolePrefices, List<E> entities) {
		if (rolePrefices == null || rolePrefices.isEmpty() || entities == null || entities.isEmpty()) {
			return false;
		}
		
		for (E entity : entities) {
			if (!hasAnyEntityRelatedRole(rolePrefices, entity)) {
				return false;
			}
		}
		
		return true;
	}
	

	@Override
	public String entityRole(String rolePrefix, UUID entityId) {
		return rolePrefix + entityId.toString();
	}
	
	@Override
	public String submitter(UUID registerId) {
		return entityRole(SUBMITTER_ROLE_PREFIX, registerId);
	}
	
	private boolean startsWithAny(String testee, String... prefices) {
		for (String prefix : prefices) {
			if (testee.startsWith(prefix)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void assertIsLoggedIn() throws UnauthorizedException {
		if (!this.isLoggedIn()) {
			throw new UnauthorizedException("You are not authorized to access this resource.");
		}
	}
	
	@Override
	public void assertHasRole(String role) throws UnauthorizedException {
		if (!hasRole(role)) {
			throw new UnauthorizedException("You are not authorized to access this resource.");
		}
	}
	
	@Override
	public void assertHasAnyRole(String... roles) throws UnauthorizedException {
		if (!hasAnyRole(roles)) {
			throw new UnauthorizedException("You are not authorized to access this resource.");
		}
	}

	@Override
	public void assertHasRoleWith(String rolePrefix) throws UnauthorizedException {
		if (!hasRoleWith(rolePrefix)) {
			throw new UnauthorizedException("You are not authorized to access this resource.");
		}
	}

	@Override
	public void assertHasAnyRoleWith(String... rolePrefices) throws UnauthorizedException {
		if (!hasAnyRoleWith(rolePrefices)) {
			throw new UnauthorizedException("You are not authorized to access this resource.");
		}
	}

	@Override
	public void assertHasEntityRelatedRole(String rolePrefix, String entityId) throws UnauthorizedException {
		UUID entityUuid = UUID.fromString(entityId);
		assertHasEntityRelatedRole(rolePrefix, entityUuid);
	}

	@Override
	public void assertHasEntityRelatedRole(String rolePrefix, UUID entityId) throws UnauthorizedException {
		if (!hasEntityRelatedRole(rolePrefix, entityId)) {
			throw new UnauthorizedException("You are not authorized to access this resource.");
		}
	}
	
	@Override
	public void assertHasAnyEntityRelatedRole(List<String> rolePrefices, Entity entity) throws UnauthorizedException {
		if (!hasAnyEntityRelatedRole(rolePrefices, entity)) {
			throw new UnauthorizedException("You are not authorized to access this resource.");
		}
	}
	
	@Override
	public void assertHasEntityRelatedRole(String rolePrefix, Entity entity) throws UnauthorizedException {
		assertHasEntityRelatedRole(rolePrefix, entity.getUuid());
	}

	@Override
	public <E extends Entity> void assertHasEntityRelatedRoleForAll(String rolePrefix, List<E> entities) throws UnauthorizedException {
		for (E entity : entities) {
			assertHasEntityRelatedRole(rolePrefix, entity);
		}
	}
	
	@Override
	public <E extends Entity> void assertHasAnyEntityRelatedRoleForAll(List<String> rolePrefices, List<E> entities) throws UnauthorizedException {
		for (E entity : entities) {
			assertHasAnyEntityRelatedRole(rolePrefices, entity);
		}
	}
	
	@Override
	public boolean may(Permission permission, Entity entity) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return permissionEvaluator.hasPermission(authentication, entity, permission); 
	}
	
	@Override
	public boolean may(Permission permission, Class<?> entityClass, UUID entityId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return permissionEvaluator.hasPermission(authentication, entityId, entityClass.getCanonicalName(), permission); 		
	}
	
	@Override
	public boolean mayRead(Entity entity) {
		return may(READ, entity);
	}

	@Override
	public boolean mayRead(Class<?> entityClass, UUID entityId) {
		return may(READ, entityClass, entityId);
	}

	@Override
	public boolean mayWrite(Entity entity) {
		return may(WRITE, entity);
	}

	@Override
	public boolean mayWrite(Class<?> entityClass, UUID entityId) {
		return may(WRITE, entityClass, entityId);
	}

	@Override
	public boolean mayDelete(Entity entity) {
		return may(DELETE, entity);
	}

	@Override
	public boolean mayDelete(Class<?> entityClass, UUID entityId) {
		return may(DELETE, entityClass, entityId);
	}
	
	@Override
	public boolean mayActOnBehalf(UUID userUuid, UUID organizationUuid, String roleName) {
		Role role = roleRepository.findByName(roleName);
		RE_SubmittingOrganization suborg = suborgRepository.findOne(organizationUuid);
		Organization org = orgRepository.findBySubmittingOrganization(suborg);
		RegistryUser user = userRepository.findOne(userUuid); 
		return mayActOnBehalf(user, org, role);
	}

	@Override
	public boolean mayActOnBehalf(Organization organization, Role role) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		
		if (!(principal instanceof RegistryUser)) {
			return false;
		}
		
		RegistryUser currentUser = (RegistryUser)principal;
		
		return mayActOnBehalf(currentUser, organization, role);
	}
	
	@Override
	public boolean mayActOnBehalf(RegistryUser user, Organization organization, Role role) {
		Delegation check = new Delegation(user, role, organization);

		for (Delegation delegation : delegationRepository.findByDelegatingOrganization(organization)) {
			if (delegation.isSame(check)) {
				return delegation.isApproved();
			}
		}
		
		return false;
	}

	@Override
	public void assertMay(Permission permission, Entity entity) throws UnauthorizedException {
		if (!may(permission, entity)) { 
			throw new UnauthorizedException("You are not authorized to access this resource.");
		}
	}
	
	@Override
	public void assertMayRead(Entity entity) throws UnauthorizedException {
		assertMay(READ, entity);
	}
	
	@Override
	public void assertMayWrite(Entity entity) throws UnauthorizedException {
		assertMay(WRITE, entity);
	}
	
	@Override
	public void assertMayDelete(Entity entity) throws UnauthorizedException {
		assertMay(DELETE, entity);
	}
	
	@Override
	public void assertMayAdmin(Entity entity) throws UnauthorizedException {
		assertMay(ADMINISTRATION, entity);
	}
	
	@Override
	public void assertIsAdmin() throws UnauthorizedException {
		if (!isAdmin()) {
			throw new UnauthorizedException("You are not authorized to access this resource.");
		}
	}
	
	@Override
	public boolean isDelegated(List<Delegation> delegations, UUID userUuid, UUID organizationUuid, String roleName) {
		for (Delegation delegation : delegations) {
			if (delegation.getUser().getUuid().equals(userUuid) && delegation.getDelegatingOrganization().getUuid().equals(organizationUuid) && delegation.getRole().getName().equals(roleName)) {
				return delegation.isApproved();
			}
		}
		
		return false;
	}
	
	@Override
	public boolean isDelegationRequested(List<Delegation> delegations, UUID userUuid, UUID organizationUuid, String roleName) {
		if (delegations == null) {
			return false;
		}
		
		for (Delegation delegation : delegations) {
			if (delegation.getUser().getUuid().equals(userUuid) && delegation.getDelegatingOrganization().getUuid().equals(organizationUuid) && delegation.getRole().getName().equals(roleName)) {
				return !delegation.isApproved();
			}
		}
		
		return false;		
	}

	@Override
	@Transactional
	public Delegation requestDelegation(UUID userUuid, UUID organizationUuid, String roleName) throws UnauthorizedException {
		Organization org = orgRepository.findOne(organizationUuid);

		Role role = roleRepository.findByName(roleName);
		if (role == null) {
			return null;
		}
		
		return orgService.requestDelegation(role, org);
	}

	@Override
	@Transactional
	public void denyDelegationRequest(UUID userUuid, UUID organizationUuid, String roleName) {
		RegistryUser user = userRepository.findOne(userUuid);
		Organization org = orgRepository.findOne(organizationUuid);
		
		if (user != null && org != null) {
			List<Delegation> delegations = delegationRepository.findByActorAndDelegatingOrganizationAndIsApprovedFalse(user, org);
			for (Delegation delegation : delegations) {
				if (delegation.getRole().getName().equals(roleName)) {
					org.removeAuthorization(delegation);
					user.removeAuthorization(delegation);
					delegationRepository.delete(delegation);
				}
			}
		}
	}

	@Override
	public String getOpenDelegationRequestCount(UUID userUuid) {
		RegistryUser user = userRepository.findOne(userUuid);
		if (user == null) {
			return null;
		}
		
		List<Delegation> openDelegations = delegationRepository.findByActorAndDelegatingOrganizationAndIsApprovedFalse(user, user.getOrganization());
		
		if (openDelegations.isEmpty()) {
			return null;
		}
		else {
			return Integer.toString(openDelegations.size());
		}
	}
	
	@Override
	public String getPointOfContactTodoCount() {
		RegistryUser currentUser = this.getCurrentUser();
		
		if (!hasRole(POINTOFCONTACT_ROLE_PREFIX + currentUser.getOrganization().getUuid().toString())) {
			return null;
		}
		else {
			int openDelegations = delegationRepository.findByDelegatingOrganizationAndIsApprovedFalse(currentUser.getOrganization()).size();
			if (openDelegations > 0) {
				return Integer.toString(openDelegations);
			}
			else {
				return null;
			}
		}
	}

	@Override
	public String getRegisterManagerTodoCount() {
		RegistryUser currentUser = this.getCurrentUser();
		
		if (!hasAnyRoleWith(MANAGER_ROLE_PREFIX)) {
			return null;
		}
		else {
			RE_SubmittingOrganization sponsor = currentUser.getOrganization().getSubmittingOrganization();
			List<Proposal> proposals = proposalRepository.findBySponsorAndStatusAndDateSubmittedIsNotNullAndGroupIsNullAndIsConcludedIsFalse(sponsor, Proposal.STATUS_UNDER_REVIEW);
			if (!proposals.isEmpty()) {
				return Integer.toString(proposals.size());
			}
			else {
				return null;
			}
		}
	}

	@Override
	public String getControlBodyTodoCount() {
		if (!hasAnyRoleWith(CONTROLBODY_ROLE_PREFIX)) {
			return null;
		}
		else {
			List<Proposal> proposals = proposalRepository.findByStatusAndGroupIsNull(Proposal.STATUS_IN_APPROVAL_PROCESS);
			if (!proposals.isEmpty()) {
				return Integer.toString(proposals.size());
			}
			else {
				return null;
			}
		}
	}

	@Override
	public boolean isControlBody(UUID proposalUuid) {
		Proposal proposal = proposalRepository.findOne(proposalUuid);
		Assert.notNull(proposal);
		
		for (Authorization auth : cbStrategy.findControlBodyAuthorizations(proposal)) {
			if (hasRole(auth.getRole().getName())) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void assertIsTrue(boolean expression) throws UnauthorizedException {
		if (!expression) {
			throw new UnauthorizedException("You are not authorized to access this resource.");
		}
	}

	@Override
	public boolean maySubmitTo(UUID targetRegisterUuid) {
		return hasEntityRelatedRole(SUBMITTER_ROLE_PREFIX, targetRegisterUuid);
	}

	@Override
	public void assertMaySubmitTo(UUID targetRegisterUuid) throws UnauthorizedException {
		assertIsTrue(maySubmitTo(targetRegisterUuid));
	}

}
