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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Clarification;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalChangeRequest;
import de.geoinfoffm.registry.core.model.ProposalChangeRequestRepository;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.Retirement;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_Disposition;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.workflow.ProposalWorkflowManager;

/**
 * The class ProposalListItem.
 *
 * @author Florian Esser
 */
public class ProposalListItemImpl implements ProposalListItem
{
	private Proposal proposal;
	private String proposalDate;
	private String dispositionDate;
	private String itemClassName;
	private String proposalStatus;
	private ProposalChangeRequest pendingChangeRequest;
	private boolean isAppealed;
	private String targetRegister;
	private RE_Disposition disposition;
	private ProposalWorkflowManager workflowManager;
	private boolean isSubmitter;

	private Map<String, Object> additionalData;
	private final MessageSource messages;
	private final Locale locale;

	public ProposalListItemImpl(Appeal appeal, MessageSource messages, Locale locale, ProposalWorkflowManager proposalWorkflowManager, ProposalChangeRequestRepository pcrRepository) {
		this(appeal.getAppealedProposal(), messages, locale, proposalWorkflowManager, pcrRepository);
	}
	
	public ProposalListItemImpl(Proposal proposal, MessageSource messages, Locale locale, ProposalWorkflowManager proposalWorkflowManager, ProposalChangeRequestRepository pcrRepository) {
		this.proposal = proposal;
		this.messages = messages;
		this.locale = locale;

		this.workflowManager = proposalWorkflowManager;
		
		this.proposalStatus = proposal.getStatus();

		if (proposal instanceof SimpleProposal) {
			SimpleProposal sp = (SimpleProposal)proposal;
			this.itemClassName = sp.getItemClassName();
		}
		else if (proposal instanceof Supersession) {
			Supersession supersession = (Supersession)proposal;
			RE_ItemClass itemClass = null;
			for (RE_RegisterItem supersededItem : supersession.getSupersededItems()) {
				if (itemClass == null) {
					itemClass = supersededItem.getItemClass();
				}
				else if (!itemClass.equals(supersededItem.getItemClass())) {
					itemClass = null;
					break;
				}
			}
			if (itemClass != null) {
				this.itemClassName = itemClass.getName();
			}
		}
		
		Set<String> targetRegisters = new HashSet<>();
		StringBuilder targetRegisterBuilder = new StringBuilder();
		for (RE_Register register : proposal.getAffectedRegisters()) {
			if (targetRegisters.contains(register.getName())) continue;
			
			if (targetRegisterBuilder.length() > 0) {
				targetRegisterBuilder.append(", ");
			}
			targetRegisterBuilder.append(register.getName());
			targetRegisters.add(register.getName());
		}
		this.targetRegister = targetRegisterBuilder.toString();

		if (workflowManager.isAppealable(proposal) || workflowManager.isAppealed(proposal) || workflowManager.isFinal(proposal)) {
			if (!proposal.getProposalManagementInformations().isEmpty()) {
				this.disposition = proposal.getDisposition();
				Date dateDisposed = proposal.getProposalManagementInformations().get(0).getDateDisposed();
				DateFormat dateOnly = new SimpleDateFormat("dd.MM.yyyy");
				this.dispositionDate = (dateDisposed == null) ? "" : dateOnly.format(dateDisposed);
			}
		}
		
		Collection<ProposalChangeRequest> pcrs = pcrRepository.findByProposalAndReviewedIsFalse(proposal);
		if (!pcrs.isEmpty()) {
			this.pendingChangeRequest = (ProposalChangeRequest)pcrs.toArray()[0];
			this.proposalStatus = "PENDING_CHANGE"; // Special status for proposals with pending change requests
		}
	}
	
