/**
 * 
 */
package de.geoinfoffm.registry.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object for user updates.
 * 
 * @author Florian Esser
 *
 */
public class RegistryUserUpdateDTO
{
	private UUID uuid;
	private String name;
	private String emailAddress;
	private String password;
	private String preferredLanguage;
	private UUID organizationUuid;
	private List<String> roles;
	private Boolean isActive;
	
	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}
	
	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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
	 * @return the organizationUuid
	 */
	public UUID getOrganizationUuid() {
		return organizationUuid;
	}
	/**
	 * @param organizationUuid the organizationUuid to set
	 */
	public void setOrganizationUuid(UUID organizationUuid) {
		this.organizationUuid = organizationUuid;
	}
	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = new ArrayList<String>();
		this.roles.addAll(roles);
	}
	
	public void addRole(String role) {
		if (this.roles == null) {
			this.roles = new ArrayList<String>();
		}
		
		this.roles.add(role);
	}

	/**
	 * @return the isRegistryPointOfContact
	 */
	public Boolean isActive() {
		return isActive;
	}
	/**
	 * @param isRegistryPointOfContact the isRegistryPointOfContact to set
	 */
	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	
}
