package de.geoinfoffm.registry.api;

import java.util.Map;
import java.util.UUID;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalChangeRequest;

public interface ProposalListItem extends ListItem<Proposal>
{
	Proposal getProposal();
	
	String getTitle();

	UUID getProposalUuid();

	String getDateSubmitted();

	String getItemClassName();

	String getSponsorName();

	UUID getSponsorUuid();

	String getProposalStatus();

	String getTechnicalProposalStatus();

	String getProposalType();

	ProposalChangeRequest getPendingChangeRequest();

	String getDispositionDate();

	String getTargetRegister();

	String getDisposition();

	Map<String, Object> getAdditionalData();

	boolean isAppealed();

	boolean isNotSubmitted();

	boolean isUnderReview();

	boolean isPending();

	boolean isAppealable();

	boolean isWithdrawable();

	boolean isFinished();

	boolean isAddition();

	boolean isClarification();

	boolean isSupersession();

	boolean isRetirement();

	boolean isGroup();

	boolean isPendingChangeRequest();
}