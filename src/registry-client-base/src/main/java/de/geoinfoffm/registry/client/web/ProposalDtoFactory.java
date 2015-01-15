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
package de.geoinfoffm.registry.client.web;

import java.lang.reflect.Constructor;

import org.springframework.beans.BeanUtils;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.core.ItemClassConfiguration;
import de.geoinfoffm.registry.core.ItemClassRegistry;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;

public class ProposalDtoFactory
{
	private ItemClassRegistry itemClassRegistry;

	private ProposalDtoFactory() {}
	
	public ProposalDtoFactory(ItemClassRegistry registry) {
		this.itemClassRegistry = registry;
	}
	
	public RegisterItemProposalDTO getProposalDto(RE_ItemClass itemClass) {
		RegisterItemProposalDTO result = (RegisterItemProposalDTO)BeanUtils.instantiateClass(findConstructor(itemClass));
		result.setItemClassUuid(itemClass.getUuid());
		
		return result;
	}
	
	public RegisterItemProposalDTO getProposalDto(Proposal proposal) {
		if (proposal instanceof SimpleProposal) {
			return getProposalDto((SimpleProposal)proposal);
		}
		else if (proposal instanceof Supersession) {
			return getProposalDto((Supersession)proposal);
		}
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
				defaultConstructor = RegisterItemProposalDTO.class.getConstructor(argumentType);
			}
		}
		catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		if (config == null) {
			return defaultConstructor;
		}

		String proposalDtoClassName = config.getDtoClass();
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
				ctor = proposalDtoClass.getConstructor(argumentType);
			}
		}
		catch (NoSuchMethodException ex) {
			throw new RuntimeException(String.format("The configured class is missing the constructor %s(%s)", proposalDtoClass.getCanonicalName(), argumentType.getCanonicalName()));
		}
		
		return ctor;
	}
}
