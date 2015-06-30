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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.geoinfoffm.registry.core.model.Authorization;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.iso19115.CI_Address;

/**
 * The class OrganizationDTO.
 *
 * @author Florian Esser
 */
public class OrganizationDTO
{
	private UUID uuid;
	private String organizationNameShortForm;
	private String organizationName;
	private String onlineResourceLogo;
	private String onlineResourceWebsite;
	private String street;
	private String addressLine2;
	private String city;
	private String postalCode;
	private String administrativeArea;
	private String country;
	private List<String> roles = new ArrayList<String>();
	
	public OrganizationDTO() { }
	
	public OrganizationDTO(UUID organizationUuid) {
		this.uuid = organizationUuid;
	}
	
	public OrganizationDTO(String name, String shortName, String street, String city, String postalCode, String administrativeArea, String country) {
		this.organizationName = name;
		this.organizationNameShortForm = shortName;
		this.street = street;
		this.city = city;
		this.postalCode = postalCode;
		this.administrativeArea = administrativeArea;
		this.country = country;
	}
	
	public OrganizationDTO(Organization organization) {
		this.uuid = organization.getUuid();
		
		if (organization.getAddress() != null) {
			CI_Address address = organization.getAddress();
			List<String> streets = address.getDeliveryPoint(); 
			if (streets != null && !streets.isEmpty()) {
				this.street = streets.get(0);
				if (streets.size() > 1) {
					this.addressLine2 = streets.get(1);
				}
			}
			this.city = address.getCity();
			this.administrativeArea = address.getAdministrativeArea();
			this.country = address.getCountry();
			this.postalCode = address.getPostalCode();
		}
		
		this.roles = new ArrayList<String>();
		if (organization.getAuthorizations() != null) {
			for (Authorization auth : organization.getAuthorizations()) {
				this.roles.add(auth.getRole().getName());
			}
		}
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getOrganizationNameShortForm() {
		return organizationNameShortForm;
	}

	public void setOrganizationNameShortForm(String organizationNameShortForm) {
		this.organizationNameShortForm = organizationNameShortForm;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOnlineResourceLogo() {
		return onlineResourceLogo;
	}

	public void setOnlineResourceLogo(String onlineResourceLogo) {
		this.onlineResourceLogo = onlineResourceLogo;
	}

	public String getOnlineResourceWebsite() {
		return onlineResourceWebsite;
	}

	public void setOnlineResourceWebsite(String onlineResourceWebsite) {
		this.onlineResourceWebsite = onlineResourceWebsite;
	}
	
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the administrativeArea
	 */
	public String getAdministrativeArea() {
		return administrativeArea;
	}

	/**
	 * @param administrativeArea the administrativeArea to set
	 */
	public void setAdministrativeArea(String administrativeArea) {
		this.administrativeArea = administrativeArea;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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
