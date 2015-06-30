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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.soap.AbstractRegisterItemProposal_Type;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso00639.LanguageCode;
import de.geoinfoffm.registry.core.model.iso19115.CI_OnlineResource;
import de.geoinfoffm.registry.core.model.iso19115.MD_CharacterSetCode;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Locale;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterManager;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubregisterDescription;

/**
 * The class SubregisterDescriptionProposalDTO.
 *
 * @author Florian Esser
 */
public class SubregisterDescriptionProposalDTO extends RegisterItemProposalDTO
{
	public String uniformResourceIdentifier;
	public String operatingLanguage;
	public List<UUID> containedItemClasses;
	public UUID subregisterManager;

	public SubregisterDescriptionProposalDTO() {
		// TODO Auto-generated constructor stub
	}

	public SubregisterDescriptionProposalDTO(String itemClassName) {
		super(itemClassName);
		// TODO Auto-generated constructor stub
	}

	public SubregisterDescriptionProposalDTO(RE_RegisterItem referencedItem) {
		super(referencedItem);
		// TODO Auto-generated constructor stub
	}

	public SubregisterDescriptionProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
		// TODO Auto-generated constructor stub
	}

	public SubregisterDescriptionProposalDTO(AbstractRegisterItemProposal_Type itemDetails) {
		super(itemDetails);
		// TODO Auto-generated constructor stub
	}

	public SubregisterDescriptionProposalDTO(Proposal proposal, ProposalDtoFactory factory) {
		super(proposal, factory);
	}

//	@Override
//	protected void initializeFromItemDetails(RegisterItemProposal_Type itemDetails) {
//		super.initializeFromItemDetails(itemDetails);
//		
//		if (itemDetails instanceof DomainNamespaceProposal_Type) {
//		}
//	}

	@Override
	public void setAdditionalValues(RE_RegisterItem item, EntityManager entityManager) {
		super.setAdditionalValues(item, entityManager);

		if (item instanceof RE_SubregisterDescription) {
			RE_SubregisterDescription subregister = (RE_SubregisterDescription)item;

			CI_OnlineResource uri = new CI_OnlineResource(this.getUniformResourceIdentifier());
			subregister.setUniformResourceIdentifier(uri);
			
			RE_Locale locale = new RE_Locale(this.getOperatingLanguage(), new LanguageCode("", this.getOperatingLanguage(), this.getOperatingLanguage()), this.getOperatingLanguage(), MD_CharacterSetCode.utf8, null);
			subregister.setOperatingLanguage(locale);
			
			for (UUID itemClassUuid : this.getContainedItemClasses()) {
				RE_ItemClass itemClass = entityManager.find(RE_ItemClass.class, itemClassUuid);
				subregister.getContainedItemClasses().add(itemClass);
			}
			
			RE_RegisterManager manager = entityManager.find(RE_RegisterManager.class, this.getSubregisterManager());
			subregister.setSubregisterManager(manager);
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem item) {
		super.loadAdditionalValues(item);
		
		if (item instanceof RE_SubregisterDescription) {
			RE_SubregisterDescription subregister = (RE_SubregisterDescription)item;
			
			this.setUniformResourceIdentifier(subregister.getUniformResourceIdentifier().getLinkage());
			this.setOperatingLanguage(subregister.getOperatingLanguage().getName());
			for (RE_ItemClass containedItemClass : subregister.getContainedItemClasses()) {
				this.getContainedItemClasses().add(containedItemClass.getUuid());
			}
		}
	}

	public String getUniformResourceIdentifier() {
		return uniformResourceIdentifier;
	}

	public void setUniformResourceIdentifier(String uniformResourceIdentifier) {
		this.uniformResourceIdentifier = uniformResourceIdentifier;
	}

	public String getOperatingLanguage() {
		return operatingLanguage;
	}

	public void setOperatingLanguage(String operatingLanguage) {
		this.operatingLanguage = operatingLanguage;
	}

	public List<UUID> getContainedItemClasses() {
		if (this.containedItemClasses == null) {
			this.containedItemClasses = new ArrayList<>();
		}
		return containedItemClasses;
	}

	public void setContainedItemClasses(List<UUID> containedItemClasses) {
		this.containedItemClasses = containedItemClasses;
	}

	public UUID getSubregisterManager() {
		return subregisterManager;
	}

	public void setSubregisterManager(UUID subregisterManager) {
		this.subregisterManager = subregisterManager;
	}	

}
