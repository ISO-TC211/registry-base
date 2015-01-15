package de.geoinfoffm.registry.api;

import static de.geoinfoffm.registry.core.security.RegistrySecurity.*;
import static org.springframework.security.acls.domain.BasePermission.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.jws.WebService;

import org.isotc211.iso19135.RE_SubmittingOrganization_Type;
import org.isotc211.iso19139.metadata.CI_ResponsibleParty_Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Authorization;
import de.geoinfoffm.registry.core.model.AuthorizationRepository;
import de.geoinfoffm.registry.core.model.Delegation;
import de.geoinfoffm.registry.core.model.DelegationRepository;
import de.geoinfoffm.registry.core.model.DelegationRequestedEvent;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.OrganizationRelatedRole;
import de.geoinfoffm.registry.core.model.OrganizationRepository;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.RoleRepository;
import de.geoinfoffm.registry.core.model.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.core.model.iso19115.CI_ResponsibleParty;
import de.geoinfoffm.registry.core.model.iso19115.CI_RoleCode;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.security.RegistryPermission;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;
import de.geoinfoffm.registry.persistence.ResponsiblePartyRepository;
import de.geoinfoffm.registry.soap.CreateOrganizationRequest;

/**
 * Service operations for {@link Organization}s in the GDI-DE Registry.
 * 
 * @author Florian Esser
 */
