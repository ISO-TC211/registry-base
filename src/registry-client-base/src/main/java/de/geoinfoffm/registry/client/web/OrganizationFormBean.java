package de.geoinfoffm.registry.client.web;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.geoinfoffm.registry.api.OrganizationUpdateDTO;
import de.geoinfoffm.registry.core.model.Authorization;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.soap.CreateOrganizationRequest;

public class OrganizationFormBean
{
	private UUID uuid;
	private String name;
	private List<String> roles = new ArrayList<String>();
	
	public OrganizationFormBean() { }
	
	public OrganizationFormBean(UUID organizationUuid) {
		this.uuid = organizationUuid;
	}
	
	public OrganizationFormBean(String name) {
		this.name = name;
	}
	
	public OrganizationFormBean(Organization organization) {
		this.uuid = organization.getUuid();
		this.name = organization.getName();
		this.roles = new ArrayList<String>();
		if (organization.getAuthorizations() != null) {
			for (Authorization auth : organization.getAuthorizations()) {
				this.roles.add(auth.getRole().getName());
			}
		}
	}

	/**
	 * @return
	 */
	public CreateOrganizationRequest toRegistrationDTO() {
		CreateOrganizationRequest result = new CreateOrganizationRequest();
		result.setName(this.name);
//		result.setSubmittingOrganization(...);
		result.getRole().addAll(this.roles);
		return result;
	}
	
	public OrganizationUpdateDTO toUpdateDTO(UUID uuid) {
		OrganizationUpdateDTO result = new OrganizationUpdateDTO();
		result.setUuid(uuid);
		result.setName(this.name);
		result.setRoles(this.roles);
		
		return result;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}
