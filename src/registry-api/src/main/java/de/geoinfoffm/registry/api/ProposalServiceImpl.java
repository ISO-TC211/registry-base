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

import static de.geoinfoffm.registry.core.security.RegistrySecurity.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.ProposalAcceptedEvent;
import de.geoinfoffm.registry.core.ProposalCreatedEvent;
import de.geoinfoffm.registry.core.ProposalRejectedEvent;
import de.geoinfoffm.registry.core.ProposalSavedEvent;
import de.geoinfoffm.registry.core.ProposalSubmittedEvent;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Actor;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Authorization;
import de.geoinfoffm.registry.core.model.AuthorizationRepository;
import de.geoinfoffm.registry.core.model.Clarification;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalChangeRequest;
import de.geoinfoffm.registry.core.model.ProposalChangeRequestRepository;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.ProposalRepoository;
import de.geoinfoffm.registry.core.model.ProposalType;
import de.geoinfoffm.registry.core.model.Retirement;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.SupersessionPart;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.ProposalManagementInformationRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_ClarificationInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.model.iso19135.SubmittingOrganizationRepoository;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.AppealRepository;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.RegisterItemRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;
import de.geoinfoffm.registry.persistence.ResponsiblePartyRepository;
import de.geoinfoffm.registry.persistence.SupersessionRepository;
import de.geoinfoffm.registry.soap.AbstractProposal_Type;
import de.geoinfoffm.registry.soap.AbstractRegisterItemProposal_Type;
import de.geoinfoffm.registry.soap.Addition_Type;
import de.geoinfoffm.registry.soap.Clarification_Type;
import de.geoinfoffm.registry.soap.ProposedChange_PropertyType;
import de.geoinfoffm.registry.soap.Retirement_Type;
import de.geoinfoffm.registry.soap.Supersession_Type;

/**
 * The class ProposalServiceImpl.
 *
 * @author Florian Esser
 */
@Transactional
//@Service
public class ProposalServiceImpl extends AbstractApplicationService<Proposal, ProposalRepoository> implements ProposalService
{
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private MessageSource messages;
	
	@Autowired
	private RegisterRepository registerRepository;
	
	@Autowired
	private SubmittingOrganizationRepoository submittingOrgRepository;
	
	@Autowired
	private ResponsiblePartyRepository partyRepository;

	@Autowired
	private ItemClassRepository itemClassRepository;
	
	@Autowired
	private ProposalChangeRequestRepository pcrRepository;

	@Autowired
	private RegisterItemService itemService;
	
	@Autowired
	private ProposalRepoository proposalRepository;
	
	@Autowired
	private ProposalManagementInformationRepository pmiRepository;

	@Autowired
	private AppealRepository appealRepository;
	
	@Autowired
	private SupersessionRepository supersessionRepository;
	
	@Autowired
	private RegistrySecurity security;
	
	@Autowired
	private ItemFactoryRegistry itemFactoryRegistry;
	
	@Autowired
	private RegisterItemRepository itemRepository;

	@Autowired
	private ControlBodyDiscoveryStrategy cbStrategy;

	@Autowired
	private AuthorizationRepository authRepository;

	@Autowired
	public ProposalServiceImpl(ProposalRepoository repository) {
		super(repository);
	}

	@Override
	public <P extends Proposal> P saveProposal(P proposal) {
		pmiRepository.save(proposal.getProposalManagementInformations());
		proposal = repository().save(proposal);
		
		eventPublisher().publishEvent(new ProposalSavedEvent(proposal));
		
		return proposal;
	}

