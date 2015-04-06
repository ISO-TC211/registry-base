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

import static de.geoinfoffm.registry.core.workflow.ProposalWorkflowManager.*;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.slf4j.Logger;

import de.bespire.LoggerFactory;
import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Disposition;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.workflow.ProposalWorkflowManager;

/**
 * @author Florian Esser
 *
 */
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "_proposalType", discriminatorType = DiscriminatorType.STRING)
@Audited @javax.persistence.Entity
public abstract class Proposal extends Entity
{
	private static final Logger logger = LoggerFactory.make();
	
	@Column(columnDefinition = "text")
	private String title;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProposalGroup group;
	
	private String status;
	
	@ManyToOne
	protected RE_SubmittingOrganization sponsor;	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateSubmitted;
	
	private Boolean isConcluded = false;
	
	public abstract List<RE_ProposalManagementInformation> getProposalManagementInformations();
	public abstract List<RE_Register> getAffectedRegisters();
	
	public abstract RE_DecisionStatus getDecisionStatus();
	public abstract RE_Disposition getDisposition();
	
	public abstract boolean isContainedIn(RE_Register register);
	
	public void accept() throws IllegalOperationException {
		
	}
	
	public abstract void delete() throws IllegalOperationException;

	protected Proposal() {
		this.title = "";
	}

	protected Proposal(String title) {
		this.setTitle(title);
	}

	/**
	 * @return the group
	 */
	public ProposalGroup getGroup() {
		return group;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroup(ProposalGroup group) {
		this.group = group;
	}

	public boolean hasGroup() {
		return this.getGroup() != null;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		logger.trace(String.format(">>> Changing status of proposal '%s' from '%s' to '%s'", this.getUuid(), this.status, status));
		this.status = status;
	}
	public RE_SubmittingOrganization getSponsor() {
		return sponsor;
	}

	public abstract void setSponsor(RE_SubmittingOrganization sponsor);

	public boolean isSubmitted() {
		return dateSubmitted != null;
	}
	
	public Date getDateSubmitted() {
		return this.dateSubmitted;
	}
	
	public void setDateSubmitted(Date dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	public Boolean isConcluded() {
		return isConcluded;
	}
	
	public void setConcluded(Boolean isConcluded) {
		this.isConcluded = isConcluded;
	}
	
	public boolean isEditable() {
		return true;
	}
	
	public interface Factory {
		Proposal createProposal(RE_ProposalManagementInformation proposalManagementInformation);
	}
}
