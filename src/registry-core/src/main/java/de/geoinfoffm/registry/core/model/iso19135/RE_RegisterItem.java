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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
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
import com.fasterxml.jackson.annotation.JsonIgnore;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.PropertyTypeAdapter;
import de.geoinfoffm.registry.core.XmlAccessor;
import de.geoinfoffm.registry.core.model.BigIntegerAdapter;
import de.geoinfoffm.registry.core.model.Clarification;
import de.geoinfoffm.registry.core.model.DateAdapter;
import de.geoinfoffm.registry.core.model.Retirement;
import de.geoinfoffm.registry.core.model.SupersessionPart;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19103.Integer;

/**
 * 
 * @created 09-Sep-2013 19:13:53
 */
@XmlType(name = "RE_RegisterItem_Type", 
		 namespace = "http://www.isotc211.org/2005/grg",
		 propOrder = { "itemIdentifier", "name", "status", "dateAccepted", "dateAmended", "definition",
					   "description", "fieldOfApplication", "alternativeExpressions", "amendmentInformation", 
					   "clarificationInformation", "additionInformation", "itemClass",
					   "specificationSource", "specificationLineage", "successors", "predecessors" })
@XmlRootElement(name = "RE_RegisterItem", namespace = "http://www.isotc211.org/2005/grg")
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Audited @Entity
public class RE_RegisterItem extends de.geoinfoffm.registry.core.Entity
{
	private static final long serialVersionUID = 3005629545051495718L;

	@XmlElement(name = "itemIdentifier", namespace = "http://www.isotc211.org/2005/grg", type = Integer.class)
	@XmlJavaTypeAdapter(BigIntegerAdapter.class)
	@Basic(optional = false)
	private BigInteger itemIdentifier;
	
