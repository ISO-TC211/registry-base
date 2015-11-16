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

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.isotc211.iso19135.RE_RegisterItem_Type;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.bespire.PersistentCollectionUtils;
import de.geoinfoffm.registry.api.soap.AbstractRegisterItemProposal_Type;
import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.Clarification;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.ProposalType;
import de.geoinfoffm.registry.core.model.Retirement;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

/**
 * @author Florian Esser
 *
 */
public class RegisterItemProposalDTO
{	
	// Used to reference existing items
	private UUID referencedItemUuid;
	private UUID parentItemUuid;
	
	private UUID itemUuid;
	private UUID proposalUuid;
	private UUID itemClassUuid;
	private String itemClassName;
	private String name;
	private String definition;
	private String description;
	private UUID targetRegisterUuid;
	private UUID sponsorUuid;
	private String justification;
	private String registerManagerNotes;
	private String controlBodyNotes;
	private ProposalType proposalType;
	
	private boolean markedForDeletion;
	
	private final Map<String, String[]> originalValues = new HashMap<String, String[]>();
	
	private final Set<UUID> supersededItems = new HashSet<UUID>();
	private final Set<RegisterItemProposalDTO> newSupersedingItems = new HashSet<RegisterItemProposalDTO>();
	private final Set<UUID> existingSupersedingItems = new HashSet<UUID>();
	
	private final Set<UUID> dependentProposals = new HashSet<>();
	
	public RegisterItemProposalDTO() {
		itemUuid = UUID.randomUUID();
	}
	
	protected RegisterItemProposalDTO(String itemClassName) {
		this();
		this.itemClassName = itemClassName;
	}
	
	public RegisterItemProposalDTO(RE_RegisterItem referencedItem) {
		if (referencedItem != null) {
			this.referencedItemUuid = referencedItem.getUuid();
		}
	}
	
	public RegisterItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		if (item.isSetUuid()) {
			this.setItemUuid(UUID.fromString(item.getUuid()));
		}
		else {
			this.setItemUuid(UUID.randomUUID());
		}
		this.setName(item.getName().getCharacterString().getValue().toString());
		this.setDefinition(item.getDefinition().getCharacterString().getValue().toString());
		if (item.isSetDescription()) {
			this.setDescription(item.getDescription().getCharacterString().getValue().toString());
		}
		this.setSponsorUuid(sponsor.getUuid());
		if (!item.isSetItemClass() || !item.getItemClass().isSetUuidref()) {
			throw new IllegalArgumentException("Item must contain uuidref to item class");
		}
		this.setItemClassUuid(UUID.fromString(item.getItemClass().getUuidref()));
	}
	
	public RegisterItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