@Service
@WebService(endpointInterface = "de.bund.bkg.gdide.registry.ws.Organization")
@ItemClassService("Organization")
public class OrganizationServiceImpl 
extends AbstractApplicationService<Organization, OrganizationRepository>
implements OrganizationService
{
	@Autowired
	private SubmittingOrganizationRepository suborgRepository;
	
	@Autowired
	private RegistryUserRepository userRepository;
	
	@Autowired
	private RegisterRepository registerRepository;
	
	@Autowired
	private RegisterService registerService;

	@Autowired
	private ItemClassRepository itemClassRepository;

	@Autowired
	private ResponsiblePartyRepository partyRepository;
	
	@Autowired
	private SubmittingOrganizationRepository submittingOrgRepository;
	
	@Autowired
	private RegisterItemService itemService;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private MutableAclService mutableAclService;
	
	@Autowired
	private AuthorizationRepository authRepository;
	
	@Autowired
	private DelegationRepository delegationRepository;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ProposalService proposalService;
	
	@Autowired
	private RegistrySecurity security;
	
	@Autowired
	public OrganizationServiceImpl(OrganizationRepository repository) {
		super(repository);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Organization findByName(String name) {
		return repository().findByName(name);
	}

	@Override
	@Transactional
	public Organization createOrganization(CreateOrganizationRequest organization) throws UnauthorizedException {
		security.assertHasAnyRoleWith(ADMIN_ROLE);

		RE_SubmittingOrganization suborg;
		if (!organization.isSetSubmittingOrganization()) {
			CI_ResponsibleParty party = new CI_ResponsibleParty(
					"NN",
					null,
					null,
					CI_RoleCode.USER);
			suborg = new RE_SubmittingOrganization(organization.getName(), party);
		}
		else if (!organization.getSubmittingOrganization().isSetUuidref()) {
			RE_SubmittingOrganization_Type suborgValue = organization.getSubmittingOrganization().getRE_SubmittingOrganization();
			CI_ResponsibleParty_Type partyValue = suborgValue.getContact().getCI_ResponsibleParty();

			CI_ResponsibleParty party = new CI_ResponsibleParty(
					partyValue.getIndividualName().getCharacterString().getValue().toString(),
					partyValue.getOrganisationName().getCharacterString().getValue().toString(),
					partyValue.getPositionName().getCharacterString().getValue().toString(),
					CI_RoleCode.USER);

			suborg = new RE_SubmittingOrganization(suborgValue.getName().getCharacterString().getValue().toString(), party);
			suborg = suborgRepository.saveAndFlush(suborg);
		}
		else {
			suborg = suborgRepository.findOne(UUID.fromString(organization.getSubmittingOrganization().getUuidref()));
		}
		
		Organization org = new Organization(organization.getName(), suborg);
		
		org = repository().save(org);
		
		// Create point of contact role for new organization
		Role pocRole = this.createOrganizationRelatedRole(POINTOFCONTACT_ROLE_PREFIX + org.getUuid().toString(), org);
		repository().appendAces(org, Arrays.asList(READ, WRITE, ADMINISTRATION), new GrantedAuthoritySid(pocRole.getName()), true);

		// Create membership role for new organization
		Role memberRole = this.createOrganizationRelatedRole(ORGANIZATIONMEMBER_ROLE_PREFIX + org.getUuid().toString(), org);
		repository().appendAce(org, READ, new GrantedAuthoritySid(pocRole.getName()), true);

		return org;
	}
	
	@Override
	@Transactional
	public Organization updateOrganization(OrganizationUpdateDTO org) throws UnauthorizedException {
		if (org.getUuid() == null) {
			throw new NullPointerException("Cannot update organization with null UUID");
		}
		
		Organization current = repository().findOne(org.getUuid());
		if (current == null) {
			throw new NullPointerException("Cannot update non-existent organization");
		}
		
		security.assertMayWrite(current);
		
		if (org.getName() != null && !org.getName().isEmpty()) {
			current.setName(org.getName());
		}

		// Organization role changes may only be performed by administrators (not by PoCs!) 
		if (security.isAdmin()) {
			List<Authorization> removedAuths = new ArrayList<Authorization>();
			if (org.getRoles() != null) { 
				for (Authorization auth : current.getAuthorizations()) {
	//				if ((auth.getRole() instanceof OrganizationRelatedRole) && ((OrganizationRelatedRole)auth.getRole()).getOrganization().getUuid().equals(org.getUuid())) {
	//					// Skip OrganizationRelatedRoles that concern the organization itself,
	//					// as those should never be removed (e.g. ROLE_POINTOFCONTACT)
	//					continue;
	//				}
					
					if (!org.getRoles().contains(auth.getRole().getName())) {
						removedAuths.add(auth);
					}
				}
				
				for (Authorization auth : removedAuths) {
					current.removeAuthorization(auth);
					authRepository.delete(auth);
				}
	
				for (String roleName : org.getRoles()) {
					Role role = roleRepository.findByName(roleName);
					if (role != null) {
						Authorization auth = authRepository.findByActorAndRole(current, role);
						if (auth == null) {
							auth = new Authorization(current, role);
							current.addAuthorization(auth);
						}
					}
				}
			}
		}

		return repository().save(current);
	}
	
	
	@Override
	public Map<UUID, String> getAllNames() {
		Map<UUID, String> result = new HashMap<UUID, String>();
		
		List<Object[]> objects = repository().getOrganizationNames();
		
		for (Object[] object : objects) {
			result.put((UUID)object[0], object[1].toString());
		}
		
		return result;
	}

	@Override
	public Delegation delegate(RegistryUser user, Role role, Organization organization) throws UnauthorizedException {
		if (!security.isAdmin() && !security.mayActOnBehalf(organization, this.getPointOfContactRole(organization))) {
			throw new UnauthorizedException(String.format("You are not allowed to delegate role '%s' on behalf of organization '%s'", role.getName(), organization.getName()));
		}

		Delegation delegation = this.getOrCreateDelegationRequest(user, role, organization);
		delegation.setApproved(true); // Delegation by Admin or PoC is approved automatically

		return delegationRepository.save(delegation);
	}

	@Override
	public Delegation requestDelegation(Role role, Organization organization) throws UnauthorizedException {
		RegistryUser currentUser = security.getCurrentUser();
		
		return this.getOrCreateDelegationRequest(currentUser, role, organization);
	}

	@Override
	public Delegation getOrCreateDelegationRequest(RegistryUser user, Role role, Organization organization) throws UnauthorizedException {
		Delegation delegation = findDelegation(user, role, organization); 
		if (delegation == null) {
			delegation = new Delegation(user, role, organization);
		}
		else {
			return delegation; // nothing to do here, delegation already created
		}

		boolean orgIsAuthorized = false;
		for (Authorization orgAuth : organization.getAuthorizations()) {
			if (orgAuth.getRole().equals(role)) {
				orgIsAuthorized = true;
			}
		}
		
		if (!orgIsAuthorized) {
			if (role instanceof OrganizationRelatedRole) {
				OrganizationRelatedRole orgRole = (OrganizationRelatedRole)role;
				if (orgRole.getOrganization().equals(organization)) {
					// An Organization may delegate all OrganizationRelatedRoles
					// relating to itself
					orgIsAuthorized = true;
				}
			}
		}
			
		if (!orgIsAuthorized) {
			throw new UnauthorizedException(String.format("The organization cannot delegate role %s", role.getName()));
		}

		delegation = delegationRepository.save(delegation);

//		if (role instanceof RegisterRelatedRole) {
//			RE_Register register = ((RegisterRelatedRole)role).getRegister();
//			Permission permission = extractPermission(role);
//			
//			registerRepository.appendAce(register, permission, new PrincipalSid(user.getUuid().toString()), false);
//		}
		
		delegationRepository.appendAces(delegation, Arrays.asList(ADMINISTRATION, READ, WRITE, DELETE), new PrincipalSid(organization.getUuid().toString()), true);
		
		eventPublisher().publishEvent(new DelegationRequestedEvent(delegation));
		
		return delegation;
	}

	protected Permission extractPermission(Role role) {
		Permission permission;
		if (role.getName().startsWith(MANAGER_ROLE_PREFIX)) {
			permission = RegistryPermission.REGISTER_MANAGE;
		}
		else if (role.getName().startsWith(CONTROLBODY_ROLE_PREFIX)) {
			permission = RegistryPermission.REGISTER_CONTROL;
		}
		else if (role.getName().startsWith(OWNER_ROLE_PREFIX)) {
			permission = RegistryPermission.ADMINISTRATION;
		}
		else if (role.getName().startsWith(SUBMITTER_ROLE_PREFIX)) {
			permission = RegistryPermission.REGISTER_SUBMIT;
		}
		else {
			throw new RuntimeException(String.format("Cannot delegate role %s", role.getName()));
		}
		return permission;
	}

	@Transactional
	@Override
	public void revokeDelegation(Delegation delegation) {
//		if (delegation.getRole() instanceof RegisterRelatedRole) {
//			RE_Register register = ((RegisterRelatedRole)delegation.getRole()).getRegister();
//			Permission permission = extractPermission(delegation.getRole());
//			registerRepository.deleteAce(register, permission, new PrincipalSid(delegation.getUser().getUuid().toString()));
//		}
		delegation.getUser().removeAuthorization(delegation);
		delegationRepository.delete(delegation);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Delegation findDelegation(RegistryUser user, Role role, Organization org) {
		Delegation check = new Delegation(user, role, org);
		
		List<Delegation> delegations = delegationRepository.findByDelegatingOrganization(org);
		for (Delegation delegation : delegations) {
			if (delegation.isSame(check)) {
				return delegation;
			}
		}
		
		return null;
	}

	@Override
	public OrganizationRelatedRole createOrganizationRelatedRole(String name, Organization organization) {
		OrganizationRelatedRole role = new OrganizationRelatedRole(name, organization);
		roleRepository.save(role);
		
		return role;
	}
	
	@Override
	public OrganizationRelatedRole getPointOfContactRole(Organization organization) {
		Assert.notNull(organization, "Organization must be provided");
		return (OrganizationRelatedRole)roleService.findByName(POINTOFCONTACT_ROLE_PREFIX + organization.getUuid().toString());
	}

	@Override
	public OrganizationRelatedRole getMembershipRole(Organization organization) {
		Assert.notNull(organization, "Organization must be provided");
		return (OrganizationRelatedRole)roleService.findByName(ORGANIZATIONMEMBER_ROLE_PREFIX + organization.getUuid().toString());
	}

	@Override
	public Collection<RegistryUser> findPointsOfContact(Organization organization) {
		Role pocRole = this.getPointOfContactRole(organization);
		return this.findRoleUsers(organization, pocRole);
	}

	@Override
	public Collection<RegistryUser> findRoleUsers(Organization organization, Role role) {
		List<RegistryUser> result = new ArrayList<RegistryUser>();
		
		List<Delegation> pocDelegations = delegationRepository.findByDelegatingOrganizationAndRole(organization, role);
		
		for (Delegation delegation : pocDelegations) {
			if ((delegation.getUser() instanceof RegistryUser) && delegation.isApproved()) {
				result.add((RegistryUser)delegation.getUser());
			}
		}
				
		return result;
	}
}