	@Override
	public <P extends Proposal> P submitProposal(P proposal) throws IllegalOperationException {
		if (proposal.isSubmitted()) {
			throw new IllegalOperationException("The proposal was already submitted");
		}
		
		if (proposal.hasGroup()) {
			return this.submitProposal((P)proposal.getGroup());
		}
		
		proposal.submit(Calendar.getInstance().getTime());
		this.saveProposal(proposal);
		
		eventPublisher().publishEvent(new ProposalSubmittedEvent(proposal));
				
		return proposal;
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#propose(de.geoinfoffm.registry.api.RegisterItemProposalDTO)
	 */
	@Override
	public Proposal propose(RegisterItemProposalDTO proposal) throws InvalidProposalException, ItemNotFoundException, IllegalOperationException {
		RE_SubmittingOrganization sponsor = submittingOrgRepository.findOne(proposal.getSponsorUuid());
		if (sponsor == null) {
			throw new InvalidProposalException(String.format("Non-existent sponsor: %s", proposal.getSponsorUuid()));
		}
		
		if (proposal.getProposalType().equals(ProposalType.ADDITION)) {
			return this.createAdditionProposal(proposal);
		}
		
		if (proposal.getProposalType().equals(ProposalType.CLARIFICATION)) {
			RE_RegisterItem item = itemService.findOne(proposal.getItemUuid());
			if (item == null) {
				throw new ItemNotFoundException(proposal.getItemUuid());
			}
			
			if (!item.isValid()) {
				throw new IllegalOperationException(String.format("Cannot clarify item with status %s", item.getStatus().name()));
			}

			Clarification clarification = this.createClarification(item, proposal.calculateProposedChanges(item), proposal.getJustification(), 
					proposal.getRegisterManagerNotes(), proposal.getControlBodyNotes(), sponsor);
			return this.submitProposal(clarification);
		}
		
		if (proposal.getProposalType().equals(ProposalType.RETIREMENT)) {
			RE_RegisterItem item = itemService.findOne(proposal.getItemUuid());
			if (item == null) {
				throw new ItemNotFoundException(proposal.getItemUuid());
			}

			if (!item.isValid()) {
				throw new IllegalOperationException(String.format("Cannot retire item with status %s", item.getStatus().name()));
			}
		
			Retirement retirement = this.createRetirement(item, proposal.getJustification(), proposal.getRegisterManagerNotes(), 
					proposal.getControlBodyNotes(), sponsor);
			return this.submitProposal(retirement);
		}

		if (proposal.getProposalType().equals(ProposalType.SUPERSESSION)) {
			RE_Register register = registerRepository.findOne(proposal.getTargetRegisterUuid());
			if (register == null) {
				throw new InvalidProposalException(String.format("Non-existent target register: %s", proposal.getTargetRegisterUuid()));
			}
			
			Set<RE_RegisterItem> supersededItems = new HashSet<RE_RegisterItem>();
			for (UUID supersededItemUuid : proposal.getSupersededItems()) {
				RE_RegisterItem item = itemService.findOne(proposal.getItemUuid());
				if (item == null) {
					throw new ItemNotFoundException(proposal.getItemUuid());
				}
				if (!item.isValid()) {
					throw new IllegalOperationException(String.format("Cannot superseded item %s with status %s", item.getUuid(), item.getStatus().name()));
				}
				supersededItems.add(item);
			}
			
			Supersession supersession = this.createSupersession(supersededItems, proposal.getNewSupersedingItems(), proposal.getJustification(), 
					proposal.getRegisterManagerNotes(), proposal.getControlBodyNotes(), sponsor);
			return this.submitProposal(supersession);
		}
		
		throw new InvalidProposalException(String.format("Unsupported proposal type: %s", proposal.getProposalType()));
	}
	
	protected RE_ItemClass findItemClass(RegisterItemProposalDTO proposal) {
		RE_ItemClass result = null;
		if (proposal.getItemClassUuid() != null) {
			result = itemClassRepository.findOne(proposal.getItemClassUuid());
		}
		else if (!org.springframework.util.StringUtils.isEmpty(proposal.getItemClassName())) {
			result = itemClassRepository.findByName(proposal.getItemClassName());
			if (result != null) {
				proposal.setItemClassUuid(result.getUuid());
			}
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#submitProposal(de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem)
	 */
	@Override
	public Addition createAdditionProposal(RegisterItemProposalDTO proposal) throws InvalidProposalException {
		RE_ItemClass itemClass;
		if (!StringUtils.isEmpty(proposal.getItemClassName())) {
			itemClass = itemClassRepository.findByName(proposal.getItemClassName());
		}
		else if (proposal.getItemClassUuid() != null) {
			itemClass = itemClassRepository.findOne(proposal.getItemClassUuid());			
		}
		else {
			throw new InvalidProposalException("Either name or UUID of item class must be provided");
		}
		
		RE_SubmittingOrganization sponsor = submittingOrgRepository.findOne(proposal.getSponsorUuid());
		
		BindingResult bindingResult = validateProposal(proposal);
		if (bindingResult.hasErrors()) {
			throw new InvalidProposalException(bindingResult);
		}

		// Create dependent items and proposal first, so we can satisfy dependencies in the main item
		List<Addition> groupProposals = new ArrayList<Addition>();
		for (RegisterItemProposalDTO dependentProposal : proposal.getDependentProposals()) {
			createDependentProposal(dependentProposal, proposal, groupProposals, sponsor);
		}		

		// TODO delegate to service if possible
		RegisterItemFactory<RE_RegisterItem, RegisterItemProposalDTO> registerItemFactory = 
				(RegisterItemFactory<RE_RegisterItem, RegisterItemProposalDTO>)itemFactoryRegistry.getFactory(itemClass.getName());
		RE_RegisterItem item = registerItemFactory.createRegisterItem(proposal);
		
		if (item == null) {
			throw new NullPointerException(String.format("Factory %s returned null item", registerItemFactory.getClass().getCanonicalName()));
		}
		
		Addition addition = Addition.createAddition(item, sponsor, 
				proposal.getJustification(), proposal.getRegisterManagerNotes(), proposal.getControlBodyNotes());

		addition = saveProposal(addition, item);
		
		if (!groupProposals.isEmpty()) {
//			HierarchicalProposal group = new HierarchicalProposal(addition, groupProposals);
//			group.setTitle(addition.getItem().getName());
//			group.setSponsor(addition.getSponsor());
//			group.setPrimaryProposal(addition);
//			group = this.saveProposal(group);
//			
//			groupProposals.add(addition);
//			for (Addition groupProposal : groupProposals) {
//				groupProposal.setGroup(group);
//				proposalRepository.save(groupProposal);
//			}
//			
//			eventPublisher().publishEvent(new ProposalCreatedEvent(group));
		}
		
		eventPublisher().publishEvent(new ProposalCreatedEvent(addition));

		return addition;
	}

	private void createDependentProposal(RegisterItemProposalDTO proposal, RegisterItemProposalDTO mainProposal,
			List<Addition> groupProposals, RE_SubmittingOrganization sponsor) throws InvalidProposalException {
		BindingResult bindingResult;
		bindingResult = validateProposal(proposal);
		if (bindingResult.hasErrors()) {
			throw new InvalidProposalException(bindingResult);
		}
		
		for (RegisterItemProposalDTO dependentProposal : proposal.getDependentProposals()) {
			createDependentProposal(dependentProposal, proposal, groupProposals, sponsor);
		}		
		
		// TODO Justification etc. müssen aus ProposalDTO des abhängigen Items kommen!

		proposal.setTargetRegisterUuid(mainProposal.getTargetRegisterUuid());
		proposal.setSponsorUuid(mainProposal.getSponsorUuid());
		proposal.setProposalType(ProposalType.ADDITION);
		
		RE_ItemClass dependentProposalItemClass = this.findItemClass(proposal);
		RegisterItemFactory<RE_RegisterItem, RegisterItemProposalDTO> itemFactory = 
				(RegisterItemFactory<RE_RegisterItem, RegisterItemProposalDTO>)itemFactoryRegistry.getFactory(dependentProposalItemClass.getName());
		RE_RegisterItem dependentItem = itemFactory.createRegisterItem(proposal);

		if (dependentItem == null) {
			throw new NullPointerException(String.format("Factory %s returned null item", itemFactory.getClass().getCanonicalName()));
		}
		
//			dependentItem.setItemIdentifier(subItemIdentifier);

		Addition subAddition = Addition.createAddition(dependentItem, sponsor, 
				proposal.getJustification(), proposal.getRegisterManagerNotes(), proposal.getControlBodyNotes());
		
		subAddition = saveProposal(subAddition, dependentItem);

		// Set the referenced item UUID in the proposal so that it may be referenced the main item
		proposal.setReferencedItemUuid(subAddition.getItem().getUuid());
		
		eventPublisher().publishEvent(new ProposalCreatedEvent(subAddition));
		
		groupProposals.add(subAddition);
	}

	private BindingResult validateProposal(RegisterItemProposalDTO proposal) {
		DataBinder binder = new DataBinder(proposal);
		binder.setValidator(validator);
		binder.validate();
		BindingResult bindingResult = binder.getBindingResult();

		return bindingResult;
	}

	private Addition saveProposal(Addition addition, RE_RegisterItem item) {
		BigInteger maxIdentifier = itemService.findMaxItemIdentifier();
		if (maxIdentifier == null) {
			maxIdentifier = BigInteger.ZERO;
		}
		item.setItemIdentifier(maxIdentifier.add(BigInteger.ONE));

		item = item.submitAsProposal();

		item = itemService.saveRegisterItem(item);
		
		addition = this.saveProposal(addition);
		return addition;
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#updateProposal(de.geoinfoffm.registry.api.RegisterItemProposalDTO)
	 */
	@Override
	public Proposal updateProposal(RegisterItemProposalDTO proposalDto) throws InvalidProposalException, UnauthorizedException {
		Proposal proposal = proposalRepository.findOne(proposalDto.getProposalUuid());
		if (proposal == null) {
			throw new InvalidProposalException(String.format("Referenced proposal with id '%s' does not exist.", proposalDto.getProposalUuid()));
		}

		BindingResult bindingResult = validateProposal(proposalDto);
		if (bindingResult.hasErrors()) {
			throw new InvalidProposalException(bindingResult);
		}

		// Is this the primary proposal of a HierarchicalProposal?
//		if (proposal.hasGroup() && proposal.getGroup() instanceof HierarchicalProposal && ((HierarchicalProposal)proposal.getGroup()).getPrimaryProposal().equals(proposal)) {
//			HierarchicalProposal hp = (HierarchicalProposal)proposal.getGroup();
//			Proposal primaryProposal = hp.getPrimaryProposal();
//
//			List<Proposal> noLongerDependent = new ArrayList<Proposal>();
//			noLongerDependent.addAll(hp.getDependentProposals());
//			// Remove those proposals from 'no longer dependent' list that are still dependent
//			for (RegisterItemProposalDTO subDto : proposalDto.getDependentProposals()) {
//				if (subDto.getProposalUuid() != null) {
//					Proposal subProposal = proposalRepository.findOne(subDto.getProposalUuid());
//					noLongerDependent.remove(subProposal);
//				}
//			}
//			
//			List<RegisterItemProposalDTO> newlyDependent = new ArrayList<RegisterItemProposalDTO>();
//			for (RegisterItemProposalDTO subDto : proposalDto.getDependentProposals()) {
//				if (subDto.getProposalUuid() == null) {
//					newlyDependent.add(subDto);
//				}
//			}
//			
//			for (Proposal perishable : noLongerDependent) {
//				perishable.setGroup(null);
//				hp.removeProposal(perishable);
//				hp.getDependentProposals().remove(perishable);
//				this.saveProposal(hp);
//			}
//			while (!noLongerDependent.isEmpty()) {
//				Proposal p = noLongerDependent.get(0);
//				noLongerDependent.remove(0);
//				this.deleteProposal(p);
//			}
//			
//			for (RegisterItemProposalDTO newSub : newlyDependent) {
//				Proposal subProposal;
//				if (ProposalType.ADDITION.equals(newSub.getProposalType())) {
//					bindingResult = validateProposal(newSub);
//					if (bindingResult.hasErrors()) {
//						throw new InvalidProposalException(bindingResult);
//					}
//
//					newSub.setTargetRegisterUuid(proposalDto.getTargetRegisterUuid());
//					newSub.setSponsorUuid(proposalDto.getSponsorUuid());
//					
//					RE_ItemClass dependentProposalItemClass = this.findItemClass(newSub);
//					RegisterItemFactory<RE_RegisterItem, RegisterItemProposalDTO> itemFactory = 
//							(RegisterItemFactory<RE_RegisterItem, RegisterItemProposalDTO>)itemFactoryRegistry.getFactory(dependentProposalItemClass.getName());
//					RE_RegisterItem dependentItem = itemFactory.createRegisterItem(newSub);
//
//					if (dependentItem == null) {
//						throw new NullPointerException(String.format("Factory %s returned null item", itemFactory.getClass().getCanonicalName()));
//					}
//					
//					subProposal = this.createAdditionProposal(newSub);
//					subProposal.setGroup(hp);
//					
//					BigInteger itemIdentifier = itemService.findMaxItemIdentifier();
//					if (itemIdentifier == null) {
//						itemIdentifier = BigInteger.ZERO;
//					}
//					dependentItem.setItemIdentifier(itemIdentifier);
//					
//					subProposal = this.saveProposal((Addition)subProposal, dependentItem);
//					// Set the referenced item UUID in the proposal so that it may be referenced the main item
//					newSub.setReferencedItemUuid(dependentItem.getUuid());
//				}
//				else if (ProposalType.SUPERSESSION.equals(newSub.getProposalType())) {
//					throw new RuntimeException("Not yet implemented for SUPERSESSIONs");
//				}
//				else {
//					subProposal = this.propose(newSub);
//				}
//				
//				eventPublisher().publishEvent(new ProposalCreatedEvent(subProposal));
//				hp.addProposal(subProposal);
//			}
//			
//			if (primaryProposal instanceof Addition) {
//				RE_RegisterItem item = ((Addition)primaryProposal).getItem();
//				hp.setTitle(item.getName());
//				proposalDto.setAdditionalValues(item, this.entityManager);
//			}
//			
//			hp = this.saveProposal(hp);
//			
//			return hp;
//		}

		if (proposalDto.getProposalType().equals(ProposalType.SUPERSESSION)) {
			throw new RuntimeException("Use updateSupersession(...)");
		}
		else {
			RE_RegisterItem item = itemService.findOne(proposalDto.getItemUuid());
			if (item == null) {
				throw new InvalidProposalException(String.format("Referenced item with id '%s' does not exist.", proposalDto.getItemUuid()));
			}

			if (proposal instanceof Addition) {
				item.setName(proposalDto.getName());
				item.setDefinition(proposalDto.getDefinition());
				item.setDescription(proposalDto.getDescription());
				
				proposalDto.setAdditionalValues(item, this.entityManager);
			}
			else if (proposal instanceof Clarification) {
				String proposedChangesJson = RE_ClarificationInformation.toJson(proposalDto.calculateProposedChanges(item));
				((Clarification)proposal).setProposedChange(proposedChangesJson);
			}

			itemService.saveRegisterItem(item);
		}
		
		if (proposal instanceof SimpleProposal) {
			((SimpleProposal)proposal).setJustification(proposalDto.getJustification());
			((SimpleProposal)proposal).setRegisterManagerNotes(proposalDto.getRegisterManagerNotes());
			((SimpleProposal)proposal).setControlBodyNotes(proposalDto.getControlBodyNotes());
		}
		
		proposal = this.saveProposal(proposal);
		
		return proposal;
	}
	
	@Override
	public Proposal updateProposal(UUID proposalUuid, AbstractProposal_Type proposalDto) throws InvalidProposalException {
		if (proposalDto instanceof Supersession_Type) {
			throw new RuntimeException("Use updateSupersession(...)");			
		}
		
		String itemUuid;
		if (proposalDto instanceof Addition_Type) {
			itemUuid = ((Addition_Type) proposalDto).getCreatedItem().getRE_RegisterItem().getValue().getUuid();
		}
		else if (proposalDto instanceof Clarification_Type) {
			itemUuid = ((Clarification_Type) proposalDto).getClarifiedItemUuid();
		}
		else if (proposalDto instanceof Retirement_Type) {
			itemUuid = ((Retirement_Type) proposalDto).getRetiredItemUuid();
		}
		else {
			throw new RuntimeException(String.format("Proposals of type %s are not supported", proposalDto.getClass().getCanonicalName()));
		}
		
		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new InvalidProposalException(String.format("Proposal with id '%s' does not exist.", proposalUuid));
		}

		RE_RegisterItem item = itemService.findOne(UUID.fromString(itemUuid));
		if (item == null) {
			throw new InvalidProposalException(String.format("Referenced item with id '%s' does not exist.", itemUuid));
		}

		if (proposal instanceof Addition) {
			AbstractRegisterItemProposal_Type changedItem = ((Addition_Type)proposalDto).getItemDetails().getAbstractRegisterItemProposal().getValue(); 
			item.setName(changedItem.getName());
			item.setDefinition(changedItem.getDefinition());
			item.setDescription(changedItem.getDescription());
		}
		else if (proposal instanceof Clarification) {
			Map<String, String[]> proposedChanges = new HashMap<String, String[]>();
			for (ProposedChange_PropertyType change : ((Clarification_Type)proposalDto).getProposedChange()) {
				if (!change.isSetProposedChange()) continue;
				
				proposedChanges.put(change.getProposedChange().getProperty(), new String[] { change.getProposedChange().getNewValue().getCharacterString().toString() });
			}
			String proposedChangesJson = RE_ClarificationInformation.toJson(proposedChanges);
			((Clarification)proposal).setProposedChange(proposedChangesJson);
		}

		itemService.saveRegisterItem(item);
		
		if (proposal instanceof SimpleProposal) {
			((SimpleProposal)proposal).setJustification(proposalDto.getJustification());
			((SimpleProposal)proposal).setRegisterManagerNotes(proposalDto.getRegisterManagerNotes());
			((SimpleProposal)proposal).setControlBodyNotes(proposalDto.getControlBodyNotes());
		}
		
		proposal = this.saveProposal(proposal);
		
		return proposal;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#withdrawProposal(de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation)
	 */
	@Override
	public Proposal withdrawProposal(Proposal proposal) throws InvalidProposalException, IllegalOperationException {
		proposal.withdraw();
		return this.saveProposal(proposal);
	}

	public List<RE_ProposalManagementInformation> findPendingProposalManagementInformation(RE_RegisterItem item) {
		List<RE_ProposalManagementInformation> pmis = pmiRepository.findByItemAndStatus(item, RE_DecisionStatus.PENDING);
		
		return pmis;
	}


	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#reviewProposal(de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem)
	 */
	@Override
	public Proposal reviewProposal(Proposal proposal) throws InvalidProposalException, IllegalOperationException, UnauthorizedException {
		if (proposal == null) {
			throw new InvalidProposalException("Cannot review null proposal.");
		}
		
		security.assertHasEntityRelatedRoleForAll(RegistrySecurity.MANAGER_ROLE_PREFIX, proposal.getAffectedRegisters());
		
		proposal.review(Calendar.getInstance().getTime());
		this.saveProposal(proposal);
		
		return proposal;
	}


	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#acceptProposal(de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem)
	 */
	@Override
	@Transactional
	public Proposal acceptProposal(Proposal proposal, String controlBodyDecisionEvent) throws InvalidProposalException,
			IllegalOperationException, UnauthorizedException {
		
		if (proposal == null) {
			throw new InvalidProposalException("Cannot accept null proposal.");
		}

		security.assertIsTrue(security.isControlBody(proposal.getUuid()));
		
		if (proposal.hasGroup()) {
			return this.acceptProposal(proposal.getGroup(), controlBodyDecisionEvent);
		}
		else {
			proposal.accept(controlBodyDecisionEvent);
			proposal = this.saveProposal(proposal);
			eventPublisher().publishEvent(new ProposalAcceptedEvent(proposal));
			
			return proposal;
		}
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#rejectProposal(de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation)
	 */
	@Override
	public Proposal rejectProposal(Proposal proposal, String controlBodyDecisionEvent) throws InvalidProposalException, IllegalOperationException, UnauthorizedException {
		if (proposal == null) {
			throw new InvalidProposalException("Cannot reject null proposal.");
		}
		
		security.assertIsTrue(security.isControlBody(proposal.getUuid()));

		proposal.reject(controlBodyDecisionEvent);
		proposal = this.saveProposal(proposal);
		
		eventPublisher().publishEvent(new ProposalRejectedEvent(proposal));
		
		return proposal;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#finalizeProposal(de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation)
	 */
	@Override
	public Proposal concludeProposal(Proposal proposal)
			throws InvalidProposalException, IllegalOperationException {
		
		if (proposal == null) {
			throw new InvalidProposalException("Cannot reject null proposal.");
		}

		proposal.conclude();
		proposal = this.saveProposal(proposal);
		
		return proposal;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#appealProposal(de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation)
	 */
	@Override
	public Appeal appealProposal(Proposal proposal, String justification, String situation, String impact) throws InvalidProposalException, IllegalOperationException {
		Appeal appeal = proposal.appeal(justification, impact, situation);

		appeal = appealRepository.save(appeal);
		
		return appeal;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#acceptAppeal(de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation)
	 */
	@Override
	public Appeal acceptAppeal(Appeal appeal) throws IllegalOperationException {
		if (appeal == null) {
			throw new NullPointerException("Cannot reject null proposal.");
		}

		appeal.accept(Calendar.getInstance().getTime());
		
		this.saveProposal(appeal.getAppealedProposal());
		appeal = appealRepository.save(appeal);
		return appeal;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#rejectAppeal(de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation)
	 */
	@Override
	public Appeal rejectAppeal(Appeal appeal) throws IllegalOperationException {
		if (appeal == null) {
			throw new NullPointerException("Cannot reject null proposal.");
		}

		appeal.reject(Calendar.getInstance().getTime());

		this.saveProposal(appeal.getAppealedProposal());
		appeal = appealRepository.save(appeal);
		return appeal;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#proposeRetirement(de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem)
	 */
	@Override
	public Retirement createRetirement(RE_RegisterItem item, String justification, String registerManagerNotes, String controlBodyNotes, RE_SubmittingOrganization sponsor) throws IllegalOperationException {
		Retirement proposal = item.proposeRetirement(justification, registerManagerNotes, controlBodyNotes, sponsor);
		proposal = this.saveProposal(proposal);
		
		eventPublisher().publishEvent(new ProposalCreatedEvent(proposal));

		return proposal;
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#proposeClarification(de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem, java.util.Map, java.lang.String, de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization)
	 */
	@Override
	public Clarification createClarification(RE_RegisterItem item, Map<String, String[]> proposedChanges,
			String justification, String registerManagerNotes, String controlBodyNotes, RE_SubmittingOrganization sponsor) throws IllegalOperationException {
		
		Clarification proposal = item.proposeClarification(proposedChanges, justification, registerManagerNotes, controlBodyNotes, sponsor);
		proposal = this.saveProposal(proposal);

		eventPublisher().publishEvent(new ProposalCreatedEvent(proposal));

		return proposal;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#proposeSupersession(java.util.Set, java.util.Set, java.lang.String, de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization)
	 */
	@Override
	public Supersession createSupersession(Set<RE_RegisterItem> supersededItems,
			Set<RegisterItemProposalDTO> successors, String justification, String registerManagerNotes, String controlBodyNotes, 
			RE_SubmittingOrganization sponsor)
			throws IllegalOperationException, InvalidProposalException {
		
		ArrayList<SimpleProposal> proposals = new ArrayList<SimpleProposal>();

		if (supersededItems == null || supersededItems.isEmpty()) {
			throw new InvalidProposalException("There must be at least one superseded item.");
		}
		
		if (successors == null || successors.isEmpty()) {
			throw new InvalidProposalException("There must be at least one successor.");
		}
		
		if (StringUtils.isEmpty(justification)) {
			throw new InvalidProposalException("A non-empty justification must be provided.");
		}

		Iterator<RE_RegisterItem> it = supersededItems.iterator();
		RE_Register targetRegister = it.next().getRegister();
		
		ArrayList<RE_RegisterItem> supersedingItems = new ArrayList<RE_RegisterItem>();
		for (RegisterItemProposalDTO successor : successors) {
			if (!successor.getTargetRegisterUuid().equals(targetRegister.getUuid())) {
				throw new InvalidProposalException("Proposed and superseded items must belong to same register");
			}
			successor.setJustification(justification); // Add supersession justification to successor
			successor.setRegisterManagerNotes(registerManagerNotes);
			successor.setControlBodyNotes(controlBodyNotes);
			successor.setSponsorUuid(sponsor.getUuid());
		
			Addition addition = this.createAdditionProposal(successor);
			supersedingItems.add(addition.getItem());
			proposals.add(addition);
		}

		for (RE_RegisterItem supersededItem : supersededItems) {
			if (!supersededItem.getRegister().getUuid().equals(targetRegister.getUuid())) {
				throw new InvalidProposalException("All proposed and superseded items must belong to the same register");
			}
			SupersessionPart supersession = supersededItem.proposeSupersession(justification, registerManagerNotes, controlBodyNotes, sponsor);
			proposals.add(supersession);
		}
		
		Supersession result = new Supersession(targetRegister, proposals, supersedingItems, justification);
		result.setRegisterManagerNotes(registerManagerNotes);
		result.setControlBodyNotes(controlBodyNotes);
		result.setSponsor(sponsor);
		
		for (Proposal proposal : proposals) {
			for (RE_ProposalManagementInformation pmi : proposal.getProposalManagementInformations()) {
				pmi.setJustification(justification);
				pmi.setRegisterManagerNotes(registerManagerNotes);
				pmi.setControlBodyNotes(controlBodyNotes);
				pmiRepository.save(pmi);
			}
		}

		result = this.saveProposal(result);

		eventPublisher().publishEvent(new ProposalCreatedEvent(result));

		return result;
	}
	
	@Override
	public Supersession updateSupersession(Supersession supersession, Set<RE_RegisterItem> supersededItems, Set<RE_RegisterItem> existingSuccessors,
			Set<RegisterItemProposalDTO> newSuccessors, String justification, String registerManagerNotes, String controlBodyNotes) throws InvalidProposalException {
		
		if (supersededItems == null || supersededItems.isEmpty()) {
			throw new InvalidProposalException("There must be at least one superseded item.");
		}
		
		if ((existingSuccessors == null || existingSuccessors.isEmpty()) && ((newSuccessors == null || newSuccessors.isEmpty()))) {
			throw new InvalidProposalException("There must be at least one successor.");
		}
		
		if (StringUtils.isEmpty(justification)) {
			throw new InvalidProposalException("A non-empty justification must be provided.");
		}

		Iterator<RE_RegisterItem> it = supersededItems.iterator();
		RE_Register targetRegister = it.next().getRegister();
		
		for (RE_RegisterItem supersededItem : supersededItems) {
			if (!supersededItem.getRegister().getUuid().equals(targetRegister.getUuid())) {
				throw new InvalidProposalException("All proposed and superseded items must belong to the same register");
			}
		}
		
		Set<RE_RegisterItem> noLongerSuperseded = new HashSet<RE_RegisterItem>();
		noLongerSuperseded.addAll(supersession.getSupersededItems());
		noLongerSuperseded.removeAll(supersededItems);
		
		Set<RE_RegisterItem> newlySuperseded = new HashSet<RE_RegisterItem>();
		newlySuperseded.addAll(supersededItems);
		newlySuperseded.removeAll(supersession.getSupersededItems());
		for (RE_RegisterItem supersededItem : newlySuperseded) {
			SupersessionPart part = supersededItem.proposeSupersession(justification, registerManagerNotes, controlBodyNotes, supersession.getSponsor());
			try {
				supersession.addProposal(part);
			} 
			catch (IllegalOperationException e) {
				throw new InvalidProposalException(e.getMessage(), e);
			}
		}
		
		Set<RE_RegisterItem> noLongerSuperseding = new HashSet<RE_RegisterItem>();
		noLongerSuperseding.addAll(supersession.getSupersedingItems());
		noLongerSuperseding.removeAll(existingSuccessors);

		Set<Proposal> perishableProposals = new HashSet<Proposal>();
		for (Proposal proposal : supersession.getProposals()) {
			if (proposal instanceof SupersessionPart) {
				if (noLongerSuperseded.contains(((SupersessionPart)proposal).getItem())) {
					perishableProposals.add(proposal);
				}
			}
			else if (proposal instanceof Addition) {
				if (noLongerSuperseding.contains(((Addition)proposal).getItem())) {
					perishableProposals.add(proposal);
				}
			}
		}
	
		supersession.removeSupersededItems(noLongerSuperseded);
		supersession.removeSupersedingItems(noLongerSuperseding);
		
			// TODO
			// 1. Erkennen, welche Items jetzt nicht mehr superseded sind und
			//   a. sie aus supersededItems entfernen
			//   b. die zugehörigen Proposals löschen
			// 2. Erkennen, welche Items jetzt neu superseded sind und entsprechende
			//    Proposals anlegen
			// 3. Entsprechend für supersedingItems vorgehen

		for (RegisterItemProposalDTO successor : newSuccessors) {
			if (!successor.getTargetRegisterUuid().equals(targetRegister.getUuid())) {
				throw new InvalidProposalException("Proposed and superseded items must belong to same register");
			}
			Addition addition = this.createAdditionProposal(successor);
			try {
				supersession.addProposal(addition);
			} 
			catch (IllegalOperationException e) {
				throw new InvalidProposalException(e.getMessage(), e);
			}
		}
		
		for (Proposal proposal : supersession.getProposals()) {
			for (RE_ProposalManagementInformation pmi : proposal.getProposalManagementInformations()) {
				pmi.setJustification(justification);
				pmi.setRegisterManagerNotes(registerManagerNotes);
				pmi.setControlBodyNotes(controlBodyNotes);
				pmiRepository.save(pmi);
			}
		}
		
		supersession.setTitle(supersession.buildName());
		supersession.setJustification(justification);
		supersession.setRegisterManagerNotes(registerManagerNotes);
		supersession.setControlBodyNotes(controlBodyNotes);

		supersession = this.saveProposal(supersession);
		proposalRepository.delete(perishableProposals);
		
		return supersession;
	}
	
	@Override
	public ProposalGroup createProposalGroup(List<Proposal> containedProposals, RE_SubmittingOrganization sponsor) throws InvalidProposalException {
		ProposalGroup result = new ProposalGroup(containedProposals);
		result.setSponsor(sponsor);
		
		result = proposalRepository.save(result); 
		eventPublisher().publishEvent(new ProposalCreatedEvent(result));

		return result;
	}

	@Override
	public ProposalGroup createProposalGroup(String name, List<Proposal> containedProposals, RE_SubmittingOrganization sponsor) throws InvalidProposalException {
		ProposalGroup result = new ProposalGroup(containedProposals, name);
		result.setSponsor(sponsor);
		
		result = proposalRepository.save(result); 
		eventPublisher().publishEvent(new ProposalCreatedEvent(result));

		return result;
	}

	@Override
	public Appeal findAppeal(UUID uuid) {
		return appealRepository.findOne(uuid);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#findAppeal(de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation)
	 */
	@Override
	public Appeal findAppeal(Proposal proposal) {
		return appealRepository.findByAppealedProposal(proposal);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#findSupersessions(java.util.List)
	 */
	@Override
	public Collection<Supersession> findSupersessions(Collection<RE_DecisionStatus> status) {
		List<Supersession> result = new ArrayList<Supersession>();
		for (Supersession supersession : supersessionRepository.findAll()) {
			if (status.contains(supersession.getDecisionStatus())) {
				result.add(supersession);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#findProposals(de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus)
	 */
	@Override
	public Collection<Proposal> findProposals(RE_Register register, RE_DecisionStatus status) {
		Collection<Proposal> proposals;
		if (status == RE_DecisionStatus.FINAL) {
			proposals = proposalRepository.findByDateSubmittedIsNotNullAndGroupIsNullAndIsConcludedIsTrue();
		}
		else {
			proposals = proposalRepository.findByDateSubmittedIsNotNullAndGroupIsNullAndIsConcludedIsFalse();
		}
		Collection<Proposal> result = new ArrayList<Proposal>();
		for (Proposal proposal : proposals) {
			if (proposal.getDecisionStatus().equals(status) && proposal.getAffectedRegisters().contains(register)) {
				result.add(proposal);
			}
		}
		
		return result;
	}

//	/* (non-Javadoc)
//	 * @see de.geoinfoffm.registry.api.RegisterItemService#findProposals(java.util.Collection)
//	 */
//	@Override
//	public Collection<Proposal> findProposals(RE_Register register, Collection<RE_DecisionStatus> status) {
//		Collection<Proposal> proposals = proposalRepository.findByDateSubmittedIsNotNull();
//		Collection<Proposal> result = new ArrayList<Proposal>();
//		for (Proposal proposal : proposals) {
//			if (status.contains(proposal.getStatus()) && proposal.getAffectedRegisters().contains(register)) {
//				result.add(proposal);
//			}
//		}
//		
//		return result;
//	}

	@Override
	public Proposal findProposal(UUID uuid) throws EntityNotFoundException {
		if (uuid == null) {
			throw new NullPointerException("UUID is null");
		}
		
		Proposal result = proposalRepository.findOne(uuid);
		if (result == null) {
			throw new EntityNotFoundException(String.format("Proposal %s does not exist.", uuid.toString()));
		}
		
		return result;
	}

	@Override
	public void approveProposalChange(Actor actor, ProposalChangeRequest changeRequest) {
		List<RE_Register> registers = changeRequest.getProposal().getAffectedRegisters();
		boolean isSubmitter = security.hasEntityRelatedRoleForAll(SUBMITTER_ROLE_PREFIX, registers);
		boolean isControlBody = security.hasEntityRelatedRoleForAll(CONTROLBODY_ROLE_PREFIX, registers);
		
		boolean mayGreenlight = (changeRequest.isEditedBySubmitter() ? isControlBody : isSubmitter);
		if (mayGreenlight) {
			changeRequest.setReviewed(true);
			pcrRepository.save(changeRequest);
		}
	}

	/**
	 * Delete a proposal and all items and other entities related to it. This operation can only be performed
	 * as long as the proposal is not concluded.
	 */
	@Transactional
	protected void deleteProposal(Proposal proposal) throws IllegalOperationException {
		if (proposal.isConcluded()) {
			throw new IllegalOperationException("Cannot delete concluded proposal");
		}
		
		if (proposal instanceof Addition) {
			// Delete the proposal, the proposed item and all proposal management information records
			Addition addition = (Addition)proposal;
			RE_RegisterItem item = addition.getItem();
			if (item.getStatus() != RE_ItemStatus.NOT_VALID) {
				throw new IllegalOperationException("Cannot delete proposal that references item with status != NOT_VALID");				
			}
			List<RE_ProposalManagementInformation> pmis = new ArrayList<>();
			pmis.addAll(item.getAdditionInformation());
			pmis.addAll(item.getClarificationInformation());
			pmis.addAll(item.getAmendmentInformation());
			while (!pmis.isEmpty()) {
				RE_ProposalManagementInformation pmi = pmis.get(0);
				pmis.remove(0);
				pmiRepository.delete(pmi);
			}
			itemRepository.delete(item);
			proposalRepository.delete(proposal);
		}
		else {
			throw new RuntimeException("Not yet implemented");
		}
	}

	@Override
	public List<Authorization> findAuthorizedControlBody(Proposal proposal) {
		return cbStrategy.findControlBodyAuthorizations(proposal);
	}

}
