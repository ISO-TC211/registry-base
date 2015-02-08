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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.geoinfoffm.registry.api.soap.CreateOrganizationRequest;
import de.geoinfoffm.registry.api.soap.CreateRegistryUserRequest;
import de.geoinfoffm.registry.core.RegistryUserCreatedEvent;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;
import de.geoinfoffm.registry.core.model.Authorization;
import de.geoinfoffm.registry.core.model.AuthorizationRepository;
import de.geoinfoffm.registry.core.model.Delegation;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.OrganizationRepository;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.RoleRepository;
import de.geoinfoffm.registry.core.model.iso19135.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;
import de.geoinfoffm.registry.persistence.ResponsiblePartyRepository;

/**
 * Service operations for {@link RegistryUser}s in the GDI-DE Registry.
 * 
 * @author Florian Esser
 */
@Transactional
@Service
public class RegistryUserServiceImpl 
extends AbstractApplicationService<RegistryUser, RegistryUserRepository> 
implements RegistryUserService
{
	@Autowired
	private OrganizationRepository orgRepository;

	@Autowired
	private OrganizationService orgService;

	@Autowired
	private SubmittingOrganizationRepository suborgRepository;
	
	@Autowired
	private RegistryUserRepository userRepository;
	
	@Autowired
	private RegisterRepository registerRepository;

	@Autowired
	private AuthorizationRepository authRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
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
	private RegistryConfiguration registryConfiguration;

	private boolean sendConfirmationMails = false;

	@Autowired
	public RegistryUserServiceImpl(RegistryUserRepository userRepository) {
		super(userRepository);
	}

	public boolean isSendConfirmationMails() {
		return sendConfirmationMails;
	}

	public void setSendConfirmationMails(boolean sendConfirmationMails) {
		this.sendConfirmationMails = sendConfirmationMails;
	}

	@Override
	public Locale getLocaleForLanguageTag(String languageTag) {
		if (languageTag == null) {
			return null;
		}
		
		return Locale.forLanguageTag(languageTag);
	}

	/* (non-Javadoc)
	 * @see de.bund.bkg.gdide.registry.api.RegistryUserService#registerUser(de.bund.bkg.gdide.registry.api.RegistryUserRegistration)
	 */
	@Override
	public RegistryUser registerUser(CreateRegistryUserRequest user) throws UserRegistrationException, UnauthorizedException {
		RegistryUser result;
		
		if (user == null) {
			throw new NullPointerException("Cannot register null user");
		}
		
		if (user.getName() == null || user.getName().isEmpty()) {
			throw new UserRegistrationException("User name must not be empty");			
		}
		
		if (user.getEmailAddress() == null || user.getEmailAddress().isEmpty()) {
			throw new UserRegistrationException("User e-mail address must not be empty");			
		}
		
		if (!isEmailAddressAvailable(user.getEmailAddress())) {
			throw new UserRegistrationException("The e-mail address is already used by another user");			
		}

		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			throw new UserRegistrationException("User password must not be empty");
		}
		
		if (user.getOrganizationUuid() == null || user.getOrganizationUuid().isEmpty()) {
			throw new UserRegistrationException("Organization UUID must not be empty");			
		}
		UUID organizationUuid = UUID.fromString(user.getOrganizationUuid());
		Organization organization = orgRepository.findOne(organizationUuid);
		
		if (user.getPreferredLanguage() == null || user.getPreferredLanguage().isEmpty()) {
			throw new UserRegistrationException("User preferred language must not be empty");
		}
		Locale preferredLanguage = this.getLocaleForLanguageTag(user.getPreferredLanguage());
		
		RegistryUser registryUser = new RegistryUser(user.getName(), user.getEmailAddress(), user.getPassword(), 
				user.getPreferredLanguage(), organization, user.isActive());
		
		if (this.isSendConfirmationMails()) {
			registryUser.setConfirmationToken(UUID.randomUUID());
		}
		
		if (user.getRole() != null) {
			for (String roleName : user.getRole()) {
				Role role = roleRepository.findByName(roleName);
				if (role != null) {
					Authorization auth = new Authorization(registryUser, role);
					registryUser.addAuthorization(auth);
				}
			}
		}
		
		if (registryConfiguration.isAdministrator(user.getEmailAddress())) {
			Role role = roleRepository.findByName(RegistrySecurityImpl.ADMIN_ROLE);
			Authorization auth = new Authorization(registryUser, role);
			registryUser.addAuthorization(auth);
		}
		
		result = repository().save(registryUser);
		
		orgService.getOrCreateDelegationRequest(registryUser, orgService.getMembershipRole(organization), organization);
		
		RegistryUserCreatedEvent event = new RegistryUserCreatedEvent(result);
		if (this.isSendConfirmationMails()) {
			event.annotate(RegistryUserService.SEND_CONFIRMATION_MAIL);
		}
		eventPublisher().publishEvent(event);
		
		return result;
	}
	
	@Override
	@Transactional
	public RegistryUser registerUser(CreateRegistryUserRequest request, CreateOrganizationRequest newOrganizationDetails)
			throws UserRegistrationException, UnauthorizedException {

		RegistryUser result;
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
			Authentication authentication = new RunAsUserToken(UUID.randomUUID().toString(), "SYSTEM", "N/A", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")), auth.getClass()); 
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		try {
			Organization org = orgService.createOrganization(newOrganizationDetails);
			if (org == null) {
				throw new UserRegistrationException("Unable to create organization");
			}
			request.setOrganizationUuid(org.getUuid().toString());
	
			result = this.registerUser(request);
			
			orgService.delegate(result, orgService.getMembershipRole(org), org);
			orgService.delegate(result, orgService.getPointOfContactRole(org), org);
			repository().appendAces(result, Arrays.asList(ADMINISTRATION, READ, WRITE, DELETE), new PrincipalSid(result.getUuid().toString()), true);
		}
		finally {
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		
//		MutableAcl orgAcl = (MutableAcl)mutableAclService.readAclById(new ObjectIdentityImpl(org), null);
//		// Allow user to manage newly created organization
//		orgAcl.insertAce(orgAcl.getEntries().size(), RegistryPermission.ADMINISTRATION, new PrincipalSid(result.getUuid().toString()), true);

		return result;
	}

	@Override
	public RegistryUser updateUser(RegistryUserUpdateDTO user) throws UpdateUserException {
		if (user.getUuid() == null) {
			throw new NullPointerException("Cannot update user with null UUID");
		}
		
		RegistryUser current = repository().findOne(user.getUuid());
		if (current == null) {
			throw new NullPointerException("Cannot update non-existent user");
		}
		
		if (user.getName() != null) {
			current.setName(user.getName());
		}
		
		if (user.getEmailAddress() != null) {
			current.setEmailAddress(user.getEmailAddress());
		}
		
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			current.setPassword(user.getPassword());
		}
		
		if (user.getPreferredLanguage() != null && !user.getPreferredLanguage().equals(current.getPreferredLanguage())) {
			current.setPreferredLanguage(user.getPreferredLanguage());
		}
		
		if (user.isActive() != null && user.isActive().booleanValue() != current.isActive()) {
			current.setActive(user.isActive().booleanValue());
		}

		if (user.getRoles() != null) {
			List<Authorization> obsoleteAuths = new ArrayList<Authorization>();

			for (Authorization auth : current.getAuthorizations()) {
				if (!(auth instanceof Delegation) && !user.getRoles().contains(auth.getRole().getName())) {
					obsoleteAuths.add(auth);
				}
			}

//			for (Authorization auth : obsoleteAuths) {
//				current.removeAuthorization(auth);
//				authRepository.delete(auth);
//			}

			for (String roleName : user.getRoles()) {
				Role role = roleRepository.findByName(roleName);
				if (role != null) {
					Authorization auth = new Authorization(current, role);
					current.addAuthorization(auth);
				}
			}
		}
	
		return repository().save(current);
	}

	/* (non-Javadoc)
	 * @see de.bund.bkg.gdide.registry.api.RegistryUserService#isEmailAddressAvailable(java.lang.String)
	 */
	@Override
	public boolean isEmailAddressAvailable(String emailAddress) {
		return repository().findByEmailAddressIgnoreCase(emailAddress) == null;
	}

	@Override
	public RegistryUser findByEmailAddress(String emailAddress) {
		return userRepository.findByEmailAddressIgnoreCase(emailAddress);
	}
	
	@Override
	public boolean confirmUser(String emailAddress, UUID token) {
		RegistryUser konfirmant = repository().findByEmailAddressIgnoreCase(emailAddress);
		
		if (konfirmant == null) {
			return false;
		}
		
		if (konfirmant.isActive() && !konfirmant.isConfirmed()) {
			if (konfirmant.getConfirmationToken().equals(token)) {
				konfirmant.setConfirmationToken(null);
				repository().save(konfirmant);
				
				return true;
			}
			else {
				return false;
			}
		}
		
		return false;
	}
	
	@Override
	public void activateConfirmationMails() {
		this.setSendConfirmationMails(true);
	}
	
	@Override
	public void deactivateConfirmationMails() {
		this.setSendConfirmationMails(false);
	}
}
