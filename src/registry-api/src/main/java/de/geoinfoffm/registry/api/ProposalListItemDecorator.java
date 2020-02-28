package de.geoinfoffm.registry.api;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalChangeRequest;

public class ProposalListItemDecorator implements ProposalListItem {

	@JsonIgnore
	private ProposalListItem decoratee;

	public ProposalListItemDecorator(ProposalListItem decoratee) {
		this.decoratee = decoratee;
	}

	@Override
	public String rowId() {
		return decoratee.rowId();
	}

	@JsonIgnore
	@Override
	public Proposal getProposal() {
		return decoratee.getProposal();
	}

	@Override
	public String getTitle() {
		return decoratee.getTitle();
	}

	@Override
	public UUID getProposalUuid() {
		return decoratee.getProposalUuid();
	}

	@Override
	public String getDateSubmitted() {
		return decoratee.getDateSubmitted();
	}

	@Override
	public String getItemClassName() {
		return decoratee.getItemClassName();
	}

	@Override
	public String getSponsorName() {
		return decoratee.getSponsorName();
	}

	@Override
	public UUID getSponsorUuid() {
		return decoratee.getSponsorUuid();
	}

	@Override
	public String getProposalStatus() {
		return decoratee.getProposalStatus();
	}

	@Override
	public String getTechnicalProposalStatus() {
		return decoratee.getTechnicalProposalStatus();
	}

	@Override
	public void overrideProposalStatus(String newStatus) {
		decoratee.overrideProposalStatus(newStatus);
	}

	@Override
	public String getProposalType() {
		return decoratee.getProposalType();
	}

	@Override
	public ProposalChangeRequest getPendingChangeRequest() {
		return decoratee.getPendingChangeRequest();
	}

	@Override
	public String getDispositionDate() {
		return decoratee.getDispositionDate();
	}

	@Override
	public String getTargetRegister() {
		return decoratee.getTargetRegister();
	}

	@Override
	public String getDisposition() {
		return decoratee.getDisposition();
	}

	@Override
	public Map<String, Object> getAdditionalData() {
		return decoratee.getAdditionalData();
	}

	@Override
	public boolean isSubmitter() {
		return decoratee.isSubmitter();
	}

	@Override
	public void setSubmitter(boolean submitter) {
		decoratee.setSubmitter(submitter);
	}

	@Override
	public boolean isAppealed() {
		return decoratee.isAppealed();
	}

	@Override
	public boolean isNotSubmitted() {
		return decoratee.isNotSubmitted();
	}

	@Override
	public boolean isUnderReview() {
		return decoratee.isUnderReview();
	}

	@Override
	public boolean isPending() {
		return decoratee.isPending();
	}

	@Override
	public boolean isAppealable() {
		return decoratee.isAppealable();
	}

	@Override
	public boolean isWithdrawable() {
		return decoratee.isWithdrawable();
	}

	@Override
	public boolean isFinished() {
		return decoratee.isFinished();
	}

	@Override
	public boolean isAddition() {
		return decoratee.isAddition();
	}

	@Override
	public boolean isClarification() {
		return decoratee.isClarification();
	}

	@Override
	public boolean isSupersession() {
		return decoratee.isSupersession();
	}

	@Override
	public boolean isRetirement() {
		return decoratee.isRetirement();
	}

	@Override
	public boolean isGroup() {
		return decoratee.isGroup();
	}

	@Override
	public boolean isPendingChangeRequest() {
		return decoratee.isPendingChangeRequest();
	}

}
