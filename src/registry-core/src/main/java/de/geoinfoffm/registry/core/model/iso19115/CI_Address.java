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

import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

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
	@AttributeOverride(name = "value", column = @Column(name = "address_deliveryPoint", length = 2000))
	private Set<CharacterString> deliveryPoint;
	/**
	 * City of the physical address
	 */	
	@AttributeOverride(name = "value", column = @Column(name = "city", length = 2000))
	private CharacterString city;
	/**
	 * State, province of the physical address
	 */
	@AttributeOverride(name = "value", column = @Column(name = "administrativeArea", length = 2000))
	private CharacterString administrativeArea;
	/**
	 * ZIP or other postal code 
	 */
	@AttributeOverride(name = "value", column = @Column(name = "postalCode", length = 2000))
	private CharacterString postalCode;
	/**
	 * Country of the physical address
	 */
	@AttributeOverride(name = "value", column = @Column(name = "country", length = 2000))
	private CharacterString country;
	/**
	 * Address of the electronic mailbox of the responsible organisation or individual
	 */
	@ElementCollection
	@AttributeOverride(name = "value", column = @Column(name = "address_email", length = 2000))
	private Set<CharacterString> electronicMailAddress;

	protected CI_Address(){

	}

	public Set<CharacterString> getDeliveryPoint() {
		return deliveryPoint;
	}

	public void setDeliveryPoint(Set<CharacterString> deliveryPoint) {
		this.deliveryPoint = deliveryPoint;
	}

	public CharacterString getCity() {
		return city;
	}

	public void setCity(CharacterString city) {
		this.city = city;
	}

	public CharacterString getAdministrativeArea() {
		return administrativeArea;
	}

	public void setAdministrativeArea(CharacterString administrativeArea) {
		this.administrativeArea = administrativeArea;
	}

	public CharacterString getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(CharacterString postalCode) {
		this.postalCode = postalCode;
	}

	public CharacterString getCountry() {
		return country;
	}

	public void setCountry(CharacterString country) {
		this.country = country;
	}

	public Set<CharacterString> getElectronicMailAddress() {
		return electronicMailAddress;
	}

	public void setElectronicMailAddress(Set<CharacterString> electronicMailAddress) {
		this.electronicMailAddress = electronicMailAddress;
	}

	public void finalize() throws Throwable {

	}

}//end CI_Address