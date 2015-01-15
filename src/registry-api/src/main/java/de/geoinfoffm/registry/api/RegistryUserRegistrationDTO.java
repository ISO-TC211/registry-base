/**
 * 
 */
package de.geoinfoffm.registry.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object for user registration.
 * 
 * @author Florian Esser
 *
 */
public class RegistryUserRegistrationDTO 
{
	private String name;
	private String emailAddress;
	private String password;
	private String preferredLanguage;
	private UUID organizationUuid;
	private List<String> roles;
	private boolean isActive = false;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress.toLowerCase();
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress.toLowerCase();
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the preferredLanguage
	 */
	public String getPreferredLanguage() {
		return preferredLanguage;
	}

	/**
	 * @param preferredLanguage the preferredLanguage to set
	 */
	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	/**
	 * @return the organization
	 */
	public UUID getOrganizationUuid() {
		return organizationUuid;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganizationUuid(UUID organizationUuid) {
		this.organizationUuid = organizationUuid;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(Collection<String> roles) {
		this.roles = new ArrayList<String>();
		this.roles.addAll(roles);
	}

	/**
	 * @return the isRegistryPointOfContact
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive the isRegistryPointOfContact to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
