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
package de.geoinfoffm.registry.core.model.iso19115;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * Location of the responsible individual or organisation
 * @created 10-Sep-2013 19:42:17
 */
@Access(AccessType.FIELD)
@Audited @Entity
public class CI_Address extends de.geoinfoffm.registry.core.Entity
{
	private static final long serialVersionUID = 5913450286809443634L;

	/**
	 * Address line for the physical address (Street name, box number, suite)
	 */
	@ElementCollection
	private List<String> deliveryPoint;
	/**
	 * City of the physical address
	 */	
	@Column(columnDefinition = "text")
	private String city;
	/**
	 * State, province of the physical address
	 */
	@Column(columnDefinition = "text")
	private String administrativeArea;
	/**
	 * ZIP or other postal code 
	 */
	@Column(columnDefinition = "text")
	private String postalCode;
	/**
	 * Country of the physical address
	 */
	@Column(columnDefinition = "text")
	private String country;
	/**
	 * Address of the electronic mailbox of the responsible organisation or individual
	 */
	@ElementCollection
	private Set<String> electronicMailAddress;

	protected CI_Address(){

	}

	public List<String> getDeliveryPoint() {
		if (deliveryPoint == null) {
			this.deliveryPoint = new ArrayList<>();
		}
		
		return deliveryPoint;
	}

	public void setDeliveryPoint(List<String> deliveryPoint) {
		this.deliveryPoint = deliveryPoint;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAdministrativeArea() {
		return administrativeArea;
	}

	public void setAdministrativeArea(String administrativeArea) {
		this.administrativeArea = administrativeArea;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Set<String> getElectronicMailAddress() {
		return electronicMailAddress;
	}

	public void setElectronicMailAddress(Set<String> electronicMailAddress) {
		this.electronicMailAddress = electronicMailAddress;
	}

	public void finalize() throws Throwable {

	}
}//end CI_Address