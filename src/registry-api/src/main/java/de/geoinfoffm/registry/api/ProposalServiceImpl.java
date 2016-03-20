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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.isotc211.iso19139.common.CharacterString_PropertyType;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import de.bespire.LoggerFactory;
import de.bespire.registry.core.model.Invalidation;
import de.geoinfoffm.registry.api.soap.AbstractProposal_Type;
import de.geoinfoffm.registry.api.soap.AbstractRegisterItemProposal_Type;
import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.api.soap.Clarification_Type;
import de.geoinfoffm.registry.api.soap.ProposedChange_PropertyType;
import de.geoinfoffm.registry.api.soap.Retirement_Type;
import de.geoinfoffm.registry.api.soap.Supersession_Type;
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
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.OrganizationRepository;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalChangeRequest;
import de.geoinfoffm.registry.core.model.ProposalChangeRequestRepository;
import de.geoinfoffm.registry.core.model.ProposalFactory;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.ProposalRepository;
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
import de.geoinfoffm.registry.core.model.iso19135.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.core.workflow.ProposalWorkflowManager;
import de.geoinfoffm.registry.persistence.AppealRepository;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.RegisterItemRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;
import de.geoinfoffm.registry.persistence.ResponsiblePartyRepository;
import de.geoinfoffm.registry.persistence.SupersessionRepository;

/**
 * The class ProposalServiceImpl.
 *
 * @author Florian Esser
 */
@Transactional
//@Service
public class ProposalServiceImpl extends AbstractApplicationService<Proposal, ProposalRepository> implements ProposalService
{
	private static final Logger logger = LoggerFactory.make();
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private MessageSource messages;
	
	@Autowired
	private RegisterRepository registerRepository;
	
	@Autowired
	private SubmittingOrganizationRepository submittingOrgRepository;
	
	@Autowired
	private OrganizationRepository orgRepository;
	
	@Autowired
	private ResponsiblePartyRepository partyRepository;

	@Autowired
	private ItemClassRepository itemClassRepository;
	
	@Autowired
	private ProposalChangeRequestRepository pcrRepository;

	@Autowired
	private RegisterItemService itemService;
	
	@Autowired
	private ProposalRepository proposalRepository;
	
	@Autowired
	private ProposalFactory proposalFactory;
	
	@Autowired
	private ProposalManagementInformationRepository pmiRepository;

	@Autowired
	private ProposalWorkflowManager proposalWorkflowManager;
	
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
	public ProposalServiceImpl(ProposalRepository repository) {
		super(repository);
	}

	@Override
	public <P extends Proposal> P saveProposal(P proposal) {
		proposalWorkflowManager.initialize(proposal);
		
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
		
		if (proposal.hasParent()) {
			return this.submitProposal((P)proposal.getParent());
		}
	
		proposalWorkflowManager.submit(proposal, Calendar.getInstance().getTime());
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
		return createAdditionProposal(proposal, new HashMap<RegisterItemProposalDTO, Proposal>());
	}
		
