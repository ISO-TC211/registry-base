package de.geoinfoffm.registry.api;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import javax.jws.WebService;

import org.springframework.stereotype.Service;

import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Delegation;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.OrganizationRelatedRole;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.soap.CreateOrganizationRequest;

/**
 * Service operations for {@link Organization}s in the GDI-DE Registry.
 * 
 * @author Florian Esser
 */
@Service
@WebService
public interface OrganizationService extends ApplicationService<Organization>
{
	public Organization findByName(String name);
	
	public Organization createOrganization(CreateOrganizationRequest organization) throws UnauthorizedException;
	public Organization updateOrganization(OrganizationUpdateDTO dto) throws UnauthorizedException;

	public Delegation delegate(RegistryUser user, Role role, Organization organization) throws UnauthorizedException;
	public Delegation requestDelegation(Role role, Organization organization) throws UnauthorizedException;
	public Delegation getOrCreateDelegationRequest(RegistryUser user, Role role, Organization organization) throws UnauthorizedException;
	public Delegation findDelegation(RegistryUser user, Role role, Organization org);
	public void revokeDelegation(Delegation delegation);
	
	public OrganizationRelatedRole createOrganizationRelatedRole(String name, Organization organization);
	
	public Map<UUID, String> getAllNames();

	Collection<RegistryUser> findRoleUsers(Organization organization, Role role);

	OrganizationRelatedRole getPointOfContactRole(Organization organization);
	Collection<RegistryUser> findPointsOfContact(Organization organization);
	
	OrganizationRelatedRole getMembershipRole(Organization organization);
}
