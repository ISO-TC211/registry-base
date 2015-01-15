package de.geoinfoffm.registry.client.web;

import de.geoinfoffm.registry.soap.CreateRegistryUserRequest;

public class SignupFormBean

{
	private String name;
	private String emailAddress;
	private String password;
	private String confirmedPassword;
	private String preferredLanguage;
	private String organizationUuid;
	private boolean organizationNotListed;

	public SignupFormBean() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

	public String getPreferredLanguage() {
		return preferredLanguage;
	}

	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	public String getOrganizationUuid() {
		return organizationUuid;
	}

	public void setOrganizationUuid(String organizationUuid) {
		this.organizationUuid = organizationUuid;
	}

	public boolean isOrganizationNotListed() {
		return organizationNotListed;
	}

	public void setOrganizationNotListed(boolean organizationNotListed) {
		this.organizationNotListed = organizationNotListed;
	}
	
//	public CreateGdiRegistryUserRequest toRegistrationDTO() {
//		CreateGdiRegistryUserRequest result = new CreateGdiRegistryUserRequest();
//		result.setName(this.name);
//		result.setEmailAddress(this.emailAddress);
//		result.setPassword(this.password);
//		result.setPreferredLanguage(this.preferredLanguage);
//		result.setOrganizationUuid(this.organizationUuid);
//		result.setActive(true);
//		
//		return result;
//	}

	public CreateRegistryUserRequest toRegistrationDTO() {
		CreateRegistryUserRequest result = new CreateRegistryUserRequest();
		result.setName(this.name);
		result.setEmailAddress(this.emailAddress);
		result.setPassword(this.password);
		result.setOrganizationUuid(this.organizationUuid);
		result.setPreferredLanguage(this.preferredLanguage);
		result.setActive(true);
		
		return result;
	}
}
