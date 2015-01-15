/**
 * 
 */
package de.geoinfoffm.registry.core.model;

import static de.geoinfoffm.registry.core.model.Proposal.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Disposition;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

/**
 * @author Florian.Esser
 *
 */
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.JOINED)
@Audited @javax.persistence.Entity
public abstract class SimpleProposal extends Proposal
{
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@NotNull
	private RE_ProposalManagementInformation proposalManagementInformation;

	protected SimpleProposal() {
		
	}

	public SimpleProposal(RE_ProposalManagementInformation proposalManagementInformation) {
		super();
		
		if (proposalManagementInformation == null) {
			throw new NullPointerException("proposalManagementInformation null");
		}
		
		this.setProposalManagementInformation(proposalManagementInformation);
		this.setSponsor(proposalManagementInformation.getSponsor());
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
		return Collections.unmodifiableList(Arrays.asList(this.getProposalManagementInformation()));
	}

	@Override
	public void review(Date reviewDate) throws IllegalOperationException {
		if (this.isReviewed()) {
			throw new IllegalOperationException("Proposal is already reviewed");
		}
		
		this.setStatus(STATUS_IN_APPROVAL_PROCESS);
		proposalManagementInformation.review(reviewDate);
	}
	

	@Override
	public void accept() throws IllegalOperationException {
		proposalManagementInformation.makeDisposition(RE_Disposition.ACCEPTED);
		this.setStatus(STATUS_FINISHED);
		this.setConcluded(true);
	}

	@Override
	public void accept(String controlBodyDecisionEvent) throws IllegalOperationException {
		proposalManagementInformation.setControlBodyDecisionEvent(controlBodyDecisionEvent);
		this.setStatus(STATUS_FINISHED);
		this.accept();
	}

	@Override
	public void reject() throws IllegalOperationException {
		this.setStatus(STATUS_APPEALABLE);
		proposalManagementInformation.makeDisposition(RE_Disposition.NOT_ACCEPTED);		
	}

	@Override
	public void reject(String controlBodyDecisionEvent) throws IllegalOperationException {
		this.setStatus(STATUS_APPEALABLE);
		proposalManagementInformation.setControlBodyDecisionEvent(controlBodyDecisionEvent);
		this.reject();
	}
	
	@Override
	public void withdraw() throws IllegalOperationException {
		this.setStatus(STATUS_WITHDRAWN);
		proposalManagementInformation.makeDisposition(RE_Disposition.WITHDRAWN);
		this.setConcluded(true);
	}
	
	@Override
	public void delete() throws IllegalOperationException {
		this.proposalManagementInformation = null;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#conclude()
	 */
	@Override
	public void conclude() throws IllegalOperationException {
		proposalManagementInformation.finalizeDisposition();
		this.setConcluded(true);
		this.setStatus(STATUS_FINISHED);
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#appeal(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Appeal appeal(String justification, String impact, String situation) throws IllegalOperationException {
		this.setStatus(STATUS_APPEALED);		
		return new Appeal(this, justification, situation, impact);
	}

	public RE_RegisterItem getItem() {
		return proposalManagementInformation.getItem();
	}
	
	public RE_Register getRegister() {
		return proposalManagementInformation.getItem().getRegister();
	}
	
	@Override
	public RE_SubmittingOrganization getSponsor() {
		return sponsor;
	}

	public void setSponsor(RE_SubmittingOrganization sponsor) {
		proposalManagementInformation.setSponsor(sponsor);
		this.sponsor = sponsor;
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

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#isPending()
	 */
	@Override
	public boolean isPending() {
		return proposalManagementInformation.isPending();
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#isReviewed()
	 */
	@Override
	public boolean isReviewed() {
		return this.proposalManagementInformation.isReviewed();
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#isUnderReview()
	 */
	@Override
	public boolean isUnderReview() {
		return !this.proposalManagementInformation.isReviewed();
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#isTentative()
	 */
	@Override
	public boolean isTentative() {
		return this.proposalManagementInformation.isTentative();
	}

	@Override
	public boolean isFinal() {
		return proposalManagementInformation.isFinal();
	}

	@Override
	public List<RE_Register> getAffectedRegisters() {
		return Arrays.asList(this.getRegister());
	}
	
	
}
