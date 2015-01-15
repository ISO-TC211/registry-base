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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * Information required enabling contact with the responsible person and/or
 * organisation
 * 
 * @created 10-Sep-2013 19:42:39
 */
@Access(AccessType.FIELD)
@Audited @Entity 
public class CI_Contact extends de.geoinfoffm.registry.core.Entity
{
	private static final long serialVersionUID = 3778596067902313872L;

	/**
	 * Telephone numbers at which the organisation or individual may be contacted
	 */
	@OneToOne
	private CI_Telephone phone;
	/**
	 * Physical and email address at which the organisation or individual may be
	 * contacted
	 */
	@OneToOne
	private CI_Address address;
	/**
	 * Online information that can be used to contact the individual or organisation
	 */
	@OneToOne
	private CI_OnlineResource onlineResource;
	/**
	 * Time period (including time zone) when individuals can contact the organisation
	 * or individual
	 */
	@AttributeOverride(name = "value", column = @Column(name = "hoursOfService", length = 2000))
	private CharacterString hoursOfService;
	/**
	 * Supplemental instructions on how or when to contact the individual or
	 * organisation
	 */
	@AttributeOverride(name = "value", column = @Column(name = "contactInstructions", length = 2000))
	private CharacterString contactInstructions;

	protected CI_Contact() {
	}
	
	public CI_Contact(CharacterString hoursOfService, CharacterString contactInstructions) {
		this.hoursOfService = hoursOfService;
		this.contactInstructions = contactInstructions;
	}
	
	
	/**
	 * @return the phone
	 */
	public CI_Telephone getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(CI_Telephone phone) {
		this.phone = phone;
	}

	/**
	 * @return the address
	 */
	public CI_Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(CI_Address address) {
		this.address = address;
	}

	/**
	 * @return the onlineResource
	 */
	public CI_OnlineResource getOnlineResource() {
		return onlineResource;
	}

	/**
	 * @param onlineResource the onlineResource to set
	 */
	public void setOnlineResource(CI_OnlineResource onlineResource) {
		this.onlineResource = onlineResource;
	}

	/**
	 * @return the hoursOfService
	 */
	public CharacterString getHoursOfService() {
		return hoursOfService;
	}

	/**
	 * @param hoursOfService the hoursOfService to set
	 */
	public void setHoursOfService(CharacterString hoursOfService) {
		this.hoursOfService = hoursOfService;
	}

	/**
	 * @return the contactInstructions
	 */
	public CharacterString getContactInstructions() {
		return contactInstructions;
	}

	/**
	 * @param contactInstructions the contactInstructions to set
	 */
	public void setContactInstructions(CharacterString contactInstructions) {
		this.contactInstructions = contactInstructions;
	}

	public void finalize() throws Throwable {

	}

}// end CI_Contact