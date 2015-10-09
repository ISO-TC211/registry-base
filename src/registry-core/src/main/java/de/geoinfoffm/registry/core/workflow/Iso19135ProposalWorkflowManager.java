package de.geoinfoffm.registry.core.workflow;

import static de.geoinfoffm.registry.core.workflow.ProposalWorkflowManager.*;

import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Disposition;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;

@Transactional
public class Iso19135ProposalWorkflowManager implements ProposalWorkflowManager 
{		
	public Iso19135ProposalWorkflowManager() {
	}
	
	@Override
	public void initialize(Proposal proposal) {
		if (proposal.getStatus() == null) {
			proposal.setStatus(STATUS_NOT_SUBMITTED);
		}
	}

	@Override
	public void submit(Proposal proposal, Date submissionDate) throws IllegalOperationException {
		proposal.setDateSubmitted(submissionDate);
		proposal.setStatus(STATUS_UNDER_REVIEW);
	}
	
	@Override
	public void review(Proposal proposal, Date reviewDate) throws IllegalOperationException {
		if (this.isReviewed(proposal)) {
			throw new IllegalOperationException("Proposal is already reviewed");
		}
		
		proposal.setStatus(STATUS_IN_APPROVAL_PROCESS);
		for (RE_ProposalManagementInformation pmi : proposal.getProposalManagementInformations()) {
			pmi.review(reviewDate);
		}
	}

	@Override
	public void accept(Proposal proposal) throws IllegalOperationException {
		proposal.accept();
		this.conclude(proposal);
		for (RE_ProposalManagementInformation pmi : proposal.getProposalManagementInformations()) {
			pmi.makeDisposition(RE_Disposition.ACCEPTED);
		}
	}

	@Override
	public void accept(Proposal proposal, String controlBodyDecisionEvent) throws IllegalOperationException {
		this.accept(proposal);
		for (RE_ProposalManagementInformation pmi : proposal.getProposalManagementInformations()) {
			pmi.setControlBodyDecisionEvent(controlBodyDecisionEvent);
		}
	}

	@Override
	public void reject(Proposal proposal) throws IllegalOperationException {
		proposal.setStatus(STATUS_APPEALABLE);
		for (RE_ProposalManagementInformation pmi : proposal.getProposalManagementInformations()) {
			pmi.makeDisposition(RE_Disposition.NOT_ACCEPTED);
		}
	}

	@Override
	public void reject(Proposal proposal, String controlBodyDecisionEvent) throws IllegalOperationException {
		this.reject(proposal);
		for (RE_ProposalManagementInformation pmi : proposal.getProposalManagementInformations()) {
			pmi.setControlBodyDecisionEvent(controlBodyDecisionEvent);
		}
	}

	@Override
	public void withdraw(Proposal proposal) throws IllegalOperationException {
		for (RE_ProposalManagementInformation pmi : proposal.getProposalManagementInformations()) {
			pmi = (RE_ProposalManagementInformation)Entity.unproxify(pmi);
			pmi.makeDisposition(RE_Disposition.WITHDRAWN);
		}
		this.conclude(proposal);
	}

	@Override
	public void conclude(Proposal proposal) throws IllegalOperationException {
		proposal.setConcluded(true);
		proposal.setStatus(STATUS_FINISHED);
	}

	@Override
	public void delete(Proposal proposal) throws IllegalOperationException {
		proposal.delete();
	}

	@Override
	public Appeal appeal(Proposal proposal, String justification, String impact, String situation) throws IllegalOperationException {
		proposal.setStatus(STATUS_APPEALED);
		return new Appeal(proposal, justification, situation, impact);
	}

	@Override
	public boolean isSubmitted(String status) {
		return !STATUS_NOT_SUBMITTED.equals(status);
	}
	
	@Override
	public boolean isSubmitted(Proposal proposal) {
		return isSubmitted(proposal.getStatus());
	}

	@Override
	public boolean isUnderReview(String status) {
		return STATUS_UNDER_REVIEW.equals(status);
	}
	
	@Override
	public boolean isUnderReview(Proposal proposal) {
		return isUnderReview(proposal.getStatus());
	}

	@Override
	public boolean isReviewed(String status) {
		return !STATUS_WITHDRAWN.equals(status) &&
				(STATUS_IN_APPROVAL_PROCESS.equals(status) 
				|| STATUS_APPEALABLE.equals(status)
				|| STATUS_APPEALED.equals(status)
				|| STATUS_FINISHED.equals(status));
	}
	
	@Override
	public boolean isReviewed(Proposal proposal) { 
		return isReviewed(proposal.getStatus());
	}

	@Override
	public boolean isPending(String status) {
		return !STATUS_WITHDRAWN.equals(status) &&
				(STATUS_IN_APPROVAL_PROCESS.equals(status) 
				|| STATUS_APPEALABLE.equals(status));
	}
	
	@Override
	public boolean isPending (Proposal proposal) {
		return isPending(proposal.getStatus());
	}

	@Override
	public boolean isTentative(String status) {
		return STATUS_APPEALED.equals(status);
	}
	
	@Override
	public boolean isTentative(Proposal proposal) {
		return isTentative(proposal.getStatus());
	}

	@Override
	public boolean isAppealable(String status) {
		return STATUS_APPEALABLE.equals(status);
	}
	
	@Override
	public boolean isAppealable(Proposal proposal) {
		return isAppealable(proposal.getStatus());
	}

	@Override
	public boolean isAppealed(String status) {
		return STATUS_APPEALED.equals(status);
	}
	
	@Override
	public boolean isAppealed(Proposal proposal) {
		return isAppealed(proposal.getStatus());
	}

	@Override
	public boolean isFinal(String status) {
		return STATUS_FINISHED.equals(status);
	}
	
	@Override
	public boolean isFinal(Proposal proposal) {
		return isFinal(proposal.getStatus());
	}

	@Override
	public boolean isWithdrawn(Proposal proposal) {
		return this.isWithdrawn(proposal.getStatus());
	}

	@Override
	public boolean isWithdrawn(String status) {
		return STATUS_WITHDRAWN.equals(status);
	}

	@Override
	public boolean isWithdrawable(Proposal proposal) {
		return isPending(proposal) || isUnderReview(proposal);
	}

	@Override
	public boolean isWithdrawable(String status) {
		return isPending(status) || isUnderReview(status);
	}

	@Override
	public boolean isEditable(Proposal proposal) {
		return !isSubmitted(proposal) || (!isFinal(proposal) && isUnderReview(proposal)); 
	}

	@Override
	public boolean isEditable(String status) {
		return !isFinal(status) && isUnderReview(status);
	}

	@Override
	public void acceptAppeal(Appeal appeal, Date dispositionDate) {
		this.accept(appeal.getAppealedProposal());
		appeal.accept(dispositionDate);
	}

	@Override
	public void rejectAppeal(Appeal appeal, Date dispositionDate) {
		this.conclude(appeal.getAppealedProposal());
		appeal.reject(dispositionDate);
	}

	@Override
	public boolean isDiscussable(Proposal proposal) {
		return STATUS_NOT_SUBMITTED.equals(proposal.getStatus()) || STATUS_IN_APPROVAL_PROCESS.equals(proposal.getStatus());
	}
}
