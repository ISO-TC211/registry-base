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
package de.geoinfoffm.registry.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Disposition;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

/**
 * @author Florian Esser
 *
 */
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.JOINED)
@Audited @javax.persistence.Entity
public class ProposalGroup extends Proposal
{
	@Transient
	private NameBuildingStrategy nameBuildingStrategy;
	
	protected ProposalGroup() {
		this.nameBuildingStrategy = new SimpleNameBuildingStrategy();
	}

	protected ProposalGroup(String title) {
		super(title);
	}

	protected ProposalGroup(NameBuildingStrategy strategy) {
		this.nameBuildingStrategy = strategy;
	}
	
	public <P extends Proposal> ProposalGroup(Collection<P> proposals) {
		this(proposals, new SimpleNameBuildingStrategy());
	}

	public <P extends Proposal> ProposalGroup(Collection<P> proposals, NameBuildingStrategy strategy) {
		this(strategy);
		
		for (P proposal : proposals) {
			proposal.setParent(this);
			this.getProposals().add(proposal);
		}
		
		this.setTitle(buildName());		
	}

	public <P extends Proposal> ProposalGroup(Collection<P> proposals, String name) {
		this();
		
		this.setProposals(new ArrayList<Proposal>());
		for (Proposal proposal : proposals) {
			this.getProposals().add(proposal);
			proposal.setParent(this);
		}
		
		this.setTitle(name);
	}

	/**
	 * @return the proposals
	 */
	public List<Proposal> getProposals() {
		return this.getDependentProposals();
	}

	/**
	 * @param proposals the proposals to set
	 */
	protected void setProposals(List<Proposal> proposals) {
		this.getDependentProposals().clear();
		for (Proposal proposal : proposals) {
			this.getDependentProposals().add(proposal);
			proposal.setParent(this);
		}
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#getProposalManagementInformations()
	 */
	@Override
	public List<RE_ProposalManagementInformation> getProposalManagementInformations() {
		List<RE_ProposalManagementInformation> result = new ArrayList<RE_ProposalManagementInformation>();
		for (Proposal proposal : getProposals()) {
			result.addAll(proposal.getProposalManagementInformations());
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#getDisposition()
	 */
	@Override
	public RE_Disposition getDisposition() {
		return getProposals().get(0).getDisposition();
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#getStatus()
	 */
	@Override
	public RE_DecisionStatus getDecisionStatus() {
		return getProposals().get(0).getDecisionStatus();
	}

	public void addProposal(Proposal proposal) throws IllegalOperationException {
		if (proposal.hasParent() && !proposal.getParent().getUuid().equals(this.getUuid())) {
			throw new IllegalOperationException(String.format("Proposal %s already belongs to group %s", proposal.getUuid(), proposal.getParent().getUuid()));
		}
		this.getProposals().add(proposal);
		proposal.setParent(this);
	}
	
	public void removeProposal(Proposal proposal) {
		if (this.getProposals() == null) {
			return;
		}
		
		this.getProposals().remove(proposal);
		proposal.setParent(null);
	}
	
	public void removeProposals(Collection<Proposal> proposals) {
		if (this.getProposals() == null) {
			return;
		}
		
		this.getProposals().removeAll(proposals);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#isInRegister(de.geoinfoffm.registry.core.model.iso19135.RE_Register)
	 */
	@Override
	public boolean isContainedIn(RE_Register register) {
		if (this.getProposals() == null || this.getProposals().isEmpty()) {
			return false;
		}

		return getProposals().get(0).isContainedIn(register);
	}
	
	@Override
	public Set<RE_Register> getAffectedRegisters() {
		Set<RE_Register> result = new HashSet<RE_Register>();
		for (Proposal proposal : this.getProposals()) {
			result.addAll(proposal.getAffectedRegisters());
		}
		
		return result;
	}
	
	public String buildName() {
		return nameBuildingStrategy.buildName(this.getProposals());
	}
	
	public static class SimpleNameBuildingStrategy implements NameBuildingStrategy {

		/* (non-Javadoc)
		 * @see de.geoinfoffm.registry.core.model.NameBuildingStrategy#buildName(java.util.Collection)
		 */
		@Override
		public <P extends Proposal> String buildName(Collection<P> proposals) {
			return buildName(proposals, new StringBuilder()).toString();
		}

		protected <P extends Proposal> StringBuilder buildName(Collection<P> proposals, StringBuilder nameBuilder) {
			for (P proposal : proposals) {
				buildName(proposal, nameBuilder);
			}
		
			return nameBuilder;
		}
		
		protected <P extends Proposal> StringBuilder buildName(P proposal, StringBuilder nameBuilder) {
			if (proposal instanceof SimpleProposal) {
				if (nameBuilder.length() > 0) {
					nameBuilder.append(", ");
				}
				nameBuilder.append(((SimpleProposal)proposal).getItem().getName());
			}
			else if (proposal instanceof ProposalGroup) {
				nameBuilder.append(buildName(((ProposalGroup)proposal).getProposals(), nameBuilder));
			}
			
			return nameBuilder;
		}

	}

	@Override
	public void delete() throws IllegalOperationException {
		for (Proposal proposal : this.getProposals()) {
			proposal.delete();
		}
	}
}
