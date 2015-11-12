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
package de.geoinfoffm.registry.api;

import static de.geoinfoffm.registry.api.iso.IsoXmlFactory.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.AppealDisposition;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalChangeRequest;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.ProposalType;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentType;
import de.geoinfoffm.registry.core.model.iso19135.RE_ClarificationInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Disposition;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.workflow.ProposalWorkflowManager;

/**
 * The class RegisterItemViewBean.
 *
 * @author Florian Esser
 */
public class RegisterItemViewBean
{	
	protected static final org.isotc211.iso19139.common.ObjectFactory gcoObjectFactory = new org.isotc211.iso19139.common.ObjectFactory();
	protected static final org.isotc211.iso19135.ObjectFactory grgObjectFactory = new org.isotc211.iso19135.ObjectFactory();

	private UUID uuid;
	private Class<?> itemClass;
	
	private BigInteger itemIdentifier;
	private String name;
	private String definition;
	private String description;
	private String status;
	
	private boolean isEditable;

	private UUID itemClassUuid;
	private String itemClassName;

	private UUID registerUuid;
	private String registerName;

	private boolean isProposal;
	private boolean isProposalGroup;
	private boolean hasParent;
	private boolean hasDependentProposals;
	private ProposalType proposalType;
	private String justification;
	private String registerManagerNotes;
	private String controlBodyNotes;
	private Map<String, List<String>> proposedChange;
	private Date proposalDate;
	private Date dateSubmitted;
	private RE_Disposition disposition;
	private RE_DecisionStatus decisionStatus;
	private Date dateAccepted;
	private Date dateAmended;
	private boolean isAppealed;
	
	private UUID proposalUuid;
	private Class<?> proposalClass;
	private String proposalStatus;
	
	private ProposalChangeRequest pendingChangeRequest;
	
	private AppealDisposition appealDisposition;
	
	private UUID sponsorUuid;
	private String sponsorName;
	
	private ProposalWorkflowManager workflowManager;
	
	@JsonIgnore
	private final Set<RegisterItemViewBean> supersededItems = new HashSet<RegisterItemViewBean>();
	@JsonIgnore
	private final Set<RegisterItemViewBean> supersedingItems = new HashSet<RegisterItemViewBean>(); 
	
	private final Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	@JsonIgnore
	private final Set<RE_AdditionInformation> additionInformations = new LinkedHashSet<RE_AdditionInformation>();
	@JsonIgnore
	private final Set<RE_AmendmentInformation> amendmentInformations = new LinkedHashSet<RE_AmendmentInformation>();
	@JsonIgnore
	private final Set<RE_ClarificationInformation> clarificationInformations = new LinkedHashSet<RE_ClarificationInformation>();

	private final Map<UUID, String> predecessors = new LinkedHashMap<UUID, String>(); 
	private final Map<UUID, String> successors = new LinkedHashMap<UUID, String>(); 
	
	private UUID parentProposalUuid;
	private final Set<RegisterItemViewBean> memberProposals = new HashSet<RegisterItemViewBean>();

	protected RegisterItemViewBean(RE_RegisterItem item) {
		this(item, true);
	}