//		if (proposal instanceof Addition_Type) {
//			initializeFromAdditionType((Addition_Type)proposal);
//		}
//		else if (proposal instanceof Clarification_Type) {
//			intializeFromClarificationType((Clarification_Type)proposal);
//		}
//		else if (proposal instanceof Retirement_Type) {
//			
//		}
		
		this.setSponsorUuid(sponsor.getUuid());
		initializeFromAdditionType(proposal);
	}
	
	public RegisterItemProposalDTO(AbstractRegisterItemProposal_Type itemDetails) {
		initializeFromItemDetails(itemDetails);
	}
	
	private void initializeFromGroup(ProposalGroup group) {
		this.setName(group.getTitle());
		this.setProposalUuid(group.getUuid());
		this.setProposalType(ProposalType.GROUP);
		this.setSponsorUuid(group.getSponsor().getUuid());
	}

	protected void initializeFromItem(RE_RegisterItem_Type proposedItem) {
		if (proposedItem.getItemClass() == null) {
			throw new IllegalArgumentException("Proposed item must reference an item class");
		}

		this.setName(proposedItem.getName().getCharacterString().getValue().toString());
		this.setDefinition(proposedItem.getDefinition().getCharacterString().getValue().toString());
		if (proposedItem.getDescription() != null) {
			this.setDescription(proposedItem.getDescription().getCharacterString().getValue().toString());
		}
		
		if (!StringUtils.isEmpty(proposedItem.getItemClass().getUuidref())) {
			// Item class is referenced by uuid
			this.setItemClassUuid(UUID.fromString(proposedItem.getItemClass().getUuidref()));
		}
		else if (proposedItem.getItemClass().getRE_ItemClass() != null) {
			// Item class is described
			this.setItemUuid(UUID.fromString(proposedItem.getItemClass().getRE_ItemClass().getUuid()));
		}
		else {
			throw new IllegalArgumentException("Proposed item must reference an item class or contain an item class description");
		}
	}

	protected void initializeFromItemDetails(AbstractRegisterItemProposal_Type itemDetails) {
		if (itemDetails.getItemClassUuid() == null) {
			throw new IllegalArgumentException("Proposed item must reference an item class");
		}

		this.setName(itemDetails.getName());
		this.setDefinition(itemDetails.getDefinition());
		if (itemDetails.getDescription() != null) {
			this.setDescription(itemDetails.getDescription());
		}
		
		if (!StringUtils.isEmpty(itemDetails.getItemClassUuid())) {
			// Item class is referenced by uuid
			this.setItemClassUuid(UUID.fromString(itemDetails.getItemClassUuid()));
		}
		else {
			throw new IllegalArgumentException("Proposed item must reference an item class or contain an item class description");
		}
	}

	protected void initializeFromAdditionType(Addition_Type addition) {
		this.setProposalType(ProposalType.ADDITION);

		AbstractRegisterItemProposal_Type itemDetails = addition.getItemDetails().getAbstractRegisterItemProposal().getValue();
		initializeFromItemDetails(itemDetails);

		this.setTargetRegisterUuid(UUID.fromString(addition.getTargetRegisterUuid()));
		this.setJustification(addition.getJustification());
		this.setRegisterManagerNotes(addition.getRegisterManagerNotes());
		this.setControlBodyNotes(addition.getControlBodyNotes());
	}
