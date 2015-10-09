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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import de.geoinfoffm.registry.api.soap.AbstractProposal_Type;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Actor;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Authorization;
import de.geoinfoffm.registry.core.model.Clarification;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalChangeRequest;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.Retirement;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

public interface ProposalService extends ApplicationService<Proposal>
{
	<P extends Proposal> P saveProposal(P proposal);
	<P extends Proposal> P submitProposal(P proposal) throws IllegalOperationException;
	
	Proposal propose(RegisterItemProposalDTO proposal) throws InvalidProposalException, ItemNotFoundException, IllegalOperationException;
	Addition createAdditionProposal(RegisterItemProposalDTO proposal) throws InvalidProposalException;
	Retirement createRetirement(RE_RegisterItem item, String justification, String registerManagerNotes, String controlBodyNotes, RE_SubmittingOrganization sponsor) throws IllegalOperationException;
	Clarification createClarification(RE_RegisterItem item, Map<String, List<String>> proposedChanges, String justification, String registerManagerNotes, String controlBodyNotes, RE_SubmittingOrganization sponsor) throws IllegalOperationException;
	Supersession createSupersession(Set<RE_RegisterItem> supersededItems, Set<RegisterItemProposalDTO> successors, String justification, String registerManagerNotes, String controlBodyNotes, RE_SubmittingOrganization sponsor) throws IllegalOperationException, InvalidProposalException;
	ProposalGroup createProposalGroup(List<Proposal> containedProposals, RE_SubmittingOrganization sponsor) throws InvalidProposalException;
	ProposalGroup createProposalGroup(String name, List<Proposal> containedProposals, RE_SubmittingOrganization sponsor) throws InvalidProposalException;

	Proposal updateProposal(RegisterItemProposalDTO proposal) throws InvalidProposalException, UnauthorizedException;
	Proposal updateProposal(UUID proposalUuid, AbstractProposal_Type proposal) throws InvalidProposalException;
	Supersession updateSupersession(Supersession supersession, Set<RE_RegisterItem> supersededItems, Set<RE_RegisterItem> existingSuccessors, Set<RegisterItemProposalDTO> newSuccessors, String justification, String registerManagerNotes, String controlBodyNotes) throws InvalidProposalException;
	
	Proposal withdrawProposal(Proposal proposal) throws InvalidProposalException, IllegalOperationException;
	void deleteProposal(Proposal proposal) throws IllegalOperationException, UnauthorizedException;

	Proposal reviewProposal(Proposal proposal) throws InvalidProposalException, IllegalOperationException, UnauthorizedException;

	Proposal acceptProposal(Proposal proposal, String controlBodyDecisionEvent) throws InvalidProposalException, IllegalOperationException, UnauthorizedException;

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
	
	void approveProposalChange(Actor actor, ProposalChangeRequest changeRequest);
	
	List<Authorization> findAuthorizedControlBody(Proposal proposal);
}