	public Addition createAdditionProposal(RegisterItemProposalDTO proposal, Map<RegisterItemProposalDTO, Proposal> proposalsCreated) throws InvalidProposalException {
		if (proposalsCreated.containsKey(proposal)) {
			logger.debug("Using previously created proposal '{}'...", proposalsCreated.get(proposal).getTitle());
			return (Addition)proposalsCreated.get(proposal);
		}
		
		logger.debug("Creating new ADDITION proposal '{}'...", proposal.getName());

		RE_ItemClass itemClass;
		if (proposal.getItemClassUuid() != null) {
			itemClass = itemClassRepository.findOne(proposal.getItemClassUuid());			
		}
		else if (!StringUtils.isEmpty(proposal.getItemClassName())) {
			itemClass = itemClassRepository.findByName(proposal.getItemClassName());
		}
		else {
			throw new InvalidProposalException("Either name or UUID of item class must be provided");
		}
		
		if (itemClass == null) {
			throw new InvalidProposalException(String.format("Referenced item class does not exists [name: '%s'][UUID: %s]", proposal.getItemClassName(), proposal.getItemClassUuid()));			
		}
		
//		logger.debug(">>> Item class: {}", itemClass.getName());

		// check target register
		final UUID targetRegisterUuid = proposal.getTargetRegisterUuid();
		final RE_Register targetRegister = registerRepository.findOne(targetRegisterUuid);
		if(!targetRegister.getContainedItemClasses().contains(itemClass))
			throw new InvalidProposalException(String.format("item of itemclass '%s' is not allowed in target register '%s'.",itemClass.getName(),targetRegister.getName()));
//		logger.debug(">>> Target register: {}", targetRegister.getName());
		
		RE_SubmittingOrganization sponsor = submittingOrgRepository.findOne(proposal.getSponsorUuid());
		if (sponsor == null) {
			// try organization UUID
			Organization org = orgRepository.findOne(proposal.getSponsorUuid());
			if (org != null) {
				sponsor = org.getSubmittingOrganization();
			}
			else {
				throw new InvalidProposalException(String.format("No submitting organization with UUID '%s' found", proposal.getSponsorUuid()));
			}
		}
//		logger.debug(">>> Sponsor: {}", sponsor.getName());
		
		BindingResult bindingResult = validateProposal(proposal);
		if (bindingResult.hasErrors()) {
			throw new InvalidProposalException(bindingResult);
		}

		// Create dependent items and proposal first, so we can satisfy dependencies in the main item
		logger.debug(">>> Processing aggregate dependencies...");
		List<Proposal> groupProposals = new ArrayList<Proposal>();
		for (RegisterItemProposalDTO dependentProposal : proposal.getAggregateDependencies()) {
			logger.debug(">>> Found dependency '{}'", dependentProposal.getName());
			if (proposalsCreated.containsKey(dependentProposal)) {
				logger.debug(">>> Using previously processed proposal '{}'", proposalsCreated.get(dependentProposal).getTitle());
//				Addition addition = (Addition)proposalsCreated.get(dependentProposal);
//				for (PropertyDescriptor property : BeanUtils.getPropertyDescriptors(proposal.getClass())) {
//					setReferenceInPropertyIfNecessary(proposal, dependentProposal, property, addition, new Stack<Class<?>>());
//				}
			}
			else {		
				groupProposals.add(createDependentProposal(dependentProposal, proposal, sponsor, proposalsCreated));
			}
		}
		if (proposal.getName().contains("Lambert")) {
			new Object();
		}
		setReferences(proposal, proposalsCreated, new Stack<Class<?>>());
		logger.debug(">>> ...done");
		
		logger.debug(">>> Updating inter-proposal references...");
		for (RegisterItemProposalDTO createdProposal : proposalsCreated.keySet()) {
			setReferences(createdProposal, proposalsCreated, new Stack<Class<?>>());
		}

		// TODO delegate to service if possible
		RegisterItemFactory<RE_RegisterItem, RegisterItemProposalDTO> registerItemFactory = 
				(RegisterItemFactory<RE_RegisterItem, RegisterItemProposalDTO>)itemFactoryRegistry.getFactory(itemClass.getName());
		RE_RegisterItem item = registerItemFactory.createRegisterItem(proposal);
		
		if (item == null) {
			throw new NullPointerException(String.format("Factory %s returned null item", registerItemFactory.getClass().getCanonicalName()));
		}
		
		Addition addition = proposalFactory.createAddition(item, sponsor, 
				proposal.getJustification(), proposal.getRegisterManagerNotes(), proposal.getControlBodyNotes());
		logger.debug(">>> Register item '{}' created [uuid={}]", item.getName(), item.getUuid());

		addition = saveProposal(addition, item);
		proposalsCreated.put(proposal, addition);
		
		logger.debug(">>> Processing composite dependencies...");
		for (RegisterItemProposalDTO dependentProposal : proposal.getCompositeDependencies()) {
			dependentProposal.setParentItemUuid(addition.getItem().getUuid());
			groupProposals.add(createDependentProposal(dependentProposal, proposal, sponsor, proposalsCreated));
		}		
		logger.debug(">>> ...done");

		if (!groupProposals.isEmpty()) {
			logger.debug(">>> Grouping proposals...");
			addition.getDependentProposals().addAll(groupProposals);
			for (Proposal groupProposal : groupProposals) {
				groupProposal.setParent(addition);
				proposalRepository.save(groupProposal);
			}
			logger.debug(">>> ...done");
		}
		
		eventPublisher().publishEvent(new ProposalCreatedEvent(addition));

		return addition;
	}
	
