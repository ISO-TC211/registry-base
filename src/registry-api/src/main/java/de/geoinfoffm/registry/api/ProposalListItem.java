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
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.Clarification;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalChangeRequest;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.Retirement;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.workflow.ProposalWorkflowManager;

/**
 * The class ProposalListItem.
 *
 * @author Florian Esser
 */
public class ProposalListItem
{
	private UUID uuid;
	private String title;
	private String proposalDate;
	private String dispositionDate;
	private String itemClassName;
	private String sponsorName;
	private UUID sponsorUuid;
	private String proposalStatus;
	private String proposalType;
	private ProposalChangeRequest pendingChangeRequest;
	private boolean isAppealed;
	private String targetRegister;
	
	private MessageSource messages;
	private Locale locale;
	
	private ProposalWorkflowManager workflowManager;
	
	public ProposalListItem(Proposal proposal, MessageSource messages, Locale locale, ProposalWorkflowManager proposalWorkflowManager) {
		this.uuid = proposal.getUuid();
		this.workflowManager = proposalWorkflowManager;

		DateFormat dateAndTime = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		DateFormat dateOnly = new SimpleDateFormat("dd.MM.yyyy");

		this.proposalDate = (proposal.getDateSubmitted() == null ? "" : dateAndTime.format(proposal.getDateSubmitted()));

		this.title = proposal.getTitle();
		if (proposal instanceof SimpleProposal) {
			SimpleProposal sp = (SimpleProposal)proposal;
			this.itemClassName = sp.getItemClassName();
		}
		this.proposalStatus = proposal.getStatus();
		
		this.proposalType = proposal.getClass().getName();

		StringBuilder targetRegisterBuilder = new StringBuilder();
		for (RE_Register register : proposal.getAffectedRegisters()) {
			if (targetRegisterBuilder.length() > 0) {
				targetRegisterBuilder.append(", ");
			}
			targetRegisterBuilder.append(register.getName());
		}
		this.targetRegister = targetRegisterBuilder.toString();

		if (workflowManager.isFinal(proposal)) {
			if (!proposal.getProposalManagementInformations().isEmpty()) {
				Date dateDisposed = proposal.getProposalManagementInformations().get(0).getDateDisposed();
				this.dispositionDate = (dateDisposed == null) ? "" : dateOnly.format(dateDisposed);
			}
		}
		
		this.sponsorName = proposal.getSponsor().getName();
		this.sponsorUuid = proposal.getSponsor().getUuid();
		
		this.messages = messages;
		this.locale = locale;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public UUID getProposalUuid() {
		return getUuid();
	}
	
	public String getTitle() {
		return title;
	}

	public String getProposalDate() {
		return proposalDate;
	}

	public String getItemClassName() {
		return messages.getMessage(this.itemClassName, null, this.itemClassName, locale);
	}

	public String getSponsorName() {
		return sponsorName;
	}
	
	public UUID getSponsorUuid() {
		return sponsorUuid;
	}

	public String getProposalStatus() {
		return messages.getMessage(this.proposalStatus, null, this.proposalStatus, locale);	
	}
	
	@JsonProperty
	public String getTechnicalProposalStatus() {
		return this.proposalStatus;
	}

	public String getProposalType() {
		return messages.getMessage(this.proposalType, null, this.proposalType, locale);
	}

	@JsonIgnore
	public ProposalChangeRequest getPendingChangeRequest() {
		return pendingChangeRequest;
	}

	public void setPendingChangeRequest(ProposalChangeRequest pendingChangeRequest) {
		this.pendingChangeRequest = pendingChangeRequest;
		this.proposalStatus = "PENDING_CHANGE"; // Special status for proposals with pending change requests
	}

	public String getDispositionDate() {
		return dispositionDate;
	}

	public void setDispositionDate(String dispositionDate) {
		this.dispositionDate = dispositionDate;
	}

	public String getTargetRegister() {
		return targetRegister;
	}

	public void setTargetRegister(String targetRegister) {
		this.targetRegister = targetRegister;
	}

	@JsonProperty
	public boolean isAppealed() {
		return isAppealed;
	}

	public void setAppealed(boolean isAppealed) {
		this.isAppealed = isAppealed;
	}

	@JsonProperty
	public boolean isNotSubmitted() {
		return !workflowManager.isSubmitted(this.proposalStatus);
	}

	@JsonProperty
	public boolean isUnderReview() {
		return workflowManager.isUnderReview(this.proposalStatus);
	}

	@JsonProperty
	public boolean isPending() {
		return workflowManager.isPending(this.proposalStatus);
	}
	
	@JsonProperty
	public boolean isAppealable() {
		return workflowManager.isAppealable(proposalStatus) && !this.isAppealed;
	}
	
	@JsonProperty
	public boolean isWithdrawable() {
		return isPending() || isUnderReview();
	}

	@JsonProperty
	public boolean isFinished() {
		return workflowManager.isFinal(proposalStatus);
	}

	@JsonProperty
	public boolean isAddition() {
		return Addition.class.getName().equals(this.proposalType);
	}

	@JsonProperty
	public boolean isClarification() {
		return Clarification.class.getName().equals(this.proposalType);
	}
	
	@JsonProperty
	public boolean isSupersession() {
		return Supersession.class.getName().equals(this.proposalType);
	}

	@JsonProperty
	public boolean isRetirement() {
		return Retirement.class.getName().equals(this.proposalType);
	}

	@JsonProperty
	public boolean isGroup() {
		return ProposalGroup.class.getName().equals(this.proposalType);
	}
	
	@JsonProperty
	public boolean isPendingChangeRequest() {
		return this.pendingChangeRequest != null;
	}
	
	@JsonProperty("DT_RowId")
	public String rowId() {
		return "proposalrow-" + this.getUuid().toString();
	}
}
