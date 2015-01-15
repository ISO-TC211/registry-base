package de.geoinfoffm.registry.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrganizationUpdateDTO
{
	private UUID uuid;
	private String name;
	private List<String> roles;
	
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
