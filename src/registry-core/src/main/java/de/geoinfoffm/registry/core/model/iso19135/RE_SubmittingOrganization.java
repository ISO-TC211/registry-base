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
package de.geoinfoffm.registry.core.model.iso19135;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_ResponsibleParty;

/**
 * 
 * @created 09-Sep-2013 19:14:30
 */
@XmlType(name = "RE_SubmittingOrganization_Type", 
		 namespace = "http://www.isotc211.org/2005/grg",
		 propOrder = { "name", "contact" })
@XmlRootElement(name = "RE_SubmittingOrganization", namespace = "http://www.isotc211.org/2005/grg")
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@Audited @Entity 
public class RE_SubmittingOrganization extends de.geoinfoffm.registry.core.Entity
{
	private static final long serialVersionUID = -1843989006286341018L;

	@XmlElement(name = "name", namespace = "http://www.isotc211.org/2005/grg", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String name;

	@XmlElement(name = "contact", namespace = "http://www.isotc211.org/2005/grg")
	@OneToOne(cascade = CascadeType.ALL)
	private CI_ResponsibleParty contact;

	@XmlTransient
	@ManyToMany
	@JoinTable(name = "RE_Register_Submitters",
	   		   joinColumns = @JoinColumn(name="submitterId"),
	   		   inverseJoinColumns = @JoinColumn(name="registerId")
	)	
	private Set<RE_Register> register;

	protected RE_SubmittingOrganization() {
		super();
	}

	public RE_SubmittingOrganization(String name, CI_ResponsibleParty contact) {
		this();

		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("RE_SubmittingOrganization: name must not be empty");
		}
		else if (contact == null) {
			throw new IllegalArgumentException("RE_SubmittingOrganization: contact must not be empty");
		}

		this.name = name;
		this.contact = contact;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String newName) throws IllegalArgumentException {
		if ((newName == null || newName.isEmpty()) /*&& !isLoadingFromPersistentStorage()*/) {
			throw new IllegalArgumentException("RE_SubmittingOrganization: name must not be empty");
		}

		this.name = newName;
	}

	public CI_ResponsibleParty getContact() {
		return this.contact;
	}

	public void setContact(CI_ResponsibleParty newContact) {
		if (newContact == null) {
			throw new IllegalArgumentException("RE_SubmittingOrganization: contact must not be empty");
		}

		this.contact = newContact;
	}

	/**
	 * @return the register
	 */
	public Set<RE_Register> getRegister() {
		return register;
	}

	/**
	 * @param register the register to set
	 */
	public void setRegister(Set<RE_Register> register) {
		this.register = register;
	}
	
	public void addRegister(RE_Register register) {
		if (this.register == null) {
			this.register = new HashSet<RE_Register>();
		}
		this.register.add(register);
	}

	public void finalize() throws Throwable {

	}
	
}// end RE_SubmittingOrganization