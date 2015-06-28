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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.hibernate.envers.Audited;
import org.hibernate.validator.internal.xml.PropertyType;
import org.springframework.transaction.annotation.Transactional;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.model.DateAdapter;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_OnlineResource;

@XmlType(name = "RE_Register_Type", namespace = "http://www.isotc211.org/2005/grg", 
		 propOrder = { "name", "contentSummary", "uniformResourceIdentifier",
					   "operatingLanguage", "alternativeLanguages", "version", "dateOfLastChange",
					   "owner", "submitter", "containedItemClasses", "citation", "manager", "containedItems" })
@XmlRootElement(name = "RE_Register", namespace = "http://www.isotc211.org/2005/grg")
@XmlSeeAlso({ CharacterString.class, CI_OnlineResource.class, RE_Locale.class, RE_Version.class, RE_RegisterItem.class, 
			  RE_RegisterManager.class, RE_AlternativeName.class, RE_ReferenceSource.class, RE_ItemClass.class, 
			  RE_SubmittingOrganization.class, RE_RegisterOwner.class, PropertyType.class })
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@Audited @Entity
public class RE_Register extends de.geoinfoffm.registry.core.Entity
{
	private static final long serialVersionUID = -7605344237886772304L;

	@XmlElement(name = "name", namespace = "http://www.isotc211.org/2005/grg", required = true, type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Basic(optional = false)
	@Column(columnDefinition = "text")
	private String name;
	
	@XmlElement(name = "contentSummary", namespace = "http://www.isotc211.org/2005/grg", required = true, type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String contentSummary;

	@XmlElement(name = "uniformResourceIdentifier", namespace = "http://www.isotc211.org/2005/grg", required = true)
	@XmlPath("uniformResourceIdentifier/gmd:CI_OnlineResource")
	@OneToOne
	private CI_OnlineResource uniformResourceIdentifier;
	
	@XmlElement(name = "operatingLanguage", namespace = "http://www.isotc211.org/2005/grg", required = true)
	@XmlPath("operatingLanguage/grg:RE_Locale")
	@OneToOne
	private RE_Locale operatingLanguage;

	@XmlElement(name = "alternativeLanguages", namespace = "http://www.isotc211.org/2005/grg", required = true)
	@XmlPath("alternativeLanguages/grg:RE_Locale")
	@OneToMany(fetch = FetchType.EAGER)
	private Set<RE_Locale> alternativeLanguages;

	@XmlElement(name = "version", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("version/grg:RE_Version")
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "versionNumber.value", column = @Column(name = "version_versionNumber", length = 255)),
		@AttributeOverride(name = "versionNumber.date", column = @Column(name = "version_date"))
	})
	private RE_Version version;
	
