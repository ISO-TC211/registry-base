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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.hibernate.envers.Audited;
import org.hibernate.validator.internal.xml.PropertyType;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_OnlineResource;


/**
 * The class RE_SubregisterDescription.
 *
 * @author Florian Esser
 * @created 09-Sep-2013 19:14:37
 */
@XmlType(name = "RE_SubregisterDescription_Type", namespace = "http://www.isotc211.org/2005/grg", 
propOrder = { "uniformResourceIdentifier", "operatingLanguage", "containedItemClass",
			   "subregisterManager" })
@XmlRootElement(name = "RE_SubregisterDescription", namespace = "http://www.isotc211.org/2005/grg")
@XmlSeeAlso({ CharacterString.class, CI_OnlineResource.class, RE_Locale.class, RE_Version.class, RE_RegisterItem.class, 
	  RE_RegisterManager.class, RE_AlternativeName.class, RE_ReferenceSource.class, RE_ItemClass.class, 
	  RE_SubmittingOrganization.class, RE_RegisterOwner.class, PropertyType.class })
@XmlAccessorType(XmlAccessType.FIELD)
@ItemClass("Subregister")
@Access(AccessType.FIELD)
@Audited @Entity
public class RE_SubregisterDescription extends RE_RegisterItem {

	@XmlElement(name = "uniformResourceIdentifier", namespace = "http://www.isotc211.org/2005/grg", required = true)
	@XmlPath("uniformResourceIdentifier/gmd:CI_OnlineResource")
	@OneToOne(cascade = CascadeType.ALL)
	private CI_OnlineResource uniformResourceIdentifier;
	
	@XmlElement(name = "operatingLanguage", namespace = "http://www.isotc211.org/2005/grg", required = true)
	@XmlPath("operatingLanguage/grg:RE_Locale")
	@OneToOne(cascade = CascadeType.ALL)
	private RE_Locale operatingLanguage;

	@XmlElement(name = "containedItemClass", namespace = "http://www.isotc211.org/2005/grg", required = true)
	@XmlPath("containedItemClasses/grg:RE_ItemClass")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RE_SubregisterDescription_ItemClasses",
	   		   joinColumns = @JoinColumn(name="itemClassId"),
	   		   inverseJoinColumns = @JoinColumn(name="subregisterDescriptionId")
	)
	private List<RE_ItemClass> containedItemClasses;

	@XmlElement(name = "manager", namespace = "http://www.isotc211.org/2005/grg", required = true)
	@XmlPath("manager/grg:RE_RegisterManager")
	@ManyToOne(cascade = CascadeType.PERSIST)
	private RE_RegisterManager subregisterManager;

	protected RE_SubregisterDescription() {
		
	}
	
	public RE_SubregisterDescription(CI_OnlineResource uniformResourceIdentifier, RE_Locale operatingLanguage, List<RE_ItemClass> containedItemClasses, RE_RegisterManager manager) {
		this.uniformResourceIdentifier = uniformResourceIdentifier;
		this.operatingLanguage = operatingLanguage;
		this.getContainedItemClasses().addAll(containedItemClasses);
		this.subregisterManager = manager;
	}

	public CI_OnlineResource getUniformResourceIdentifier() {
		return uniformResourceIdentifier;
	}

	public void setUniformResourceIdentifier(CI_OnlineResource uniformResourceIdentifier) {
		this.uniformResourceIdentifier = uniformResourceIdentifier;
	}

	public RE_Locale getOperatingLanguage() {
		return operatingLanguage;
	}

	public void setOperatingLanguage(RE_Locale operatingLanguage) {
		this.operatingLanguage = operatingLanguage;
	}

	public List<RE_ItemClass> getContainedItemClasses() {
		if (this.containedItemClasses == null) {
			this.containedItemClasses = new ArrayList<>();
		}
		return containedItemClasses;
	}

	public void setContainedItemClasses(List<RE_ItemClass> containedItemClasses) {
		this.containedItemClasses = containedItemClasses;
	}

	public RE_RegisterManager getSubregisterManager() {
		return subregisterManager;
	}

	public void setSubregisterManager(RE_RegisterManager subregisterManager) {
		this.subregisterManager = subregisterManager;
	}
}//end RE_SubregisterDescription