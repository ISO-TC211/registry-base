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

import java.lang.reflect.Constructor;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import de.geoinfoffm.registry.api.soap.AbstractProposal_Type;
import de.geoinfoffm.registry.api.soap.AbstractRegisterItemProposal_Type;
import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.core.ItemClassConfiguration;
import de.geoinfoffm.registry.core.ItemClassRegistry;
//import de.geoinfoffm.registry.core.model.HierarchicalProposal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;

/**
 * A factory for creating ProposalDto objects.
 *
 * @author Florian Esser
 */
@Component
public class ProposalDtoFactory
{
	private static final Logger logger = LoggerFactory.getLogger(ProposalDtoFactory.class);
	
	private ItemClassRegistry itemClassRegistry;
	private EntityManager entityManager;

	private ProposalDtoFactory() {}
	
	public ProposalDtoFactory(ItemClassRegistry registry, EntityManager entityManager) {
		this.itemClassRegistry = registry;
		this.entityManager = entityManager;
	}
	
	public RegisterItemProposalDTO getProposalDto(RE_ItemClass itemClass) {
		RegisterItemProposalDTO result = (RegisterItemProposalDTO)BeanUtils.instantiateClass(findConstructor(itemClass));
		result.setItemClassUuid(itemClass.getUuid());
		
		return result;
	}

	public RegisterItemProposalDTO getProposalDto(AbstractProposal_Type proposal) {
		RegisterItemProposalDTO result = null;
		if (proposal instanceof Addition_Type) {
			result = this.getProposalDto(((Addition_Type)proposal).getItemDetails().getAbstractRegisterItemProposal().getValue());
			
			result.setJustification(proposal.getJustification());
			result.setRegisterManagerNotes(proposal.getRegisterManagerNotes());
			result.setControlBodyNotes(proposal.getControlBodyNotes());
			result.setSponsorUuid(UUID.fromString(proposal.getSponsor().getUuidref()));
			result.setTargetRegisterUuid(UUID.fromString(((Addition_Type)proposal).getTargetRegisterUuid()));
		}
		else {
			throw new RuntimeException("Not yet implemented for type " + proposal.getClass().getCanonicalName());
		}
		
		return result;
	}
	
	public RegisterItemProposalDTO getProposalDto(AbstractRegisterItemProposal_Type proposal) {
		String itemClassUuid = proposal.getItemClassUuid();
		RE_ItemClass itemClass = entityManager.find(RE_ItemClass.class, UUID.fromString(itemClassUuid));
		return (RegisterItemProposalDTO)BeanUtils.instantiateClass(findConstructor(itemClass, proposal.getClass()), proposal);		
	}

	public RegisterItemProposalDTO getProposalDto(Proposal proposal) {
		if (proposal instanceof SimpleProposal) {
			return getProposalDto((SimpleProposal)proposal);
		}
		else if (proposal instanceof Supersession) {
			return getProposalDto((Supersession)proposal);
		}
//		else if (proposal instanceof HierarchicalProposal) {
//			HierarchicalProposal group = (HierarchicalProposal)proposal;
//			if (group.getPrimaryProposal() != null) {
//				return getProposalDto(group.getPrimaryProposal());
//			}
//			else {
//				throw new RuntimeException("Missing primary proposal");
//			}
//		}
		else if (proposal instanceof ProposalGroup) {
			throw new RuntimeException("Not yet implemented");
		}
		else {
			throw new RuntimeException("Not yet implemented");
		}
	}

	public RegisterItemProposalDTO getProposalDto(SimpleProposal proposal) {
		RE_ItemClass itemClass = proposal.getItem().getItemClass();
		return (RegisterItemProposalDTO)BeanUtils.instantiateClass(findConstructor(itemClass, Proposal.class), proposal);		
	}
	
	public RegisterItemProposalDTO getProposalDto(Supersession supersession) {
		return new RegisterItemProposalDTO(supersession);
	}

	private Constructor<?> findConstructor(RE_ItemClass itemClass) {
		return findConstructor(itemClass, null);
	}

	private Constructor<?> findConstructor(RE_ItemClass itemClass, Class<?> argumentType) {
		ItemClassConfiguration config = itemClassRegistry.getConfiguration(itemClass.getName());
		
		Constructor<RegisterItemProposalDTO> defaultConstructor;
		try {
			if (argumentType == null || Void.class.equals(argumentType)) {
				defaultConstructor = RegisterItemProposalDTO.class.getConstructor();
			}
			else {
				defaultConstructor = ConstructorUtils.getMatchingAccessibleConstructor(RegisterItemProposalDTO.class, argumentType);
			}
		}
		catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		if (config == null) {
			logger.debug("Item class registry contains no configuration for item class {} (UUID: {})", itemClass.getName(), itemClass.getUuid());
			return defaultConstructor;
		}

		String proposalDtoClassName = config.getDtoClass();
		logger.debug("Item class configuration define class {} as DTO for item class {}", proposalDtoClassName, itemClass.getName());
		Class<?> proposalDtoClass;		
		try {
			proposalDtoClass = this.getClass().getClassLoader().loadClass(proposalDtoClassName);
			if (!RegisterItemProposalDTO.class.isAssignableFrom(proposalDtoClass)) {
				throw new RuntimeException(String.format("The configured class %s is not derived from RegisterItemProposalDTO", proposalDtoClassName));
			}
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		Constructor<?> ctor;
		try {
			if (argumentType == null || argumentType.equals(Void.class)) {
				ctor = proposalDtoClass.getDeclaredConstructor();
			}
			else {
				ctor = ConstructorUtils.getMatchingAccessibleConstructor(proposalDtoClass, argumentType);
			}
		}
		catch (NoSuchMethodException ex) {
			throw new RuntimeException(String.format("The configured class is missing the constructor %s(%s)", proposalDtoClass.getCanonicalName(), argumentType.getCanonicalName()));
		}
		
		if (ctor == null) {
			throw new RuntimeException(String.format("Proposal DTO class %s does not provide ctor for argument type %s", proposalDtoClassName, argumentType.getCanonicalName()));
		}
		
		return ctor;
	}
}