	@XmlElement(name = "name", namespace = "http://www.isotc211.org/2005/grg", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Basic(optional = false)
	@Column(columnDefinition = "text")
	private String name;

	@XmlElement(name = "status", namespace = "http://www.isotc211.org/2005/grg")
//	@XmlPath("status/grg:RE_ItemStatus") 
	@Enumerated(EnumType.STRING)
	private RE_ItemStatus status;

	@XmlElement(name = "dateAccepted", namespace = "http://www.isotc211.org/2005/grg", type = de.geoinfoffm.registry.core.model.iso19103.Date.class)
	@XmlJavaTypeAdapter(DateAdapter.class)
	@Temporal(TemporalType.DATE)
	private Date dateAccepted;

	@XmlElement(name = "dateAmended", namespace = "http://www.isotc211.org/2005/grg", type = de.geoinfoffm.registry.core.model.iso19103.Date.class)
	@XmlJavaTypeAdapter(DateAdapter.class)
	@Temporal(TemporalType.DATE)
	private Date dateAmended;

	@XmlElement(name = "definition", namespace = "http://www.isotc211.org/2005/grg", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String definition;

	@XmlElement(name = "description", namespace = "http://www.isotc211.org/2005/grg", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String description;

	@JsonIgnore
	@XmlElement(name = "fieldOfApplication", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("fieldOfApplication/grg:RE_FieldOfApplication")
	@ElementCollection
	@AttributeOverrides({
		@AttributeOverride(name = "name", column = @Column(name = "fieldOfApplication_name")),
		@AttributeOverride(name = "description", column = @Column(name = "fieldOfApplication_description"))
	})
	private Set<RE_FieldOfApplication> fieldOfApplication;

	@XmlElement(name = "alternativeExpressions", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("alternativeExpressions/grg:RE_AlternativeExpression")
	@OneToMany
	@AttributeOverrides({
		@AttributeOverride(name = "locale_name", column = @Column(name = "alternativeExpression_locale_name")),
		@AttributeOverride(name = "locale_language", column = @Column(name = "alternativeExpression_locale_language")),
		@AttributeOverride(name = "locale_country", column = @Column(name ="alternativeExpression_locale_country")),
		@AttributeOverride(name = "locale_characterEncoding", column = @Column(name = "alternativeExpression_locale_characterEncoding")),
		@AttributeOverride(name = "locale_citationId", column = @Column(name = "alternativeExpression_locale_citationId"))		
	})
	private Set<RE_AlternativeExpression> alternativeExpressions;

	@XmlElement(name = "successor", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("successor/grg:RE_RegisterItem")
	@XmlJavaTypeAdapter(PropertyTypeAdapter.class)
	@ManyToMany
	@JoinTable(name="RE_RegisterItem_Successor",
	 joinColumns=@JoinColumn(name="successorId"),
	 inverseJoinColumns=@JoinColumn(name="predecessorId")
	)
	private List<RE_RegisterItem> successors;

	@JsonBackReference
	@XmlElement(name = "predecessor", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("predecessor/grg:RE_RegisterItem")
	@XmlInverseReference(mappedBy = "successor")
	@ManyToMany
	@JoinTable(name="RE_RegisterItem_Successor",
	 joinColumns=@JoinColumn(name="predecessorId"),
	 inverseJoinColumns=@JoinColumn(name="successorId")
	)
	private List<RE_RegisterItem> predecessors;

	@XmlElement(name = "specificationLineage", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("specificationLineage/grg:RE_Reference")
	@OneToMany
	private Set<RE_Reference> specificationLineage;

//	@Embedded
//	@AttributeOverrides({
//		@AttributeOverride(name = "itemIdentifierAtSource.value", column = @Column(name = "specificationSource_itemIdentifierAtSource")),
//		@AttributeOverride(name = "similarity", column = @Column(name = "specificationSource_similarity")),
//		@AttributeOverride(name = "referenceText.value", column = @Column(name = "specificationSource_referenceText")),
//		@AttributeOverride(name = "similarity", column = @Column(name = "specificationSource_similarity")),
//		@AttributeOverride(name = "notes.value", column = @Column(name = "specificationSource_notes")),
//		@AttributeOverride(name = "sourceCitation.text.value", column = @Column(name = "specificationSource_sourceCitation_text")),
//		@AttributeOverride(name = "sourceCitation_citation", column = @Column(name = "specificationSource_sourceCitation_citation"))
//	})
	@XmlElement(name = "specificationSource", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("specificationSource/grg:RE_Reference")
	@OneToOne
	private RE_Reference specificationSource;

	@JsonBackReference
	@XmlElement(name = "itemClass", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("itemClass/grg:RE_ItemClass")
	@ManyToOne
	private RE_ItemClass itemClass;

	@XmlElement(name = "additionInformation", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("additionInformation/grg:RE_AdditionInformation")
	@XmlAccessor(XmlAccessType.PROPERTY)
	@Transient
//	@OneToMany(mappedBy = "item", targetEntity = RE_ProposalManagementInformation.class)
//	@OneToMany(mappedBy = "item")
//	@JoinTable(name="RE_RegisterItem_ProposalManagementInfos",
//	 		   joinColumns=@JoinColumn(name="itemId"),
//	 		   inverseJoinColumns=@JoinColumn(name="pmiId")
//	)
	private Set<RE_AdditionInformation> additionInformation;

	@XmlElement(name = "clarificationInformation", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("clarificationInformation/grg:RE_ClarificationInformation")
	@XmlAccessor(XmlAccessType.PROPERTY)
	@Transient
//	@OneToMany(mappedBy = "item", targetEntity = RE_ProposalManagementInformation.class)
//	@OneToMany(mappedBy = "item")
//	@JoinTable(name="RE_RegisterItem_ProposalManagementInfos",
//	 		   joinColumns=@JoinColumn(name="itemId"),
//	 		   inverseJoinColumns=@JoinColumn(name="pmiId")
//	)
	private Set<RE_ClarificationInformation> clarificationInformation;

	@XmlElement(name = "amendmentInformation", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("amendmentInformation/grg:RE_AmendmentInformation")
	@XmlAccessor(XmlAccessType.PROPERTY)
	@Transient
//	@OneToMany(mappedBy = "item", targetEntity = RE_ProposalManagementInformation.class)
//	@OneToMany(mappedBy = "item")
//	@JoinTable(name="RE_RegisterItem_ProposalManagementInfos",
//	 		   joinColumns=@JoinColumn(name="itemId"),
//	 		   inverseJoinColumns=@JoinColumn(name="pmiId")
//	)
	private Set<RE_AmendmentInformation> amendmentInformation;
	
	@JsonIgnore
	@XmlTransient
	@OneToMany(mappedBy = "item")
//	@JoinTable(name="RE_RegisterItem_ProposalManagementInfos",
//	  		   joinColumns=@JoinColumn(name="itemId"),
//	  		   inverseJoinColumns=@JoinColumn(name="pmiId")
//	)
	private Set<RE_ProposalManagementInformation> proposalManagementInformations;

	@JsonBackReference
	@XmlInverseReference(mappedBy = "containedItems")
	@XmlPath("register/grg:RE_Register")
	@ManyToOne
	private RE_Register register;
	
	public RE_RegisterItem() {
		super();
	}
	
	public RE_RegisterItem(RE_Register register, RE_ItemClass itemClass, 
			String name, String definition, RE_AdditionInformation additionInformation) {
		
		this.itemClass = itemClass;
		this.register = register;
		this.name = name;
		this.definition = definition;
		this.additionInformation = new HashSet<RE_AdditionInformation>();
		this.additionInformation.add(additionInformation);
	}
	
	/**
	 * @return the itemIdentifier
	 */
	public BigInteger getItemIdentifier() {
		return itemIdentifier;
	}

	/**
	 * @param itemIdentifier the itemIdentifier to set
	 */
	public void setItemIdentifier(BigInteger itemIdentifier) {
		this.itemIdentifier = itemIdentifier;
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
	 * @return the status
	 */
	public RE_ItemStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(RE_ItemStatus status) {
		this.status = status;
	}

	/**
	 * @return the dateAccepted
	 */
	public Date getDateAccepted() {
		return dateAccepted;
	}

	/**
	 * @param dateAccepted the dateAccepted to set
	 */
	public void setDateAccepted(Date dateAccepted) {
		this.dateAccepted = dateAccepted;
	}

	/**
	 * @return the dateAmended
	 */
	public Date getDateAmended() {
		return dateAmended;
	}

	/**
	 * @param dateAmended the dateAmended to set
	 */
	public void setDateAmended(Date dateAmended) {
		this.dateAmended = dateAmended;
	}

	/**
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * @param definition the definition to set
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
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
	 * @return the fieldOfApplication
	 */
	public Set<RE_FieldOfApplication> getFieldOfApplication() {
		return fieldOfApplication;
	}

	/**
	 * @param fieldOfApplication the fieldOfApplication to set
	 */
	public void setFieldOfApplication(Set<RE_FieldOfApplication> fieldOfApplication) {
		this.fieldOfApplication = fieldOfApplication;
	}

	/**
	 * @return the alternativeExpressions
	 */
	public Set<RE_AlternativeExpression> getAlternativeExpressions() {
		return alternativeExpressions;
	}

	/**
	 * @param alternativeExpressions the alternativeExpressions to set
	 */
	public void setAlternativeExpressions(Set<RE_AlternativeExpression> alternativeExpressions) {
		this.alternativeExpressions = alternativeExpressions;
	}

	/**
	 * @return the successors
	 */
	public List<RE_RegisterItem> getSuccessors() {
		if (this.successors == null) {
			this.successors = new ArrayList<RE_RegisterItem>();
		}
		return successors;
	}

	/**
	 * @param successors the successors to set
	 */
	public void setSuccessors(Collection<RE_RegisterItem> successors) {
		if (this.successors != null && !this.successors.isEmpty()) {
			throw new RuntimeException("The list of successors must only be set once");
		}
		
		this.successors = new ArrayList<RE_RegisterItem>();
		this.successors.addAll(successors);
	}

	public void setSuccessors(List<RE_RegisterItem> successors) {
		this.setSuccessors((Collection<RE_RegisterItem>)successors);
	}
	
	public void addSuccessors(Collection<RE_RegisterItem> successors) {
		this.successors.addAll(successors);
	}

	/**
	 * @return the predecessors
	 */
	public List<RE_RegisterItem> getPredecessors() {
		if (this.predecessors == null) {
			this.predecessors = new ArrayList<RE_RegisterItem>();
		}
		return Collections.unmodifiableList(predecessors);
	}

	/**
	 * @return
	 */
	public boolean hasPredecessor() {
		return !predecessors.isEmpty();
	}

	/**
	 * @param predecessors the predecessors to set
	 */
	public void setPredecessors(List<RE_RegisterItem> predecessors) {
		this.predecessors = new ArrayList<RE_RegisterItem>();
		this.predecessors.addAll(predecessors);
	}
	
	public void addPredecessors(Collection<RE_RegisterItem> predecessors) {
		if (this.predecessors == null) {
			this.predecessors = new ArrayList<RE_RegisterItem>();
		}
		this.predecessors.addAll(predecessors);
	}

	/**
	 * @return the specificationLineage
	 */
	public Set<RE_Reference> getSpecificationLineage() {
		if (specificationLineage == null) {
			specificationLineage = new HashSet<RE_Reference>();
		}
		return specificationLineage;
	}

	/**
	 * @param specificationLineage the specificationLineage to set
	 */
	protected void setSpecificationLineage(Set<RE_Reference> specificationLineage) {
		this.specificationLineage = specificationLineage;
	}

	/**
	 * @return the specificationSource
	 */
	public RE_Reference getSpecificationSource() {
		return specificationSource;
	}

	/**
	 * @param specificationSource the specificationSource to set
	 */
	public void setSpecificationSource(RE_Reference specificationSource) {
		this.specificationSource = specificationSource;
	}

	/**
	 * @return the itemClass
	 */
	public RE_ItemClass getItemClass() {
		return itemClass;
	}

	/**
	 * @param itemClass the itemClass to set
	 */
	public void setItemClass(RE_ItemClass itemClass) {
		this.itemClass = itemClass;
	}

	/**
	 * @return the additionInformation
	 */
	public Set<RE_AdditionInformation> getAdditionInformation() {
//		return additionInformation;
		if (this.proposalManagementInformations == null) {
			return Collections.EMPTY_SET;
		}
		
		Set<RE_AdditionInformation> result = new HashSet<RE_AdditionInformation>();
		for (RE_ProposalManagementInformation pmi : this.proposalManagementInformations) {
			Object o = de.geoinfoffm.registry.core.Entity.unproxify(pmi);
			if (o instanceof RE_AdditionInformation) {
				result.add((RE_AdditionInformation)o);
			}
		}
		
		return result;
	}

	/**
	 * @param additionInformation the additionInformation to set
	 */
	public void setAdditionInformation(Set<RE_AdditionInformation> additionInformation) {
		this.additionInformation = additionInformation;
	}
	
	public void addAdditionInformation(RE_AdditionInformation additionInformation) {
		if (this.additionInformation == null) {
			this.additionInformation = new HashSet<RE_AdditionInformation>();
		}

		if (additionInformation.getItem() == null) {
			additionInformation.setItem(this);
		}
		else if (!additionInformation.getItem().equals(this)) {
			throw new IllegalStateException("The addition information already belongs to another item");
		}
		this.additionInformation.add(additionInformation);
		
		if (this.proposalManagementInformations == null) {
			this.proposalManagementInformations = new HashSet<RE_ProposalManagementInformation>();
		}
		this.proposalManagementInformations.add(additionInformation);
	}

	/**
	 * @return the clarificationInformation
	 */
	public Set<RE_ClarificationInformation> getClarificationInformation() {
		if (this.proposalManagementInformations == null) {
			return Collections.EMPTY_SET;
		}
		
		Set<RE_ClarificationInformation> result = new HashSet<RE_ClarificationInformation>();
		for (RE_ProposalManagementInformation pmi : this.proposalManagementInformations) {
			Object o = de.geoinfoffm.registry.core.Entity.unproxify(pmi);
			if (o instanceof RE_ClarificationInformation) {
				result.add((RE_ClarificationInformation)o);
			}
		}
		
		return result;
	}

	/**
	 * @param clarificationInformation the clarificationInformation to set
	 */
	public void setClarificationInformation(Set<RE_ClarificationInformation> clarificationInformation) {
		this.clarificationInformation = clarificationInformation;
	}
	
	public void addClarificationInformation(RE_ClarificationInformation clarificationInformation) {
		if (this.clarificationInformation == null) {
			this.clarificationInformation = new HashSet<RE_ClarificationInformation>();
		}
		this.clarificationInformation.add(clarificationInformation);

		if (this.proposalManagementInformations == null) {
			this.proposalManagementInformations = new HashSet<RE_ProposalManagementInformation>();
		}
		this.proposalManagementInformations.add(clarificationInformation);
	}

	/**
	 * @return the amendmentInformation
	 */
	public Set<RE_AmendmentInformation> getAmendmentInformation() {
//		return Collections.unmodifiableSet(amendmentInformation);
		if (this.proposalManagementInformations == null) {
			return Collections.EMPTY_SET;
		}
		
		Set<RE_AmendmentInformation> result = new HashSet<RE_AmendmentInformation>();
		for (RE_ProposalManagementInformation pmi : this.proposalManagementInformations) {
			Object o = de.geoinfoffm.registry.core.Entity.unproxify(pmi);
			if (o instanceof RE_AmendmentInformation) {
				result.add((RE_AmendmentInformation)o);
			}
		}
		
		return result;
	}

	/**
	 * @param amendmentInformation the amendmentInformation to set
	 */
	public void setAmendmentInformation(Set<RE_AmendmentInformation> amendmentInformation) {
		this.amendmentInformation = amendmentInformation;
	}
	
	public void addAmendmentInformation(RE_AmendmentInformation amendmentInformation) {
		if (this.amendmentInformation == null) {
			this.amendmentInformation = new HashSet<RE_AmendmentInformation>();
		}
		this.amendmentInformation.add(amendmentInformation);

		if (this.proposalManagementInformations == null) {
			this.proposalManagementInformations = new HashSet<RE_ProposalManagementInformation>();
		}
		this.proposalManagementInformations.add(amendmentInformation);
	}
	
	public void removeAmendmentInformation(RE_AmendmentInformation amendmentInformation) {
		this.amendmentInformation.remove(amendmentInformation);
	}

	/**
	 * @return the register
	 */
	public RE_Register getRegister() {
		return register;
	}

	/**
	 * @param register the register to set
	 */
	public void setRegister(RE_Register register) {
		this.register = register;
		register.addContainedItem(this);
	}
	
	public RE_RegisterItem submitAsProposal() {
		return this;
	}
	
	public boolean isValid() {
		return this.getStatus().equals(RE_ItemStatus.VALID);
	}
	
	public void finalize() throws Throwable {

	}
	
	public Retirement proposeRetirement(String justification, String registerManagerNotes, String controlBodyNotes, RE_SubmittingOrganization sponsor) {
		RE_AmendmentInformation retirementInformation = new RE_AmendmentInformation();
		retirementInformation.setAmendmentType(RE_AmendmentType.RETIREMENT);
		retirementInformation.setItem(this);
		retirementInformation.setStatus(RE_DecisionStatus.PENDING);
		retirementInformation.setSponsor(sponsor);
		retirementInformation.setJustification(justification);
		retirementInformation.setRegisterManagerNotes(registerManagerNotes);
		retirementInformation.setControlBodyNotes(controlBodyNotes);
		this.addAmendmentInformation(retirementInformation);
		
		return new Retirement(retirementInformation);
	}
	
	public Clarification proposeClarification(Map<String, String[]> proposedChanges, String justification, String registerManagerNotes, String controlBodyNotes, RE_SubmittingOrganization sponsor) {
		RE_ClarificationInformation clarificationInformation = new RE_ClarificationInformation();
		clarificationInformation.setItem(this);
		clarificationInformation.setStatus(RE_DecisionStatus.PENDING);
		clarificationInformation.setSponsor(sponsor);
		clarificationInformation.setJustification(justification);
		clarificationInformation.setRegisterManagerNotes(registerManagerNotes);
		clarificationInformation.setControlBodyNotes(controlBodyNotes);
		
		String proposedChangesJson = RE_ClarificationInformation.toJson(proposedChanges);
		
		clarificationInformation.setProposedChange(new CharacterString(proposedChangesJson));
		
		this.addClarificationInformation(clarificationInformation);

		return new Clarification(clarificationInformation);
	}
	
	public SupersessionPart proposeSupersession(String justification, String registerManagerNotes, String controlBodyNotes, RE_SubmittingOrganization sponsor) {
		RE_AmendmentInformation result = new RE_AmendmentInformation();

		result.setAmendmentType(RE_AmendmentType.SUPERSESSION);
		result.setItem(this);
		result.setStatus(RE_DecisionStatus.PENDING);	
		result.setSponsor(sponsor);
		result.setJustification(justification);
		result.setRegisterManagerNotes(registerManagerNotes);
		result.setControlBodyNotes(controlBodyNotes);
		this.addAmendmentInformation(result);
		
		return new SupersessionPart(result);
	}
	
	
	public boolean isContainedIn(RE_Register register) {
		return this.register.equals(register);
	}
}//end RE_RegisterItem