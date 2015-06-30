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
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * Information about online sources from which the dataset, specification, or
 * community profile name and extended metadata elements can be obtained.
 * 
 * @created 10-Sep-2013 19:43:12
 */
@XmlType(name = "CI_OnlineResource_Type", namespace = "http://www.isotc211.org/2005/gmd",
propOrder = { "linkage", "protocol", "applicationProfile",
			  "name", "description", "function" })
@XmlRootElement(name = "CI_OnlineResource", namespace = "http://www.isotc211.org/2005/gmd")
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@Audited @Entity
public class CI_OnlineResource extends de.geoinfoffm.registry.core.Entity
{
	/**
	 * Method, source, or location for online access.
	 * Example: a Uniform Resource Locator (URL) such as http://www,gii.getty.
	 * edu/tgn_browser/
	 */
	@XmlElement(name = "linkage", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@org.hibernate.validator.constraints.URL
	public String linkage;
	/**
	 * Connection protocol to be used
	 */
	@XmlElement(name = "protocol", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	public String protocol;
	/**
	 * Name of an application profile that can be used with the resource
	 */
	@XmlElement(name = "applicationProfile", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	public String applicationProfile;
	/**
	 * Name of the resource
	 */
	@XmlElement(name = "name", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	public String name;
	/**
	 * Description of what the resource is/does
	 */
	@XmlElement(name = "description", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	public String description;
	/**
	 * Function performed by the resource
	 */
	@XmlElement(name = "function", namespace = "http://www.isotc211.org/2005/gmd")
	public CI_OnLineFunctionCode function;
	
	protected CI_OnlineResource() {

	}

	public CI_OnlineResource(String linkage) {
		this.linkage = linkage;
	}

	/**
	 * @return the linkage
	 */
	public String getLinkage() {
		return linkage;
	}

	/**
	 * @param linkage the linkage to set
	 */
	public void setLinkage(String linkage) {
		this.linkage = linkage;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the applicationProfile
	 */
	public String getApplicationProfile() {
		return applicationProfile;
	}

	/**
	 * @param applicationProfile the applicationProfile to set
	 */
	public void setApplicationProfile(String applicationProfile) {
		this.applicationProfile = applicationProfile;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the function
	 */
	public CI_OnLineFunctionCode getFunction() {
		return function;
	}

	/**
	 * @param function the function to set
	 */
	public void setFunction(CI_OnLineFunctionCode function) {
		this.function = function;
	}

	public void finalize() throws Throwable {

	}
}// end CI_OnlineResource