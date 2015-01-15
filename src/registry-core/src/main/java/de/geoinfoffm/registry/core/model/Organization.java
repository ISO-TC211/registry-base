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
package de.geoinfoffm.registry.core.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_ResponsibleParty;
import de.geoinfoffm.registry.core.model.iso19115.CI_RoleCode;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;


/**
 * Persistence class for {@link Organization}.<br>
 * <br>
 * Contains JPA annotations for object-relational mapping and JAXB annotations for
 * XML serialization.
 * 
 * @author Florian Esser
 *
 */
@XmlType(name = "Organization_Type", 
		 namespace = "http://registry.gdi-de.org",
		 propOrder = { "shortName", "name", "category", "onlineResourceLogo", "onlineResourceWebsite",
			   		   "submittingOrganization", "users" })
@XmlRootElement(name = "Organization", namespace = "http://registry.gdi-de.org")
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@Audited @javax.persistence.Entity
public class Organization extends Actor 
{
	private static final long serialVersionUID = -6985882046283953955L;

	@XmlElement(name = "name", namespace = "http://registry.gdi-de.org", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Basic(optional = false)
	@Column(columnDefinition = "text")
	private String name;

	@XmlElement(name = "submittingOrganization", namespace = "http://registry.gdi-de.org")
	@XmlPath("organization/grg:RE_SubmittingOrganization")
//		@Basic(optional = false)
	@OneToOne(cascade = CascadeType.PERSIST, optional = false)
	private RE_SubmittingOrganization submittingOrganization;
	
	@XmlElement(name = "users", namespace = "http://registry.gdi-de.org")
	@OneToMany(mappedBy = "organization")
	private Set<RegistryUser> users = new HashSet<RegistryUser>();
	
	@XmlTransient
	@OneToMany(mappedBy = "delegatingOrganization")
	private Set<Delegation> delegations = new HashSet<Delegation>();
	
	/**
	 * Creates an empty persistence object.
	 */
	protected Organization() { 
	}

	public Organization(String name) {
		this.name = name;

		CI_ResponsibleParty resp = new CI_ResponsibleParty("NN", null, null, CI_RoleCode.USER);
		RE_SubmittingOrganization suborg = new RE_SubmittingOrganization(name, resp);
		this.submittingOrganization = suborg;		
	}
	
	public Organization(String name, RE_SubmittingOrganization submittingOrganization) {
		this.name = name;
		this.submittingOrganization = submittingOrganization;
	}
	
	/**
	 * @return the name
	 */
	@Override
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
	 * @return the submittingOrganization
	 */
	public RE_SubmittingOrganization getSubmittingOrganization() {
		return submittingOrganization;
	}

	/**
	 * @param submittingOrganization the submittingOrganization to set
	 */
	public void setSubmittingOrganization(RE_SubmittingOrganization submittingOrganization) {
		this.submittingOrganization = submittingOrganization;
	}

	/**
	 * @return the users
	 */
	public Set<RegistryUser> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(Set<RegistryUser> users) {
		this.users = users;
	}

	public Set<Delegation> getDelegations() {
		return delegations;
	}

	public void setDelegations(Set<Delegation> delegations) {
		this.delegations = delegations;
	}
	
}