//	
//	private void intializeFromClarificationType(Clarification_Type clarification) {
//		this.setProposalType(ProposalType.CLARIFICATION);
//		
//		initializeFromItem(proposedItem);
//		
//		Map<String, String> proposedChanges = new HashMap<String, String>();
//		for (ProposedChange_PropertyType change : clarification.getProposedChange()) {
//			if (!change.isSetProposedChange()) continue;
//			
//			proposedChanges.put(change.getProposedChange().getProperty(), change.getProposedChange().getNewValue().getCharacterString().toString());
//		}
//		String json = RE_ClarificationInformation.toJson(proposedChanges);
//		Map<String, String> originalValues = this.applyProposedChanges(json);
//		for (String propertyName : originalValues.keySet()) {
//			this.addOriginalValue(propertyName, originalValues.get(propertyName));
//		}
//	}
//	
//	private void initializeFromRetirementType(Retirement_Type retirement) {
//		this.setProposalType(ProposalType.RETIREMENT);
//		
//		initializeFromItem(retirement.getRetiredItemUuid());
//	}
	
	public RegisterItemProposalDTO(Proposal proposal) {
		this.proposalUuid = proposal.getUuid();
		
		if (proposal instanceof SimpleProposal) {
			initializeFromSimpleProposal((SimpleProposal)proposal);
		}
		else if (proposal instanceof Supersession) {
			initializeFromSupersession((Supersession)proposal);
		}
		else if (proposal instanceof ProposalGroup) {
			initializeFromGroup((ProposalGroup)proposal);
		}
	}
	
	private void initializeFromSimpleProposal(SimpleProposal proposal) {
		initializeFromItem(proposal.getItem());
		
		this.setProposalUuid(proposal.getUuid());
		this.setJustification(proposal.getJustification());
		this.setRegisterManagerNotes(proposal.getRegisterManagerNotes());
		this.setControlBodyNotes(proposal.getControlBodyNotes());
		this.setSponsorUuid(proposal.getSponsor().getUuid());
		
		if (proposal instanceof Addition) {
			this.setProposalType(ProposalType.ADDITION);
		}
		else if (proposal instanceof Clarification) {
			this.setProposalType(ProposalType.CLARIFICATION);

			Map<String, String[]> originalValues = this.applyProposedChanges(((Clarification)proposal).getProposedChange());
			for (String propertyName : originalValues.keySet()) {
				this.addOriginalValue(propertyName, originalValues.get(propertyName));
			}
		}
		else if (proposal instanceof Retirement) {
			this.setProposalType(ProposalType.RETIREMENT);
		}
		
		this.setTargetRegisterUuid(proposal.getRegister().getUuid());
	}
	
	private void initializeFromSupersession(Supersession supersession) {
		this.setProposalUuid(supersession.getUuid());
		this.setProposalType(ProposalType.SUPERSESSION);
		this.setSponsorUuid(supersession.getSponsor().getUuid());
		
		if (!supersession.getSupersessionParts().isEmpty()) {
			this.setJustification(supersession.getSupersessionParts().get(0).getJustification());
			this.setRegisterManagerNotes(supersession.getSupersessionParts().get(0).getRegisterManagerNotes());
			this.setControlBodyNotes(supersession.getSupersessionParts().get(0).getControlBodyNotes());
		}
			
		for (RE_RegisterItem supersededItem : supersession.getSupersededItems()) {
			this.supersededItems.add(supersededItem.getUuid());
		}
		
		for (RE_RegisterItem supersedingItem : supersession.getSupersedingItems()) {
			this.existingSupersedingItems.add(supersedingItem.getUuid());
		}
	}
	
	private void initializeFromItem(RE_RegisterItem item) {
		this.setItemUuid(item.getUuid());
		this.setDefinition(item.getDefinition());
		this.setDescription(item.getDescription());
		this.setItemClassUuid(item.getItemClass().getUuid());
		this.setItemUuid(item.getUuid());
		this.setName(item.getName());
		
		this.loadAdditionalValues(item);
	}
	
	/**
	 * Must be overwritten by extending classes to set additional
	 * property values.
	 */
	public void setAdditionalValues(RE_RegisterItem item, EntityManager entityManager) {
		// does nothing here
	}
	
	public void loadAdditionalValues(RE_RegisterItem item) {
		
	}
	
	public void loadDependentProposalDetails(Collection<RegisterItemProposalDTO> dependentProposals) {
	}

	public UUID getReferencedItemUuid() {
		return referencedItemUuid;
	}

	public void setReferencedItemUuid(UUID referencedItemUuid) {
		this.referencedItemUuid = referencedItemUuid;
	}
	
	public UUID getParentItemUuid() {
		return parentItemUuid;
	}

	public void setParentItemUuid(UUID parentItemUuid) {
		this.parentItemUuid = parentItemUuid;
	}

	public UUID getUuid() {
		return itemUuid;
	}

	/**
	 * @return the itemUuid
	 */
	public UUID getItemUuid() {
		return itemUuid;
	}

	/**
	 * @param itemUuid the itemUuid to set
	 */
	public void setItemUuid(UUID itemUuid) {
		this.itemUuid = itemUuid;
	}

	/**
	 * @return the proposalUuid
	 */
	public UUID getProposalUuid() {
		return proposalUuid;
	}

	/**
	 * @param proposalUuid the proposalUuid to set
	 */
	public void setProposalUuid(UUID proposalUuid) {
		this.proposalUuid = proposalUuid;
	}

	/**
	 * @return the itemClassUuid
	 */
	public UUID getItemClassUuid() {
		return itemClassUuid;
	}

	/**
	 * @param itemClassUuid the itemClassUuid to set
	 */
	public void setItemClassUuid(UUID itemClassUuid) {
		this.itemClassUuid = itemClassUuid;
	}

	public String getItemClassName() {
		return itemClassName;
	}

	public void setItemClassName(String itemClassName) {
		this.itemClassName = itemClassName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * @param definition the definition to set
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the targetRegisterUuid
	 */
	public UUID getTargetRegisterUuid() {
		return targetRegisterUuid;
	}

	/**
	 * @param targetRegisterUuid the targetRegisterUuid to set
	 */
	public void setTargetRegisterUuid(UUID targetRegisterUuid) {
		this.targetRegisterUuid = targetRegisterUuid;
	}

	/**
	 * @return the sponsorUuid
	 */
	public UUID getSponsorUuid() {
		return sponsorUuid;
	}

	/**
	 * @param sponsorUuid the sponsorUuid to set
	 */
	public void setSponsorUuid(UUID sponsorUuid) {
		this.sponsorUuid = sponsorUuid;
	}

	/**
	 * @return the justification
	 */
	public String getJustification() {
		return justification;
	}

	/**
	 * @param justification the justification to set
	 */
	public void setJustification(String justification) {
		this.justification = justification;
	}

	/**
	 * @return the registerManagerNotes
	 */
	public String getRegisterManagerNotes() {
		return registerManagerNotes;
	}

	/**
	 * @param registerManagerNotes the registerManagerNotes to set
	 */
	public void setRegisterManagerNotes(String registerManagerNotes) {
		this.registerManagerNotes = registerManagerNotes;
	}

	/**
	 * @return the controlBodyNotes
	 */
	public String getControlBodyNotes() {
		return controlBodyNotes;
	}

	/**
	 * @param controlBodyNotes the controlBodyNotes to set
	 */
	public void setControlBodyNotes(String controlBodyNotes) {
		this.controlBodyNotes = controlBodyNotes;
	}

	/**
	 * @return the proposalType
	 */
	public ProposalType getProposalType() {
		return proposalType;
	}

	/**
	 * @param proposalType the proposalType to set
	 */
	public void setProposalType(ProposalType proposalType) {
		this.proposalType = proposalType;
	}
	
	public boolean isMarkedForDeletion() {
		return markedForDeletion;
	}

	public void setMarkedForDeletion(boolean markedForDeletion) {
		this.markedForDeletion = markedForDeletion;
	}

	/**
	 * @return the proposedChanges
	 */
	public Map<String, String[]> getOriginalValues() {
		return Collections.unmodifiableMap(originalValues);
	}
	
	public Map<String, String[]> applyProposedChanges(String json) {
		Map<String, String[]> originalValues = new HashMap<String, String[]>();
		
		ObjectMapper jsonMapper = new ObjectMapper();
		try {
			Map<String, List<String>> proposedChanges = jsonMapper.readValue(json, Map.class);
			for (String propertyName : proposedChanges.keySet()) {
				PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(this.getClass(), propertyName);
				if (pd == null) {
					continue;
				}
		
				Object origValue = pd.getReadMethod().invoke(this);
				if (Collection.class.isAssignableFrom(pd.getPropertyType())) {
					if (origValue == null) {
						originalValues.put(propertyName, null);
					}
					else {
						originalValues.put(propertyName, (String[])((Collection)pd.getReadMethod().invoke(this)).toArray(new String[] { }));
					}
					Collection coll = (Collection)pd.getReadMethod().invoke(this);
					for (String change : proposedChanges.get(propertyName)) {
						coll.add(change);
					}
				}
				else if (CharSequence.class.isAssignableFrom(pd.getPropertyType())) {
					originalValues.put(propertyName, new String[] { (String)pd.getReadMethod().invoke(this) });
					List<String> changes = proposedChanges.get(propertyName);
					if (!changes.isEmpty()) {
						pd.getWriteMethod().invoke(this, changes.get(0));
					}
					else {
						pd.getWriteMethod().invoke(this, null);
					} 
				}
				else {
					throw new RuntimeException(String.format("Property %s is neither a collection nor a CharSequence", propertyName));
				}
			}
			
			return originalValues;
		}
		catch (IOException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public void addOriginalValue(String propertyName, String[] value) {
		this.originalValues.put(propertyName, value);
	}
	
	public Map<String, List<String>> calculateProposedChanges(RE_RegisterItem original) {
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		
		PropertyDescriptor[] originalProperties = BeanUtils.getPropertyDescriptors(original.getClass());
		for (PropertyDescriptor originalProperty : originalProperties) {
			if (Collection.class.isAssignableFrom(originalProperty.getPropertyType())) {
				Collection originalCollection;
				Collection newCollection;
				try {
					originalCollection = (Collection)originalProperty.getReadMethod().invoke(original);

					PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(this.getClass(), originalProperty.getName());
					if (pd == null) {
						continue;
					}
				
					newCollection = (Collection)pd.getReadMethod().invoke(this);
					
					if (CollectionUtils.isEmpty(originalCollection) && CollectionUtils.isEmpty(newCollection)) continue;

					if (!CollectionUtils.isEmpty(originalCollection)) {
						if (!CharSequence.class.isAssignableFrom(CollectionUtils.findCommonElementType(originalCollection))) continue;
						
						if (!PersistentCollectionUtils.equals(originalCollection, newCollection)) {
							List<String> list = new ArrayList<String>();
							list.addAll(newCollection);
							result.put(originalProperty.getName(), list);
							continue;
						}
					}
					
					if (!CollectionUtils.isEmpty(newCollection)) {
						if (!CharSequence.class.isAssignableFrom(CollectionUtils.findCommonElementType(newCollection))) continue;

						if (!PersistentCollectionUtils.equals(newCollection, originalCollection)) {
							List<String> list = new ArrayList<String>();
							list.addAll(newCollection);
							result.put(originalProperty.getName(), list);
						}
					}
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					throw new RuntimeException(e.getMessage(), e);
				}
				
				continue;
			}
			else if (!CharSequence.class.isAssignableFrom(originalProperty.getPropertyType())) continue;

			String originalValue;
			String newValue;
			try {
				CharSequence originalValueCharSeq = (CharSequence)originalProperty.getReadMethod().invoke(original);
				if (originalValueCharSeq != null) {
					originalValue = originalValueCharSeq.toString();
				}
				else {
					originalValue = null;
				}
				
				PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(this.getClass(), originalProperty.getName());
				if (pd == null) {
					continue;
				}
			
				newValue = (String)pd.getReadMethod().invoke(this);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage(), e);
			}
			
			if (originalValue == null) {
				if (!StringUtils.isEmpty(newValue)) {
					List<String> list = new ArrayList<String>();
					list.add(newValue);
					result.put(originalProperty.getName(), list);					
				}
			}
			else {
				if (!originalValue.equals(newValue)) {
					List<String> list = new ArrayList<String>();
					list.add(newValue);
					result.put(originalProperty.getName(), list);
				}
			}
			
		}
		
		return Collections.unmodifiableMap(result);
	}
	 
	public String[] getOriginalValue(String propertyName) {
		return this.originalValues.get(propertyName);
	}
	
	/**
	 * @return the supersededItems
	 */
	public Set<UUID> getSupersededItems() {
		return Collections.unmodifiableSet(supersededItems);
	}
	
	public void addSupersededItem(UUID itemUuid) {
		supersededItems.add(itemUuid);
	}
	
	public void removeSupersededItem(UUID itemUuid) throws IllegalOperationException {
		supersededItems.remove(itemUuid);
	}

	/**
	 * @return the supersedingItems
	 */
	public Set<RegisterItemProposalDTO> getNewSupersedingItems() {
		return Collections.unmodifiableSet(newSupersedingItems);
	}

	public void addNewSupersedingItem(RegisterItemProposalDTO newItem) {
		newSupersedingItems.add(newItem);
	}

	/**
	 * @return the supersedingItems
	 */
	public Set<UUID> getExistingSupersedingItems() {
		return Collections.unmodifiableSet(existingSupersedingItems);
	}

	public void addExistingSupersedingItem(UUID existingItemUuid) {
		existingSupersedingItems.add(existingItemUuid);
	}
	
	public void removeSupersedingItem(UUID itemUuid) {
		existingSupersedingItems.remove(itemUuid);
		newSupersedingItems.remove(itemUuid);
	}

	public Set<UUID> getDependentProposals() {
		return dependentProposals;
	}

	public List<RegisterItemProposalDTO> getAggregateDependencies() {
		return Arrays.asList();
	}
	
	public List<RegisterItemProposalDTO> getCompositeDependencies() {
		return Arrays.asList();
	}
	
	protected List<RegisterItemProposalDTO> findDependentProposals(RegisterItemProposalDTO... dtos) {
		List<RegisterItemProposalDTO> dependentProposals = new ArrayList<RegisterItemProposalDTO>();
		for (RegisterItemProposalDTO dto : dtos) {
//			if (dto != null && dto.getItemUuid() != null && dto.getReferencedItemUuid() == null) {
			if (dto != null && dto.getReferencedItemUuid() == null) {
				dependentProposals.add(dto);
			}
		}
		
		return dependentProposals;
	}
	
//	public void addDependentItem(RegisterItemProposalDTO item) {
//		this.dependentItems.add(item);
//	}
//	
//	public void removeDependentItem(RegisterItemProposalDTO item) {
//		this.dependentItems.remove(item);
//	}

	public boolean isAddition() {
		return this.proposalType.equals(ProposalType.ADDITION);
	}

	public boolean isClarification() {
		return this.proposalType.equals(ProposalType.CLARIFICATION);
	}
	
	public boolean isRetirement() {
		return this.proposalType.equals(ProposalType.RETIREMENT);
	}

	public boolean isSupersession() {
		return this.proposalType.equals(ProposalType.SUPERSESSION);
	}
}