	protected RegisterItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		initializeFromItem(item, loadDetails);
	}

	protected RegisterItemViewBean(Proposal proposal) {
		if (proposal.hasParent()) {
			this.setParentProposalUuid(proposal.getParent().getUuid());
			this.hasParent = true;
		}
		
		if (proposal.hasDependentProposals()) {
			for (Proposal dependentProposal : proposal.getDependentProposals()) {
				this.getMemberProposals().add(new RegisterItemViewBean(dependentProposal));
			}
			this.hasDependentProposals = true;
		}
		
		this.setDateSubmitted(proposal.getDateSubmitted());
		
		if (proposal instanceof SimpleProposal) {
			initializeFromSimpleProposal((SimpleProposal)proposal);
		}
		else if (proposal instanceof Supersession) {
			initializeFromSupersession((Supersession)proposal);
		}
		else if (proposal instanceof ProposalGroup) {
			initializeFromGroup((ProposalGroup)proposal);
		}
	}

	protected RegisterItemViewBean(SimpleProposal proposal) {
		initializeFromSimpleProposal(proposal);
	}
	
	protected RegisterItemViewBean(Appeal appeal) {
		initializeFromAppeal(appeal);
	}
	
	protected RegisterItemViewBean(Supersession supersession) {
		initializeFromSupersession(supersession);
	}

	public static RegisterItemViewBean forAppeal(Appeal appeal, ProposalWorkflowManager workflowManager) {
		RegisterItemViewBean result = new RegisterItemViewBean(appeal);
		result.setWorkflowManager(workflowManager);
		
		return result;
	}
	
	public static RegisterItemViewBean forItem(RE_RegisterItem item, boolean loadDetails, ProposalWorkflowManager workflowManager) {
		RegisterItemViewBean result = new RegisterItemViewBean(item, loadDetails);
		result.setWorkflowManager(workflowManager);
		
		return result;
	}
	
	public static <T extends RegisterItemViewBean> T forItem(RE_RegisterItem item, boolean loadDetails, Class<T> viewBeanType, ProposalWorkflowManager workflowManager) {
		T result;
		try {
			result = ConstructorUtils.invokeConstructor(viewBeanType, new Object[] { item, loadDetails }, new Class<?>[] { RE_RegisterItem.class, Boolean.class });
		}
		catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		result.setWorkflowManager(workflowManager);
		
		return result;
	}

	
	public static RegisterItemViewBean forProposal(Proposal proposal, ProposalWorkflowManager workflowManager) {
		RegisterItemViewBean result = new RegisterItemViewBean(proposal);
		result.setWorkflowManager(workflowManager);
		result.setEditable(workflowManager.isEditable(proposal));
		
		return result;
	}

	public static <T extends RegisterItemViewBean> T forProposal(Proposal proposal, Class<T> viewBeanType, ProposalWorkflowManager workflowManager) {
		T result;
		try {
			result = ConstructorUtils.invokeConstructor(viewBeanType, new Object[] { proposal }, new Class<?>[] { Proposal.class });
		}
		catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		result.setWorkflowManager(workflowManager);
		result.setEditable(workflowManager.isEditable(proposal));
		
		return result;
	}

	protected void setWorkflowManager(ProposalWorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	/**
	 * May be overwritten by extending classes
	 */
	protected void addAdditionalProperties(RE_RegisterItem item, boolean loadDetails) {
		// does nothing here
	}
	
	public RE_RegisterItem_Type toXmlType(ProposalDtoFactory dtoFactory) {
		RE_RegisterItem_Type result = this.createXmlType(dtoFactory);
		this.setXmlValues(result);
		
		return result;
	}
	
	protected RE_RegisterItem_Type createXmlType(ProposalDtoFactory proposalDtoFactory) {
		return proposalDtoFactory.getXmlType(this.getItemClassName());
	}
	
	public void setXmlValues(RE_RegisterItem_Type result) {
		result.setDateAccepted(date(this.getDateAccepted()));
		result.setDateAmended(date(this.getDateAmended()));
		result.setDefinition(characterString(this.getDefinition()));
		result.setDescription(characterString(this.getDescription()));
		result.setItemClass(itemClass(this.getItemClassUuid()));
		result.setItemIdentifier(integer(this.getItemIdentifier()));
		result.setName(characterString(this.getName()));
		// result.setSpecificationSource(...);  // TODO
		result.setStatus(itemStatus(this.getStatus()));
		result.setUuid(this.uuid.toString());
		
		for (RE_AdditionInformation adnInfo : this.getAdditionInformations()) {
			result.getAdditionInformation().add(additionInformation(adnInfo));
		}
		
		for (RE_ClarificationInformation cnInfo : this.getClarificationInformations()) {
			result.getClarificationInformation().add(clarificationInformation(cnInfo));
		}
		
		for (RE_AmendmentInformation amtInfo : this.getAmendmentInformations()) {
			result.getAmendmentInformation().add(amendmentInformation(amtInfo));
		}

		// TODO
//		for (RE_AlternativeExpression altExp : this.getAlternativeExpressions()) {
//			
//		}
		
		// TODO
//		result.getFieldOfApplication().add(...)
		
		for (UUID predecessorUuid : this.getPredecessors().keySet()) {
			result.getPredecessor().add(registerItem(predecessorUuid));
		}
		
		for (UUID successorUuid : this.getSuccessors().keySet()) {
			result.getSuccessor().add(registerItem(successorUuid));
		}
		
		// TODO
//		result.getSpecificationLineage().add(...)
	}

	/**
	 * @param item
	 * @param loadDetails 
	 */
	private void initializeFromItem(RE_RegisterItem item, boolean loadDetails) {
		this.uuid = item.getUuid();
		this.itemClass = item.getClass();
		this.itemIdentifier = item.getItemIdentifier();
		this.name = item.getName();
		this.definition = item.getDefinition();
		this.description = item.getDescription();
		this.status = item.getStatus().name();
		
		this.itemClassUuid = item.getItemClass().getUuid();
		this.itemClassName = item.getItemClass().getName();

		if (item.getRegister() != null) {
			this.registerUuid = item.getRegister().getUuid();
			this.registerName = item.getRegister().getName();
		}
		
		this.isProposal = false;
		this.setAppealed(false);
		
		this.dateAccepted = item.getDateAccepted();
		this.dateAmended = item.getDateAmended();
		
		if (loadDetails) {
			for (RE_AdditionInformation ai : item.getAdditionInformation()) {
				if (ai.isFinal()) {
					initializeFromProposalManagementInformation(ai);
					break;
				}
			}
		
			this.additionInformations.addAll(item.getAdditionInformation());
			this.clarificationInformations.addAll(item.getClarificationInformation());
			this.amendmentInformations.addAll(item.getAmendmentInformation());
			
			for (RE_RegisterItem predecessor : item.getPredecessors()) {
				this.predecessors.put(predecessor.getUuid(), predecessor.getName());
			}
	
			for (RE_RegisterItem successor : item.getSuccessors()) {
				this.successors.put(successor.getUuid(), successor.getName());
			}
		}
		
		this.addAdditionalProperties(item, loadDetails);
}
	
	private void initializeFromProposalManagementInformation(RE_ProposalManagementInformation proposal) {
		if (!proposal.isFinal()) {
			this.isProposal = true;
		}
		
		if (proposal instanceof HibernateProxy) {
			Hibernate.initialize(proposal);
	        proposal = (RE_ProposalManagementInformation)((HibernateProxy) proposal)
	                  .getHibernateLazyInitializer()
	                  .getImplementation();
		}

		this.proposalDate = proposal.getDateProposed();
		if (proposal instanceof RE_AdditionInformation) {
			this.proposalType = ProposalType.ADDITION;
		}
		else if (proposal instanceof RE_ClarificationInformation) {
			this.proposalType = ProposalType.CLARIFICATION;
			this.setProposedChange(RE_ClarificationInformation.fromJson(CharacterString.asString(((RE_ClarificationInformation)proposal).getProposedChange())));
		}	
		else if (proposal instanceof RE_AmendmentInformation && ((RE_AmendmentInformation)proposal).getAmendmentType().equals(RE_AmendmentType.RETIREMENT)) {
			this.proposalType = ProposalType.RETIREMENT;
		}

//		this.setProposalStatus(ProposalStatus.UNDER_REVIEW);
//		if (proposal.isPending()) {
//			this.setProposalStatus(ProposalStatus.IN_APPROVAL_PROCESS);
//		}
//		if (proposal.isTentative()) {
//			this.setProposalStatus(ProposalStatus.APPEALABLE);
//		}
//		if (proposal.isFinal()) {
//			this.setProposalStatus(ProposalStatus.FINISHED);
//		}

		this.justification = proposal.getJustification();
		this.sponsorUuid = proposal.getSponsor().getUuid();
		this.sponsorName = proposal.getSponsor().getName(); 
		
		this.setRegisterManagerNotes(proposal.getRegisterManagerNotes());
		this.setControlBodyNotes(proposal.getControlBodyNotes());

		this.setDisposition(proposal.getDisposition());
		this.setDecisionStatus(proposal.getStatus());		
	}
	
	private void initializeFromSimpleProposal(SimpleProposal proposal) {
		initializeFromItem(proposal.getItem(), true);
		initializeFromProposalManagementInformation(proposal.getProposalManagementInformation());
		this.setProposalUuid(proposal.getUuid());
		this.setProposalClass(proposal.getClass());
		this.setProposalStatus(proposal.getStatus());
	}

	private void initializeFromGroup(ProposalGroup group) {
		this.setProposalUuid(group.getUuid());
		this.setProposalClass(group.getClass());
		this.setName(group.getTitle());

		this.setProposalStatus(group.getStatus());

		if (group.isSubmitted()) {
			initializeFromProposalManagementInformation(group.getProposalManagementInformations().get(0));
		}
		
		this.isProposalGroup = true;
		this.setProposalType(ProposalType.GROUP);
		if (!group.getAffectedRegisters().isEmpty()) {
			this.setRegisterUuid(group.getAffectedRegisters().iterator().next().getUuid());
			this.setRegisterName(group.getAffectedRegisters().iterator().next().getName());
		}
		
		for (Proposal proposal : group.getProposals()) {
			RegisterItemViewBean member  = new RegisterItemViewBean(proposal);
			this.getMemberProposals().add(member);
		}
	}

	private void initializeFromSupersession(Supersession supersession) {
		initializeFromProposalManagementInformation(supersession.getProposalManagementInformations().get(0));
		
		this.setProposalUuid(supersession.getUuid());
		this.proposalClass = supersession.getClass();
		this.setName(supersession.getTitle());
		this.setRegisterUuid(supersession.getTargetRegister().getId());
		
		if (!supersession.getSupersessionParts().isEmpty()) {
			this.setRegisterManagerNotes(supersession.getSupersessionParts().get(0).getRegisterManagerNotes());
			this.setControlBodyNotes(supersession.getSupersessionParts().get(0).getControlBodyNotes());
		}
		
		for (RE_RegisterItem si : supersession.getSupersededItems()) {
			this.supersededItems.add(new RegisterItemViewBean(si));
		}
		for (RE_RegisterItem si : supersession.getSupersedingItems()) {
			this.supersedingItems.add(new RegisterItemViewBean(si));
		}
		
		this.setProposalType(ProposalType.SUPERSESSION);
		
		this.setProposalStatus(supersession.getStatus());
	}

	private void initializeFromAppeal(Appeal appeal) {
		Proposal appealedProposal = appeal.getAppealedProposal();
		if (appealedProposal instanceof SimpleProposal) {
			initializeFromSimpleProposal((SimpleProposal)appealedProposal);
		}
		else if (appealedProposal instanceof Supersession) {
			initializeFromSupersession((Supersession)appealedProposal);
		}
		else if (appealedProposal instanceof ProposalGroup) {
			initializeFromGroup((ProposalGroup)appealedProposal);
		}
		
		this.isAppealed = true;
//		if (appeal.isDecided()) {
//			this.setProposalStatus(ProposalStatus.FINISHED);
//		}
//		else {
//			this.setProposalStatus(ProposalStatus.APPEALED);			
//		}
		this.appealDisposition = appeal.getDisposition();
	}
	
	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Class<?> getItemClass() {
		return itemClass;
	}

	public void setItemClass(Class<?> itemClass) {
		this.itemClass = itemClass;
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the disposition
	 */
	public RE_Disposition getDisposition() {
		return disposition;
	}

	/**
	 * @param disposition the disposition to set
	 */
	public void setDisposition(RE_Disposition disposition) {
		this.disposition = disposition;
	}

	/**
	 * @return the proposalType
	 */
	public ProposalType getProposalType() {
		return proposalType;
	}

	/**
	 * @param proposalType the proposalType to set
	 */
	public void setProposalType(ProposalType proposalType) {
		this.proposalType = proposalType;
	}

	/**
	 * @return the proposalDate
	 */
	public Date getProposalDate() {
		return proposalDate;
	}

	/**
	 * @param proposalDate the proposalDate to set
	 */
	public void setProposalDate(Date proposalDate) {
		this.proposalDate = proposalDate;
	}

	public Date getDateSubmitted() {
		return dateSubmitted;
	}

	public void setDateSubmitted(Date dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	/**
	 * @return the decisionStatus
	 */
	public RE_DecisionStatus getDecisionStatus() {
		return decisionStatus;
	}

	/**
	 * @param decisionStatus the decisionStatus to set
	 */
	public void setDecisionStatus(RE_DecisionStatus decisionStatus) {
		this.decisionStatus = decisionStatus;
	}

	public Date getDateAccepted() {
		return dateAccepted;
	}

	public void setDateAccepted(Date dateAccepted) {
		this.dateAccepted = dateAccepted;
	}

	public Date getDateAmended() {
		return dateAmended;
	}

	public void setDateAmended(Date dateAmended) {
		this.dateAmended = dateAmended;
	}

	/**
	 * @return the itemClassUuid
	 */
	public UUID getItemClassUuid() {
		return itemClassUuid;
	}

	/**
	 * @param itemClassUuid the itemClassUuid to set
	 */
	public void setItemClassUuid(UUID itemClassUuid) {
		this.itemClassUuid = itemClassUuid;
	}

	/**
	 * @return the itemClassName
	 */
	public String getItemClassName() {
		return itemClassName;
	}

	/**
	 * @param itemClassName the itemClassName to set
	 */
	public void setItemClassName(String itemClassName) {
		this.itemClassName = itemClassName;
	}

	/**
	 * @return the isProposal
	 */
	public boolean isProposal() {
		return isProposal;
	}

	public boolean isProposalGroup() {
		return isProposalGroup;
	}

	public void setProposalGroup(boolean isProposalGroup) {
		this.isProposalGroup = isProposalGroup;
	}

	public boolean hasParent() {
		return hasParent;
	}

	public void setHasParent(boolean hasParent) {
		this.hasParent = hasParent;
	}

	public boolean hasDependentProposals() {
		return hasDependentProposals;
	}

	public void setHasDependentProposals(boolean hasDependentProposals) {
		this.hasDependentProposals = hasDependentProposals;
	}

	/**
	 * @return the isEditable
	 */
	public boolean isEditable() {
		return isEditable;
	}

	/**
	 * @param isEditable the isEditable to set
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	/**
	 * @param isProposal the isProposal to set
	 */
	public void setProposal(boolean isProposal) {
		this.isProposal = isProposal;
	}

	/**
	 * @return the sponsorUuid
	 */
	public UUID getSponsorUuid() {
		return sponsorUuid;
	}

	/**
	 * @param sponsorUuid the sponsorUuid to set
	 */
	public void setSponsorUuid(UUID sponsorUuid) {
		this.sponsorUuid = sponsorUuid;
	}

	/**
	 * @return the sponsorName
	 */
	public String getSponsorName() {
		return sponsorName;
	}

	/**
	 * @param sponsorName the sponsorName to set
	 */
	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	/**
	 * @return the isAppealed
	 */
	public boolean isAppealed() {
		return isAppealed;
	}

	/**
	 * @param isAppealed the isAppealed to set
	 */
	public void setAppealed(boolean isAppealed) {
		this.isAppealed = isAppealed;
	}

	/**
	 * @return the proposalUuid
	 */
	public UUID getProposalUuid() {
		return proposalUuid;
	}

	/**
	 * @param proposalUuid the proposalUuid to set
	 */
	public void setProposalUuid(UUID proposalUuid) {
		this.proposalUuid = proposalUuid;
	}

	public Class<?> getProposalClass() {
		return proposalClass;
	}

	public void setProposalClass(Class<?> proposalClass) {
		this.proposalClass = proposalClass;
	}

	/**
	 * @return the proposalStatus
	 */
	public String getProposalStatus() {
		return proposalStatus;
	}

	/**
	 * @param proposalStatus the proposalStatus to set
	 */
	public void setProposalStatus(String proposalStatus) {
		this.proposalStatus = proposalStatus;
	}

	public ProposalChangeRequest getPendingChangeRequest() {
		return pendingChangeRequest;
	}

	public void setPendingChangeRequest(ProposalChangeRequest pendingChangeRequest) {
		this.pendingChangeRequest = pendingChangeRequest;
	}

	/**
	 * @return the justification
	 */
	public String getJustification() {
		return justification;
	}

	/**
	 * @param justification the justification to set
	 */
	public void setJustification(String justification) {
		this.justification = justification;
	}

	/**
	 * @return the registerManagerNotes
	 */
	public String getRegisterManagerNotes() {
		return registerManagerNotes;
	}

	/**
	 * @param registerManagerNotes the registerManagerNotes to set
	 */
	public void setRegisterManagerNotes(String registerManagerNotes) {
		this.registerManagerNotes = registerManagerNotes;
	}

	/**
	 * @return the controlBodyNotes
	 */
	public String getControlBodyNotes() {
		return controlBodyNotes;
	}

	/**
	 * @param controlBodyNotes the controlBodyNotes to set
	 */
	public void setControlBodyNotes(String controlBodyNotes) {
		this.controlBodyNotes = controlBodyNotes;
	}

	/**
	 * @return the proposedChange
	 */
	public Map<String, List<String>> getProposedChange() {
		return proposedChange;
	}

	/**
	 * @param proposedChange the proposedChange to set
	 */
	public void setProposedChange(Map<String, List<String>> proposedChange) {
		this.proposedChange = proposedChange;
	}

	/**
	 * @return the appealDisposition
	 */
	public AppealDisposition getAppealDisposition() {
		return appealDisposition;
	}

	/**
	 * @param appealDisposition the appealDisposition to set
	 */
	public void setAppealDisposition(AppealDisposition appealDisposition) {
		this.appealDisposition = appealDisposition;
	}

	/**
	 * @return the registers
	 */
	public UUID getRegisterUuid() {
		return registerUuid;
	}

	/**
	 * @return the targetRegister
	 */
	public String getRegisterName() {
		return registerName;
	}

	/**
	 * @param targetRegister the targetRegister to set
	 */
	public void setRegisterName(String registerName) {
		this.registerName = registerName;
	}

	/**
	 * @return the supersededItems
	 */
	public Set<RegisterItemViewBean> getSupersededItems() {
		return supersededItems;
	}

	/**
	 * @return the supersedingItems
	 */
	public Set<RegisterItemViewBean> getSupersedingItems() {
		return supersedingItems;
	}

	/**
	 * @return the additionalProperties
	 */
	public Map<String, Object> getAdditionalProperties() {
		return Collections.unmodifiableMap(additionalProperties);
	}
	
	public void addAdditionalProperty(String propertyName, Object value) {
		this.additionalProperties.put(propertyName, value);
	}
	
	/**
	 * Returns all proposal management information records sorted by date.
	 * 
	 * @return
	 */
	public SortedSet<RE_ProposalManagementInformation> getSortedProposalManagementInformations() {
		SortedSet<RE_ProposalManagementInformation> result = new TreeSet<RE_ProposalManagementInformation>(new Comparator<RE_ProposalManagementInformation>() {

			@Override
			public int compare(RE_ProposalManagementInformation o1, RE_ProposalManagementInformation o2) {
				int result = getRelevantDate(o1).compareTo(getRelevantDate(o2));
				if (result == 0) {
					result = 1;
				}
				
				return result;
			}

			public Date getRelevantDate(RE_ProposalManagementInformation pmi) {
				Date result;
				if (pmi.getDateDisposed() != null) {
					result = pmi.getDateDisposed();
				}
				else if (pmi.getDateProposed() != null) {
					result = pmi.getDateProposed();
				}
				else {
					result = Calendar.getInstance().getTime();
				}
				
				return result;
			}
		});
		
		result.addAll(additionInformations);
		result.addAll(clarificationInformations);
		result.addAll(amendmentInformations);
		
		return result;
	}

	/**
	 * @return the additionInformations
	 */
	public Set<RE_AdditionInformation> getAdditionInformations() {
		return additionInformations;
	}

	/**
	 * @return the amendmentInformations
	 */
	public Set<RE_AmendmentInformation> getAmendmentInformations() {
		return amendmentInformations;
	}

	/**
	 * @return the clarificationInformations
	 */
	public Set<RE_ClarificationInformation> getClarificationInformations() {
		return clarificationInformations;
	}

	/**
	 * @param registerUuid the registerUuid to set
	 */
	public void setRegisterUuid(UUID registerUuid) {
		this.registerUuid = registerUuid;
	}

	public UUID getParentProposalUuid() {
		return parentProposalUuid;
	}
	
	public void setParentProposalUuid(UUID uuid) {
		this.parentProposalUuid = uuid;
	}

	public Set<RegisterItemViewBean> getMemberProposals() {
		return memberProposals;
	}

	/**
	 * @return the predecessors
	 */
	public Map<UUID, String> getPredecessors() {
		return predecessors;
	}

	/**
	 * @return the successors
	 */
	public Map<UUID, String> getSuccessors() {
		return successors;
	}

	@JsonIgnore
	public boolean isNotSubmitted() {
		return !workflowManager.isSubmitted(this.proposalStatus);
	}

	@JsonIgnore
	public boolean isUnderReview() {
		return workflowManager.isUnderReview(proposalStatus);
	}

	@JsonIgnore
	public boolean isPending() {
		return workflowManager.isPending(proposalStatus);
	}
	
	@JsonIgnore
	public boolean isAppealable() {
		return workflowManager.isAppealable(proposalStatus) && !this.isAppealed();
	}
	
	@JsonIgnore
	public boolean isWithdrawable() {
		return workflowManager.isWithdrawable(proposalStatus);
	}

	@JsonIgnore
	public boolean isFinished() {
		return workflowManager.isFinal(proposalStatus);
	}

	@JsonIgnore
	public boolean isWithdrawn() {
		return workflowManager.isWithdrawn(proposalStatus);
//		return this.getDisposition().equals(RE_Disposition.WITHDRAWN);
	}

	public boolean isValid() {
		return RE_ItemStatus.VALID.name().equals(this.getStatus());
	}
	
	@JsonIgnore
	public boolean isAddition() {
		return this.getProposalType().equals(ProposalType.ADDITION);
	}

	@JsonIgnore
	public boolean isClarification() {
		return this.getProposalType().equals(ProposalType.CLARIFICATION);
	}
	
	@JsonIgnore
	public boolean isSupersession() {
		return this.getProposalType().equals(ProposalType.SUPERSESSION);
	}

	@JsonIgnore
	public boolean isRetirement() {
		return this.getProposalType().equals(ProposalType.RETIREMENT);
	}
}
