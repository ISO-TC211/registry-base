package de.geoinfoffm.registry.core.security;

import java.util.List;
import java.util.UUID;

import org.springframework.security.acls.model.Permission;

import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Delegation;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.Role;

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

	String submitter(UUID registerId);

	String entityRole(String rolePrefix, UUID entityId);

	<E extends Entity> boolean hasAnyEntityRelatedRoleForAll(List<String> rolePrefices, List<E> entities);

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