	@Override
	@JsonIgnore
	public Proposal getProposal() {
		return proposal;
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#getTitle()
	 */
	@Override
	public String getTitle() {
		return proposal.getTitle();
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#getProposalUuid()
	 */
	@Override
	public UUID getProposalUuid() {
		return proposal.getUuid();
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#getProposalDate()
	 */
	@Override
	public String getDateSubmitted() {
		DateFormat dateAndTime = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		DateFormat dateOnly = new SimpleDateFormat("dd.MM.yyyy");

		return (proposal.getDateSubmitted() == null ? "" : dateOnly.format(proposal.getDateSubmitted()));
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#getItemClassName()
	 */
	@Override
	public String getItemClassName() {
		return messages.getMessage(this.itemClassName, null, this.itemClassName, locale);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#getSponsorName()
	 */
	@Override
	public String getSponsorName() {
		return proposal.getSponsor().getName();
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#getSponsorUuid()
	 */
	@Override
	public UUID getSponsorUuid() {
		return proposal.getSponsor().getUuid();
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#getProposalStatus()
	 */
	@Override
	public String getProposalStatus() {
		return messages.getMessage(this.getTechnicalProposalStatus(), null, this.getTechnicalProposalStatus(), locale);	
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#getTechnicalProposalStatus()
	 */
	@Override
	@JsonProperty
	public String getTechnicalProposalStatus() {
		return this.proposalStatus;
	}
	
	@Override
	public void overrideProposalStatus(String newStatus) {
		this.proposalStatus = newStatus;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#getProposalType()
	 */
	@Override
	public String getProposalType() {
		return messages.getMessage(proposal.getClass().getName(), null, proposal.getClass().getName(), locale);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#getPendingChangeRequest()
	 */
	@Override
	@JsonIgnore
	public ProposalChangeRequest getPendingChangeRequest() {
		return pendingChangeRequest;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#getDispositionDate()
	 */
	@Override
	public String getDispositionDate() {
		return dispositionDate;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#getTargetRegister()
	 */
	@Override
	public String getTargetRegister() {
		return targetRegister;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#getDisposition()
	 */
	@Override
	@JsonProperty
	public String getDisposition() {
		if (this.disposition == null) {
			return "";
		}
		else {
			return messages.getMessage(this.disposition.name(), null, this.disposition.name(), locale);
		}
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#getAdditionalData()
	 */
	@Override
	@JsonProperty
	public Map<String, Object> getAdditionalData() {
		if (this.additionalData == null) {
			this.additionalData = new HashMap<>();
		}
		
		return additionalData;
	}
	
	@Override
	@JsonProperty
	public boolean isSubmitter() {
		return this.isSubmitter;
	}
	
	@Override
	public void setSubmitter(boolean submitter) {
		this.isSubmitter = submitter;
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#isAppealed()
	 */
	@Override
	@JsonProperty
	public boolean isAppealed() {
		return isAppealed;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#isNotSubmitted()
	 */
	@Override
	@JsonProperty
	public boolean isNotSubmitted() {
		return !workflowManager.isSubmitted(this.proposalStatus);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#isUnderReview()
	 */
	@Override
	@JsonProperty
	public boolean isUnderReview() {
		return workflowManager.isUnderReview(this.proposalStatus);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#isPending()
	 */
	@Override
	@JsonProperty
	public boolean isPending() {
		return workflowManager.isPending(this.proposalStatus);
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#isAppealable()
	 */
	@Override
	@JsonProperty
	public boolean isAppealable() {
		return workflowManager.isAppealable(proposalStatus) && !this.isAppealed;
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#isWithdrawable()
	 */
	@Override
	@JsonProperty
	public boolean isWithdrawable() {
		return isPending() || isUnderReview();
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#isFinished()
	 */
	@Override
	@JsonProperty
	public boolean isFinished() {
		return workflowManager.isFinal(proposalStatus);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#isAddition()
	 */
	@Override
	@JsonProperty
	public boolean isAddition() {
		return Addition.class.getName().equals(this.getProposalType());
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#isClarification()
	 */
	@Override
	@JsonProperty
	public boolean isClarification() {
		return Clarification.class.getName().equals(this.getProposalType());
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#isSupersession()
	 */
	@Override
	@JsonProperty
	public boolean isSupersession() {
		return Supersession.class.getName().equals(this.getProposalType());
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#isRetirement()
	 */
	@Override
	@JsonProperty
	public boolean isRetirement() {
		return Retirement.class.getName().equals(this.getProposalType());
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#isGroup()
	 */
	@Override
	@JsonProperty
	public boolean isGroup() {
		return ProposalGroup.class.getName().equals(proposal.getClass().getName());
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ProposalListItem#isPendingChangeRequest()
	 */
	@Override
	@JsonProperty
	public boolean isPendingChangeRequest() {
		return this.pendingChangeRequest != null;
	}
	
	@Override
	@JsonProperty("DT_RowId")
	public String rowId() {
		return "proposalrow-" + proposal.getUuid().toString();
	}
}
