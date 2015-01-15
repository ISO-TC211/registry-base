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
package de.geoinfoffm.registry.client.web;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.geoinfoffm.registry.api.RegistryUserUpdateDTO;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.soap.CreateRegistryUserRequest;

/**
 * @author Florian Esser
 *
 */
public class RegistryUserFormBean
{
	private UUID uuid;
	private String name;
	private String emailAddress;
	private String password;
	private String confirmedPassword;
	private UUID organizationUuid;
	private String preferredLanguage;
	private List<String> roles = new ArrayList<String>();
	private boolean isActive = true;
	
	public RegistryUserFormBean() {
		
	}
	
	public RegistryUserFormBean(RegistryUser userData) {
		initializeFromUser(userData);
	}

	public void initializeFromUser(RegistryUser user) {
		this.uuid = user.getUuid();
		this.name = user.getName().toString();
		this.emailAddress = user.getEmailAddress().toString();
		this.preferredLanguage = user.getPreferredLanguage().toString();
		this.organizationUuid = user.getOrganization().getUuid();

		for (Role role : user.getRoles()) {
			this.roles.add(role.getName());
		}
		
		this.isActive = user.isActive();
	}
	
	/**
	 * @return
	 */
	public CreateRegistryUserRequest toRegistrationDTO() {
		CreateRegistryUserRequest result = new CreateRegistryUserRequest();
		result.setName(this.name);
		result.setEmailAddress(this.emailAddress);
		result.setPassword(this.password);
		result.setOrganizationUuid(this.organizationUuid.toString());
		result.setPreferredLanguage(this.preferredLanguage);
		result.setActive(this.isActive);
		
		return result;
	}
	
	public RegistryUserUpdateDTO toUpdateDTO(UUID uuid) {
		RegistryUserUpdateDTO result = new RegistryUserUpdateDTO();
		result.setUuid(uuid);
		result.setName(this.name);
		result.setEmailAddress(this.emailAddress);
		result.setPassword(this.password);
		result.setOrganizationUuid(this.organizationUuid);
		result.setPreferredLanguage(this.preferredLanguage);
		result.setActive(this.isActive);
		result.setRoles(this.roles);
		
		return result;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

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
	 * @return the confirmedPassword
	 */
	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	/**
	 * @param confirmedPassword the confirmedPassword to set
	 */
	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

	public UUID getOrganizationUuid() {
		return organizationUuid;
	}

	public void setOrganizationUuid(UUID organizationUuid) {
		this.organizationUuid = organizationUuid;
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

	public List<String> getRoles() {
		return this.roles;
	}
	
	public void addRole(String role) {
		this.roles.add(role);
	}
	
	public void removeRole(String role) {
		this.roles.remove(role);
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isRegistryPointOfContact the isRegistryPointOfContact to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