	private void setReferences(Object target, Map<RegisterItemProposalDTO, Proposal> proposalsCreated, Stack<Class<?>> classes) {
		for (PropertyDescriptor property : BeanUtils.getPropertyDescriptors(target.getClass())) {
			try {
				if (property.getName().equals("axes")) {
					new Object();
				}
				if (RegisterItemProposalDTO.class.isAssignableFrom(property.getPropertyType())) {
					RegisterItemProposalDTO dependentProposal = (RegisterItemProposalDTO)property.getReadMethod().invoke(target);
					if (dependentProposal != null && dependentProposal.getReferencedItemUuid() == null && proposalsCreated.containsKey(dependentProposal)) {
						// Set the referencedItemUuid property so that the RegisterItemFactory can reference the correct item
						Proposal proposal = proposalsCreated.get(dependentProposal);
						if (proposal instanceof Addition) {
							Addition addition = (Addition)proposal;
							dependentProposal.setReferencedItemUuid(addition.getItem().getUuid());
							logger.debug(">>>>>> Added reference to previously created item '{}' in property '{}' of object '{}'", new Object[] { addition.getItem().getName(), property.getName(), target.toString() });
							property.getWriteMethod().invoke(target, dependentProposal);
						}
					}
				}
				else if (Collection.class.isAssignableFrom(property.getPropertyType())) {
					Field field = ReflectionUtils.findField(target.getClass(), property.getName());
					if (field != null) {
						Type t = ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
						if (t instanceof Class) {
							if (RegisterItemProposalDTO.class.isAssignableFrom((Class)t)) {
								for (RegisterItemProposalDTO value : (Collection<RegisterItemProposalDTO>)property.getReadMethod().invoke(target)) {
									if (value != null && value.getReferencedItemUuid() == null && proposalsCreated.containsKey(value)) {
										// Set the referencedItemUuid property so that the RegisterItemFactory can reference the correct item
										Proposal proposal = proposalsCreated.get(value);
										if (proposal instanceof Addition) {
											Addition addition = (Addition)proposal;
											value.setReferencedItemUuid(addition.getItem().getUuid());
											logger.debug(">>>>>> Added reference to previously created item '{}' in property '{}' of object '{}'", new Object[] { addition.getItem().getName(), property.getName(), target.toString() });
										}
									}
								}
							}
							else if (!BeanUtils.isSimpleValueType((Class)t) && !UUID.class.equals((Class)t) && !Map.class.isAssignableFrom((Class)t)) {
								if (property.getName().equals("parameterValues")) {
									new Object();
								}
								for (Object value : (Collection<?>)property.getReadMethod().invoke(target)) {
									for (PropertyDescriptor subProperty : BeanUtils.getPropertyDescriptors(value.getClass())) {
										if (!classes.contains(subProperty.getPropertyType())) {
											classes.push(subProperty.getPropertyType());
											setReferences(value, proposalsCreated, classes);
											classes.pop();
										}
										else {
											new Object();
										}
									}
								}
							}
						}
					}
				}
				else if (!BeanUtils.isSimpleProperty(property.getPropertyType()) && !UUID.class.equals(property.getPropertyType()) && !Map.class.isAssignableFrom(property.getPropertyType())) {
					// Check if property is itself an object that has a property that references the proposal
					for (PropertyDescriptor subProperty : BeanUtils.getPropertyDescriptors(property.getPropertyType())) {
						if (!classes.contains(subProperty.getPropertyType())) {
							Object value = property.getReadMethod().invoke(target);
							if (value != null) {
								classes.push(subProperty.getPropertyType());
								setReferences(value, proposalsCreated, classes);
								classes.pop();
							}
						}
						else {
							new Object();
						}
					}
				}
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void setReferenceInPropertyIfNecessary(Object dto, RegisterItemProposalDTO dependentProposal, PropertyDescriptor property, Addition addition, Stack<Class<?>> classes) {
		try {
			if (property.getReadMethod().getDeclaringClass().isAssignableFrom(dto.getClass()) && dependentProposal.getClass().isAssignableFrom(property.getPropertyType()) && property.getReadMethod().invoke(dto) == dependentProposal) {
				RegisterItemProposalDTO value = (RegisterItemProposalDTO)property.getReadMethod().invoke(dto);
				// Set the referencedItemUuid property so that the RegisterItemFactory can reference the correct item
				value.setReferencedItemUuid(addition.getItem().getUuid());
				logger.debug(">>>>>> Added reference to previously created item '{}' in property '{}' of object '{}'", new Object[] { addition.getItem().getName(), property.getName(), dto.toString() });
				property.getWriteMethod().invoke(dto, value);
			}
			else if (Collection.class.isAssignableFrom(property.getPropertyType())) {
				if (property.getName().equals("axes")) {
					new Object();
				}
				Field field = ReflectionUtils.findField(dto.getClass(), property.getName());
				if (field != null) {
					Type t = ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
					if (t instanceof Class) {
						if (dependentProposal.getClass().isAssignableFrom((Class)t)) {
							for (RegisterItemProposalDTO value : (Collection<RegisterItemProposalDTO>)property.getReadMethod().invoke(dto)) {
								if (value == dependentProposal) {
									value.setReferencedItemUuid(addition.getItem().getUuid());
								}
							}
						}
						else if (!BeanUtils.isSimpleValueType((Class)t) && !UUID.class.equals((Class)t) && !Map.class.isAssignableFrom((Class)t)) {
							if (property.getName().equals("parameterValues")) {
								new Object();
							}
							for (Object value : (Collection<?>)property.getReadMethod().invoke(dto)) {
								for (PropertyDescriptor subProperty : BeanUtils.getPropertyDescriptors(value.getClass())) {
									if (!classes.contains(subProperty.getPropertyType())) {
										classes.push(subProperty.getPropertyType());
										setReferenceInPropertyIfNecessary(value, dependentProposal, subProperty, addition, classes);
										classes.pop();
									}
									else {
										new Object();
									}
								}
							}
						}
					}
				}
			}
			else if (!BeanUtils.isSimpleProperty(property.getPropertyType()) && !UUID.class.equals(property.getPropertyType()) && !Map.class.isAssignableFrom(property.getPropertyType())) {
				// Check if property is itself an object that has a property that references the proposal
				for (PropertyDescriptor subProperty : BeanUtils.getPropertyDescriptors(property.getPropertyType())) {
					if (!classes.contains(subProperty.getPropertyType())) {
						classes.push(subProperty.getPropertyType());
						setReferenceInPropertyIfNecessary(dto, dependentProposal, subProperty, addition, classes);
						classes.pop();
					}
					else {
						new Object();
					}
				}
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassCastException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private Proposal createDependentProposal(RegisterItemProposalDTO proposal, RegisterItemProposalDTO mainProposal, RE_SubmittingOrganization sponsor, Map<RegisterItemProposalDTO, Proposal> proposalsCreated) throws InvalidProposalException {
		BindingResult bindingResult;
		bindingResult = validateProposal(proposal);
		if (bindingResult.hasErrors()) {
			throw new InvalidProposalException(bindingResult);
		}
		
		logger.debug(">>> Creating dependent proposal '{}' [uuid={}]", proposal.getName(), proposal.getUuid());
		
		proposal.setTargetRegisterUuid(mainProposal.getTargetRegisterUuid());
		proposal.setSponsorUuid(mainProposal.getSponsorUuid());
		proposal.setJustification(mainProposal.getJustification());
		proposal.setProposalType(ProposalType.ADDITION);
		
		List<Proposal> subsubProposals = new ArrayList<Proposal>();
		for (RegisterItemProposalDTO dependentProposal : proposal.getAggregateDependencies()) {
			if (dependentProposal == proposal) continue;
			if (proposalsCreated.containsKey(dependentProposal)) {
				logger.debug(">>> Using previously processed proposal '{}'", proposalsCreated.get(dependentProposal).getTitle());
				Addition addition = (Addition)proposalsCreated.get(dependentProposal);
				// Find the property of the proposal that contains the dependent proposal
				for (PropertyDescriptor property : BeanUtils.getPropertyDescriptors(proposal.getClass())) {
					try {
						if (dependentProposal.getClass().isAssignableFrom(property.getPropertyType()) && property.getReadMethod().invoke(proposal) == dependentProposal) {
							RegisterItemProposalDTO value = (RegisterItemProposalDTO)property.getReadMethod().invoke(proposal);
							// Set the referencedItemUuid property so that the RegisterItemFactory can reference the correct item
							value.setReferencedItemUuid(addition.getItem().getUuid());
							property.getWriteMethod().invoke(proposal, value);
							break;
						}
					}
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
			else {		
				subsubProposals.add(createDependentProposal(dependentProposal, proposal, sponsor, proposalsCreated));
			}
		}		
		
		// TODO Justification etc. müssen aus ProposalDTO des abhängigen Items kommen!

		RE_ItemClass dependentProposalItemClass = this.findItemClass(proposal);
		if (dependentProposalItemClass == null) {
			new Object();
		}
		RegisterItemFactory<RE_RegisterItem, RegisterItemProposalDTO> itemFactory = 
				(RegisterItemFactory<RE_RegisterItem, RegisterItemProposalDTO>)itemFactoryRegistry.getFactory(dependentProposalItemClass.getName());
		RE_RegisterItem dependentItem = itemFactory.createRegisterItem(proposal);

		if (dependentItem == null) {
			throw new NullPointerException(String.format("Factory %s returned null item", itemFactory.getClass().getCanonicalName()));
		}
		logger.debug(">>>>> Dependent item '{}' created [uuid={}]", dependentItem.getName(), dependentItem.getUuid());
		
		Addition subAddition = proposalFactory.createAddition(dependentItem, sponsor, 
				proposal.getJustification(), proposal.getRegisterManagerNotes(), proposal.getControlBodyNotes());
		
		subAddition = saveProposal(subAddition, dependentItem);
		
		proposalsCreated.put(proposal, subAddition);

		// Set the referenced item UUID in the proposal so that it may be referenced the main item
		proposal.setReferencedItemUuid(subAddition.getItem().getUuid());
		
		for (RegisterItemProposalDTO dependentProposal : proposal.getCompositeDependencies()) {
			dependentProposal.setParentItemUuid(subAddition.getItem().getUuid());
			subsubProposals.add(createDependentProposal(dependentProposal, proposal, sponsor, proposalsCreated));
		}		
		
		if (!subsubProposals.isEmpty()) {
			subAddition.getDependentProposals().addAll(subsubProposals);
			for (Proposal subsubProposal : subsubProposals) {
				subsubProposal.setParent(subAddition);
			}
		}
		eventPublisher().publishEvent(new ProposalCreatedEvent(subAddition));
	
		return subAddition;
	}

	private BindingResult validateProposal(RegisterItemProposalDTO proposal) {
		DataBinder binder = new DataBinder(proposal);
		binder.setValidator(validator);
		binder.validate();
		BindingResult bindingResult = binder.getBindingResult();

		return bindingResult;
	}

	private Addition saveProposal(Addition addition, RE_RegisterItem item) {

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
		
		Map<RegisterItemProposalDTO, Proposal> proposalsCreated =  new HashMap<RegisterItemProposalDTO, Proposal>();

		BindingResult bindingResult = validateProposal(proposalDto);
		if (bindingResult.hasErrors()) {
			throw new InvalidProposalException(bindingResult);
		}
		
		if (!proposal.getDependentProposals().isEmpty()) {
			List<Proposal> noLongerDependent = new ArrayList<Proposal>();
			noLongerDependent.addAll(proposal.getDependentProposals());

			// Remove those proposals from 'no longer dependent' list that are still dependent
			List<RegisterItemProposalDTO> allDependent = new ArrayList<RegisterItemProposalDTO>();
			allDependent.addAll(proposalDto.getAggregateDependencies());
			allDependent.addAll(proposalDto.getCompositeDependencies());

			for (RegisterItemProposalDTO subDto : allDependent) {
				if (subDto.getProposalUuid() != null && !subDto.isMarkedForDeletion()) {
					Proposal subProposal = proposalRepository.findOne(subDto.getProposalUuid());
					noLongerDependent.remove(subProposal);
				}
			}
			
			List<RegisterItemProposalDTO> newlyDependent = new ArrayList<RegisterItemProposalDTO>();
			for (RegisterItemProposalDTO subDto : allDependent) {
				if (subDto.getProposalUuid() == null) {
					subDto.setProposalType(proposalDto.getProposalType());
					newlyDependent.add(subDto);
				}
				else {
					this.updateProposal(subDto);
				}
			}
			
			for (Proposal perishable : noLongerDependent) {
				perishable.setParent(null);
				proposal.getDependentProposals().remove(perishable);
				this.saveProposal(proposal);
			}
			while (!noLongerDependent.isEmpty()) {
				Proposal p = noLongerDependent.get(0);
				noLongerDependent.remove(0);
				this.deleteProposal(p);
			}
			
			for (RegisterItemProposalDTO newSub : newlyDependent) {
				newSub.setParentItemUuid(proposalDto.getItemUuid());
				Proposal subProposal = createDependentProposal(newSub, proposalDto, proposal.getSponsor(), proposalsCreated);
				subProposal.setParent(proposal);
				proposal.getDependentProposals().add(subProposal);
			}

		}

		if (proposalDto.getProposalType().equals(ProposalType.SUPERSESSION)) {
			throw new RuntimeException("Use updateSupersession(...)");
		}
		else {
			RE_RegisterItem item = itemService.findOne(proposalDto.getItemUuid());
			if (item == null) {
				throw new InvalidProposalException(String.format("Referenced item with id '%s' does not exist.", proposalDto.getItemUuid()));
			}
			
			proposal.setTitle(proposalDto.getName());

			if (proposal instanceof Addition) {
				Addition addition = (Addition)proposal;
				
				item.setName(proposalDto.getName());
				item.setDefinition(proposalDto.getDefinition());
				item.setDescription(proposalDto.getDescription());				
				
				proposalDto.setAdditionalValues(item, this.entityManager);

				List<Proposal> dependentProposals = new ArrayList<Proposal>();
				for (RegisterItemProposalDTO dependentProposal : proposalDto.getCompositeDependencies()) {
					if (dependentProposal.getProposalUuid() == null) {
						dependentProposal.setParentItemUuid(addition.getItem().getUuid());
						dependentProposals.add(createDependentProposal(dependentProposal, proposalDto, proposal.getSponsor(), proposalsCreated));
					}
				}
				
				if (!dependentProposals.isEmpty()) {
					addition.getDependentProposals().addAll(dependentProposals);
					for (Proposal dependentProposal : dependentProposals) {
						dependentProposal.setParent(addition);
					}
				}
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
			Map<String, List<String>> proposedChanges = new HashMap<String, List<String>>();
			for (ProposedChange_PropertyType change : ((Clarification_Type)proposalDto).getProposedChange()) {
				if (!change.isSetProposedChange()) continue;

				List<String> newValues = new ArrayList<String>();
				for (CharacterString_PropertyType newValue : change.getProposedChange().getNewValue()) {
					newValues.add(newValue.getCharacterString().getValue().toString());
				}
				proposedChanges.put(change.getProposedChange().getProperty(), newValues);
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
		proposalWorkflowManager.withdraw(proposal);
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
		
		proposalWorkflowManager.review(proposal, Calendar.getInstance().getTime());
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
		
		if (proposal.hasParent()) {
			return this.acceptProposal(proposal.getParent(), controlBodyDecisionEvent);
		}
		else {
			proposalWorkflowManager.accept(proposal, controlBodyDecisionEvent);
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

		proposalWorkflowManager.reject(proposal, controlBodyDecisionEvent);
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

		proposalWorkflowManager.conclude(proposal);
		proposal = this.saveProposal(proposal);
		
		return proposal;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#appealProposal(de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation)
	 */
	@Override
	public Appeal appealProposal(Proposal proposal, String justification, String situation, String impact) throws InvalidProposalException, IllegalOperationException {
		Assert.notNull(proposal, "Cannot appeal null proposal");
		
		List<Appeal> appeals = appealRepository.findByAppealedProposal(proposal);
		Appeal appeal;
		if (appeals.isEmpty()) {
			appeal = proposalWorkflowManager.appeal(proposal, justification, impact, situation);
		}
		else {
			appeal = appeals.get(0);
			appeal.setJustification(justification);
			appeal.setImpact(impact);
			appeal.setSituation(situation);
		}

		appeal = appealRepository.save(appeal);
		
		return appeal;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#acceptAppeal(de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation)
	 */
	@Override
	public Appeal acceptAppeal(Appeal appeal) throws IllegalOperationException {
		Assert.notNull(appeal, "Cannot reject null proposal");
		
		proposalWorkflowManager.acceptAppeal(appeal, Calendar.getInstance().getTime());
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
		
		proposalWorkflowManager.rejectAppeal(appeal, Calendar.getInstance().getTime());
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
	 * @see de.geoinfoffm.registry.api.RegisterItemService#proposeInvalidation(de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem)
	 */
	@Override
	public Invalidation createInvalidation(RE_RegisterItem item, String justification, String registerManagerNotes, String controlBodyNotes, RE_SubmittingOrganization sponsor) throws IllegalOperationException {
		Invalidation proposal = item.proposeInvalidation(justification, registerManagerNotes, controlBodyNotes, sponsor);
		proposal = this.saveProposal(proposal);
		
		eventPublisher().publishEvent(new ProposalCreatedEvent(proposal));

		return proposal;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#proposeClarification(de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem, java.util.Map, java.lang.String, de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization)
	 */
	@Override
	public Clarification createClarification(RE_RegisterItem item, Map<String, List<String>> proposedChanges,
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
		
		proposalWorkflowManager.initialize(result);
		
		result = proposalRepository.save(result); 
		eventPublisher().publishEvent(new ProposalCreatedEvent(result));

		return result;
	}

	@Override
	public ProposalGroup createProposalGroup(String name, List<Proposal> containedProposals, RE_SubmittingOrganization sponsor) throws InvalidProposalException {
		ProposalGroup result = new ProposalGroup(containedProposals, name);
		result.setSponsor(sponsor);

		proposalWorkflowManager.initialize(result);

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
		List<Appeal> appeals = appealRepository.findByAppealedProposal(proposal);
		if (appeals.isEmpty()) {
			return null;
		}
		else {
			return appeals.get(0);
		}
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
			proposals = proposalRepository.findByDateSubmittedIsNotNullAndParentIsNullAndIsConcludedIsTrue();
		}
		else {
			proposals = proposalRepository.findByDateSubmittedIsNotNullAndParentIsNullAndIsConcludedIsFalse();
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
		boolean isSubmitter = security.maySubmitToAll(registers);
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
	 * @throws UnauthorizedException 
	 */
	@Override
	@Transactional
	public void deleteProposal(Proposal proposal) throws IllegalOperationException, UnauthorizedException {
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