	@XmlElement(name = "dateOfLastChange", namespace = "http://www.isotc211.org/2005/grg", type = de.geoinfoffm.registry.core.model.iso19103.Date.class)
	@XmlJavaTypeAdapter(DateAdapter.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOfLastChange; 
	
	@XmlElement(name = "containedItem", namespace = "http://www.isotc211.org/2005/grg", required = true)
	@XmlPath("containedItems/grg:RE_RegisterItem")
	@OneToMany(mappedBy = "register", fetch = FetchType.LAZY)
	private Set<RE_RegisterItem> containedItems;

	@XmlElement(name = "manager", namespace = "http://www.isotc211.org/2005/grg", required = true)
	@XmlPath("manager/grg:RE_RegisterManager")
	@ManyToOne(cascade = CascadeType.PERSIST)
	private RE_RegisterManager manager;
	
	@XmlElement(name = "citation", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("citation/grg:RE_ReferenceSource")
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "text.value", column = @Column(name = "sourceCitation_text")),
		@AttributeOverride(name = "citation", column = @Column(name = "sourceCitation_citation"))
	})
	private RE_ReferenceSource citation;

	@XmlElement(name = "containedItemClass", namespace = "http://www.isotc211.org/2005/grg", required = true)
	@XmlPath("containedItemClasses/grg:RE_ItemClass")
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name = "RE_Register_ItemClasses",
	   		   joinColumns = @JoinColumn(name="itemClassId"),
	   		   inverseJoinColumns = @JoinColumn(name="registerId")
	)
	private Set<RE_ItemClass> containedItemClasses;
	
	@XmlElement(name = "submitter", namespace = "http://www.isotc211.org/2005/grg", required = true)
	@XmlPath("submitter/grg:RE_SubmittingOrganization")
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "RE_Register_Submitters",
	   		   joinColumns = @JoinColumn(name="registerId"),
	   		   inverseJoinColumns = @JoinColumn(name="submitterId")
	)	
	private Set<RE_SubmittingOrganization> submitter;
	
	@XmlElement(name = "owner", namespace = "http://www.isotc211.org/2005/grg", required = true)
	@XmlPath("owner/grg:RE_RegisterOwner")
	@ManyToOne(cascade = CascadeType.PERSIST)
	private RE_RegisterOwner owner;

	protected RE_Register() {

	}
	
	public RE_Register(String name) {
		this.name = name;
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
	 * @return the contentSummary
	 */
	public String getContentSummary() {
		return contentSummary;
	}

	/**
	 * @param contentSummary the contentSummary to set
	 */
	public void setContentSummary(String contentSummary) {
		this.contentSummary = contentSummary;
	}

	/**
	 * @return the uniformResourceIdentifier
	 */
	public CI_OnlineResource getUniformResourceIdentifier() {
		return uniformResourceIdentifier;
	}

	/**
	 * @param uniformResourceIdentifier the uniformResourceIdentifier to set
	 */
	public void setUniformResourceIdentifier(CI_OnlineResource uniformResourceIdentifier) {
		this.uniformResourceIdentifier = uniformResourceIdentifier;
	}

	/**
	 * @return the operatingLanguage
	 */
	public RE_Locale getOperatingLanguage() {
		return operatingLanguage;
	}

	/**
	 * @param operatingLanguage the operatingLanguage to set
	 */
	public void setOperatingLanguage(RE_Locale operatingLanguage) {
		this.operatingLanguage = operatingLanguage;
	}

	/**
	 * @return the alternativeLanguages
	 */
	public Set<RE_Locale> getAlternativeLanguages() {
		if (alternativeLanguages == null) {
			alternativeLanguages = new HashSet<RE_Locale>();
		}
		
		return alternativeLanguages;
	}

	/**
	 * @param alternativeLanguages the alternativeLanguages to set
	 */
	protected void setAlternativeLanguages(Set<RE_Locale> alternativeLanguages) {
		this.alternativeLanguages = alternativeLanguages;
	}

	/**
	 * @return the version
	 */
	public RE_Version getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(RE_Version version) {
		this.version = version;
	}

	/**
	 * @return the dateOfLastChange
	 */
	public Date getDateOfLastChange() {
		return dateOfLastChange;
	}

	/**
	 * @param dateOfLastChange the dateOfLastChange to set
	 */
	public void setDateOfLastChange(Date dateOfLastChange) {
		this.dateOfLastChange = dateOfLastChange;
	}

	/**
	 * @return the containedItem
	 */
	public Set<RE_RegisterItem> getContainedItems() {
		if (this.containedItems == null) {
			this.containedItems = new HashSet<RE_RegisterItem>();
		}
		return Collections.unmodifiableSet(containedItems);
	}
	
	public Set<RE_RegisterItem> getContainedItems(RE_ItemStatus status) {
		Set<RE_RegisterItem> result = new HashSet<RE_RegisterItem>();
		for (RE_RegisterItem item : this.containedItems) {
			if (item.getStatus().equals(status)) {
				result.add(item);
			}
		}
		
		return Collections.unmodifiableSet(result);
	}

	public Set<RE_RegisterItem> getContainedItems(Collection<RE_ItemStatus> status) {
		Set<RE_RegisterItem> result = new HashSet<RE_RegisterItem>();
		for (RE_RegisterItem item : this.containedItems) {
			if (status.contains(item.getStatus())) {
				result.add(item);
			}
		}
		
		return Collections.unmodifiableSet(result);
	}

	public Set<RE_RegisterItem> getContainedItems(RE_ItemClass itemClass, Collection<RE_ItemStatus> status) {
		Set<RE_RegisterItem> result = new HashSet<RE_RegisterItem>();
		for (RE_RegisterItem item : this.containedItems) {
			if (item.getItemClass().equals(itemClass) && status.contains(item.getStatus())) {
				result.add(item);
			}
		}
		
		return Collections.unmodifiableSet(result);
	}

	/**
	 * @param containedItem the containedItem to set
	 */
	public void setContainedItems(Set<RE_RegisterItem> containedItem) {
		this.containedItems = containedItem;
	}
	
	public void addContainedItem(RE_RegisterItem containedItem) {
		if (this.containedItems == null) {
			this.containedItems = new HashSet<RE_RegisterItem>();
		}
		this.containedItems.add(containedItem);
	}

	/**
	 * @return the manager
	 */
	public RE_RegisterManager getManager() {
		return manager;
	}

	/**
	 * @param manager the manager to set
	 */
	public void setManager(RE_RegisterManager manager) {
		this.manager = manager;
	}

	/**
	 * @return the citation
	 */
	public RE_ReferenceSource getCitation() {
		return citation;
	}

	/**
	 * @param citation the citation to set
	 */
	public void setCitation(RE_ReferenceSource citation) {
		this.citation = citation;
	}

	/**
	 * @return the containedItemClass
	 */
	public Set<RE_ItemClass> getContainedItemClasses() {
		if (this.containedItemClasses == null) {
			this.containedItemClasses = new LinkedHashSet<RE_ItemClass>();
		}

		return this.containedItemClasses;
	}

	/**
	 * @param containedItemClass the containedItemClass to set
	 */
	protected void setContainedItemClasses(Set<RE_ItemClass> containedItemClasses) {
		this.containedItemClasses = containedItemClasses;
	}
	
	/**
	 * @return the submitter
	 */
	@Transactional(readOnly = true)
	public Set<RE_SubmittingOrganization> getSubmitter() {
		if (this.submitter == null) {
			this.submitter = new HashSet<RE_SubmittingOrganization>();
		}

		return submitter;
	}

	/**
	 * @param submitter the submitter to set
	 */
	protected void setSubmitter(Set<RE_SubmittingOrganization> submitter) {
		this.submitter = submitter;
	}
	
	/**
	 * @return the owner
	 */
	public RE_RegisterOwner getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(RE_RegisterOwner owner) {
		this.owner = owner;
	}
}// end RE_Register