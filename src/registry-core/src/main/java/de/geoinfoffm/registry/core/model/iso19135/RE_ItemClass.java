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
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;

/**
 * 
 * @created 09-Sep-2013 19:12:51
 */
@XmlType(name = "RE_ItemClass_Type", 
		 namespace = "http://www.isotc211.org/2005/grg",
		 propOrder = { "name", "technicalStandard", "alternativeNames", "describedItem" })
@XmlRootElement(name = "RE_ItemClass", namespace = "http://www.isotc211.org/2005/grg")
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@Audited @Entity
public class RE_ItemClass extends de.geoinfoffm.registry.core.Entity
{

	@XmlElement(name = "name", namespace = "http://www.isotc211.org/2005/grg", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String name;

	@XmlElement(name = "technicalStandard", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("technicalStandard/gmd:CI_Citation")
	@OneToOne
	private CI_Citation technicalStandard;

	@XmlElement(name = "alternativeNames", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("alternativeNames/grg:RE_AlternativeName")
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name = "RE_ItemClass_AlternativeNames",
	   joinColumns = @JoinColumn(name="itemClassId"),
	   inverseJoinColumns = @JoinColumn(name="alternativeNameId")
	)
	private Set<RE_AlternativeName> alternativeNames;

	@JsonBackReference
	@XmlTransient
	@ManyToMany
	@JoinTable(name = "RE_Register_ItemClasses",
	   		   joinColumns = @JoinColumn(name="itemClassId"),
	   		   inverseJoinColumns = @JoinColumn(name="registerId")
	)
	private Set<RE_Register> registers;
	
	@XmlElement(name = "describedItem", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("describedItem/grg:RE_RegisterItem")
	@XmlInverseReference(mappedBy = "itemClass")
	@OneToMany(mappedBy = "itemClass")
	private Set<RE_RegisterItem> describedItem;
	
	public RE_ItemClass() {

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
	 * @return the technicalStandard
	 */
	public CI_Citation getTechnicalStandard() {
		return technicalStandard;
	}

	/**
	 * @param technicalStandard the technicalStandard to set
	 */
	public void setTechnicalStandard(CI_Citation technicalStandard) {
		this.technicalStandard = technicalStandard;
	}

	/**
	 * @return the alternativeLanguages
	 */
	public Set<RE_AlternativeName> getAlternativeNames() {
		return alternativeNames;
	}

	/**
	 * @param alternativeLanguages the alternativeLanguages to set
	 */
	public void setAlternativeNames(Set<RE_AlternativeName> alternativeNames) {
		this.alternativeNames = alternativeNames;
	}

	/**
	 * @return the registers
	 */
	public Set<RE_Register> getRegisters() {
		if (this.registers == null) {
			this.registers = new HashSet<RE_Register>();
		}

		return registers;
	}

	/**
	 * @param registers the registers to set
	 */
	protected void setRegisters(Set<RE_Register> registers) {
		this.registers = registers;
	}
	
	public Set<RE_RegisterItem> getDescribedItem() {
		if (describedItem == null) {
			describedItem = new HashSet<RE_RegisterItem>();
		}
		return describedItem;
	}

	public void setDescribedItem(Set<RE_RegisterItem> describedItem) {
		this.describedItem = describedItem;
	}

	public void finalize() throws Throwable {

	}
}//end RE_ItemClass