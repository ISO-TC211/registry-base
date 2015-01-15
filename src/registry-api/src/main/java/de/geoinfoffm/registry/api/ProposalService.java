package de.geoinfoffm.registry.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Clarification;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.Retirement;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.soap.AbstractProposal_Type;

public interface ProposalService extends ApplicationService<Proposal>
{
	<P extends Proposal> P saveProposal(P proposal);
	<P extends Proposal> P submitProposal(P proposal) throws IllegalOperationException;
	
	Proposal propose(RegisterItemProposalDTO proposal) throws InvalidProposalException, ItemNotFoundException, IllegalOperationException;
	Addition createAdditionProposal(RegisterItemProposalDTO proposal) throws InvalidProposalException;
	Retirement proposeRetirement(RE_RegisterItem item, String justification, String registerManagerNotes, String controlBodyNotes, RE_SubmittingOrganization sponsor) throws IllegalOperationException;
	Clarification proposeClarification(RE_RegisterItem item, Map<String, String[]> proposedChanges, String justification, String registerManagerNotes, String controlBodyNotes, RE_SubmittingOrganization sponsor) throws IllegalOperationException;
	Supersession proposeSupersession(Set<RE_RegisterItem> supersededItems, Set<RegisterItemProposalDTO> successors, String justification, String registerManagerNotes, String controlBodyNotes, RE_SubmittingOrganization sponsor) throws IllegalOperationException, InvalidProposalException;

	Proposal updateProposal(RegisterItemProposalDTO proposal) throws InvalidProposalException;
	Proposal updateProposal(UUID proposalUuid, AbstractProposal_Type proposal) throws InvalidProposalException;
	Supersession updateSupersession(Supersession supersession, Set<RE_RegisterItem> supersededItems, Set<RE_RegisterItem> existingSuccessors, Set<RegisterItemProposalDTO> newSuccessors, String justification, String registerManagerNotes, String controlBodyNotes) throws InvalidProposalException;
	Proposal withdrawProposal(Proposal proposal) throws InvalidProposalException, IllegalOperationException;

	Proposal reviewProposal(Proposal proposal) throws InvalidProposalException, IllegalOperationException;

	Proposal acceptProposal(Proposal proposal, String controlBodyDecisionEvent) throws InvalidProposalException, IllegalOperationException;

	Proposal rejectProposal(Proposal proposal, String controlBodyDecisionEvent) throws InvalidProposalException, IllegalOperationException, UnauthorizedException;

	Proposal concludeProposal(Proposal proposal) throws InvalidProposalException, IllegalOperationException;
	Appeal appealProposal(Proposal proposal, String justification, String situation, String impact) throws InvalidProposalException, IllegalOperationException;

	Appeal acceptAppeal(Appeal appeal) throws IllegalOperationException;
	Appeal rejectAppeal(Appeal appeal) throws IllegalOperationException;

	Proposal findProposal(UUID uuid) throws EntityNotFoundException;
	Collection<Proposal> findProposals(RE_Register register, RE_DecisionStatus status);
//	Collection<Proposal> findProposals(RE_Register register, Collection<RE_DecisionStatus> status);
	
	Collection<Supersession> findSupersessions(Collection<RE_DecisionStatus> status);

	Appeal findAppeal(UUID uuid);
	Appeal findAppeal(Proposal proposal);
}
