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

import de.geoinfoffm.registry.api.OrganizationUpdateDTO;
import de.geoinfoffm.registry.api.soap.CreateOrganizationRequest;
import de.geoinfoffm.registry.core.model.Authorization;
import de.geoinfoffm.registry.core.model.Organization;

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
