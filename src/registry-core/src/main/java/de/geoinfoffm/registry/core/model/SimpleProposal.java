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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Disposition;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

/**
 * The class SimpleProposal.
 *
 * @author Florian Esser
 */
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.JOINED)
@Audited @javax.persistence.Entity
public abstract class SimpleProposal extends Proposal
{
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@NotNull
//	@IndexedEmbedded(includePaths = { "dateDisposed", "item.register.name" })
	private RE_ProposalManagementInformation proposalManagementInformation;

//	@Field(store = Store.YES)
	@Column(columnDefinition = "text")
	private String itemClassName;
	
	@ManyToOne
	private RE_Register targetRegister;
	
	protected SimpleProposal() {
		super();
	}
	
	protected SimpleProposal(String title) {
		super(title);
	}

	public SimpleProposal(RE_ProposalManagementInformation proposalManagementInformation) {
		super((proposalManagementInformation == null) ? "" : proposalManagementInformation.getItem().getName());
		
		if (proposalManagementInformation == null) {
			throw new NullPointerException("proposalManagementInformation null");
		}
		
		this.setProposalManagementInformation(proposalManagementInformation);
		this.setSponsor(proposalManagementInformation.getSponsor());
		if (proposalManagementInformation.getItem() != null && proposalManagementInformation.getItem().getItemClass() != null) {
			this.setItemClassName(proposalManagementInformation.getItem().getItemClass().getName());
			this.setTargetRegister(proposalManagementInformation.getItem().getRegister());
		}
	}
	
	public RE_DecisionStatus getDecisionStatus() {
		return proposalManagementInformation.getStatus();
	}
	
	public RE_Disposition getDisposition() {
		return proposalManagementInformation.getDisposition();
	}
	
	public boolean isContainedIn(RE_Register register) {
		return proposalManagementInformation.getItem().isContainedIn(register);
	}

	/**
	 * @return the proposalManagementInformations
	 */
	public RE_ProposalManagementInformation getProposalManagementInformation() {
		return proposalManagementInformation;
	}

	/**
	 * @param proposalManagementInformations the proposalManagementInformations to set
	 */
	protected void setProposalManagementInformation(RE_ProposalManagementInformation proposalManagementInformation) {
		this.proposalManagementInformation = proposalManagementInformation;
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#getProposalManagementInformations()
	 */
	@Override
	public List<RE_ProposalManagementInformation> getProposalManagementInformations() {
		List<RE_ProposalManagementInformation> result = new ArrayList<RE_ProposalManagementInformation>();
		result.add(this.getProposalManagementInformation());
		for (Proposal dependentProposal : this.getDependentProposals()) {
			result.addAll(dependentProposal.getProposalManagementInformations());
		}
		return Collections.unmodifiableList(result);
//		return Collections.unmodifiableList(Arrays.asList(this.getProposalManagementInformation()));
	}

	public String getItemClassName() {
		return itemClassName;
	}

	public void setItemClassName(String itemClassName) {
		this.itemClassName = itemClassName;
	}

	public RE_Register getTargetRegister() {
		return targetRegister;
	}

	public void setTargetRegister(RE_Register targetRegister) {
		this.targetRegister = targetRegister;
	}

	@Override
	public void delete() throws IllegalOperationException {
		this.proposalManagementInformation = null;
	}
	
	public RE_RegisterItem getItem() {
		return proposalManagementInformation.getItem();
	}
	
	public RE_Register getRegister() {
		return this.targetRegister;
	}
	
	@Override
	public RE_SubmittingOrganization getSponsor() {
		return sponsor;
	}

	public void setSponsor(RE_SubmittingOrganization sponsor) {
		super.setSponsor(sponsor);
		
		proposalManagementInformation.setSponsor(sponsor);
	}

	public String getJustification() {
		return proposalManagementInformation.getJustification();
	}
	
	public void setJustification(String justification) {
		proposalManagementInformation.setJustification(justification);
	}
	
	public Date getDateProposed() {
		return proposalManagementInformation.getDateProposed();
	}

	public Date getDateDisposed() {
		return proposalManagementInformation.getDateDisposed();
	}
	
	public String getRegisterManagerNotes() {
		return proposalManagementInformation.getRegisterManagerNotes();
	}

	public void setRegisterManagerNotes(String notes) {
		proposalManagementInformation.setRegisterManagerNotes(notes);
	}

	public String getControlBodyNotes() {
		return proposalManagementInformation.getControlBodyNotes();
	}

	public void setControlBodyNotes(String notes) {
		proposalManagementInformation.setControlBodyNotes(notes);
	}

	public String getControlBodyDecisionEvent() {
		return proposalManagementInformation.getControlBodyDecisionEvent();
	}
	
	public void setControlBodyDecisionEvent(String event) {
		proposalManagementInformation.setControlBodyDecisionEvent(event);
	}

	@Override
	public Set<RE_Register> getAffectedRegisters() {
		return Collections.singleton(this.getRegister());
	}	
	
	
}
