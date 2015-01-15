/**
 * 
 */
package de.geoinfoffm.registry.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

/**
 * @author Florian.Esser
 *
 */
@Access(AccessType.FIELD)
@DiscriminatorValue("SUPERSESSION")
@Audited @javax.persistence.Entity
public class Supersession extends ProposalGroup
{
	@ManyToMany
	@JoinTable(name = "Supersession_SupersedingItems", joinColumns = @JoinColumn(name = "supersessionId"))
	private List<RE_RegisterItem> supersedingItems;
	
	@OneToOne
	private RE_Register targetRegister;
	
	@NotNull
	@Column(columnDefinition = "text")
	private String justification;
	
	@Column(columnDefinition = "text")
	private String registerManagerNotes;
	
	@Column(columnDefinition = "text")
	private String controlBodyNotes;

	protected Supersession() {
		super(new SupersessionNameBuildingStrategy());
	}

	protected Supersession(RE_Register targetRegister, String justification) {
		super(new SupersessionNameBuildingStrategy());
		this.targetRegister = targetRegister;
		this.justification = justification;
	}

	protected Supersession(RE_Register targetRegister, String justification, NameBuildingStrategy strategy) {
		super(strategy);
		this.targetRegister = targetRegister;
		this.justification = justification;
	}
	
	protected Supersession(RE_Register targetRegister, String justification, Collection<Proposal> proposals) {
		this(targetRegister, proposals, justification, new SupersessionNameBuildingStrategy());
	}

	protected Supersession(RE_Register targetRegister, Collection<Proposal> proposals, String justification, NameBuildingStrategy strategy) { 
		super(proposals, strategy);
		this.targetRegister = targetRegister;
		this.justification = justification;
	}
	
	public Supersession(RE_Register targetRegister, Collection<SimpleProposal> simpleProposals,
			Collection<RE_RegisterItem> existingSupersedingItems, String justification) {

		super(simpleProposals, new SupersessionNameBuildingStrategy());
		
		this.targetRegister = targetRegister;
		this.justification = justification;
		this.supersedingItems = new ArrayList<RE_RegisterItem>();
		this.supersedingItems.addAll(existingSupersedingItems);
	}
	
	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public String getRegisterManagerNotes() {
		return registerManagerNotes;
	}

	public void setRegisterManagerNotes(String registerManagerNotes) {
		this.registerManagerNotes = registerManagerNotes;
	}

	public String getControlBodyNotes() {
		return controlBodyNotes;
	}

	public void setControlBodyNotes(String controlBodyNotes) {
		this.controlBodyNotes = controlBodyNotes;
	}

	@Override
	public void addProposal(Proposal proposal) throws IllegalOperationException {
		super.addProposal(proposal);
		
		if (proposal instanceof Addition) {
			this.supersedingItems.add(((Addition)proposal).getItem());
		}
	}

	/**
	 * @return the supersededItems
	 */
	public List<RE_RegisterItem> getSupersededItems() {
		List<RE_RegisterItem> result = new ArrayList<RE_RegisterItem>();
		for (RE_ProposalManagementInformation pmi : this.getProposalManagementInformations()) {
			Object o = Entity.unproxify(pmi);
			if (o instanceof RE_AmendmentInformation) { 
				result.add(((RE_AmendmentInformation)o).getItem());
			}
		}
		
		return result;
	}

	public void removeSupersededItem(RE_RegisterItem item) {
		for (Proposal proposal : this.getProposals()) {
			if (proposal instanceof SupersessionPart) {
				SupersessionPart part = (SupersessionPart)proposal;
				if (part.getItem().equals(item)) {
					this.removeProposal(part);
					return;
				}
			}
		}
	}

	public void removeSupersededItems(Collection<RE_RegisterItem> items) {
		for (RE_RegisterItem item : items) {
			this.removeSupersededItem(item);
		}
	}

	public void removeSupersedingItem(RE_RegisterItem item) {
		this.supersedingItems.remove(item);
		for (Proposal proposal : this.getProposals()) {
			if (proposal instanceof Addition) {
				Addition addition = (Addition)proposal;
				if (addition.getItem().equals(item)) {
					this.removeProposal(addition);
					return;
				}
			}
		}
	}

	public void removeSupersedingItems(Collection<RE_RegisterItem> items) {
		for (RE_RegisterItem item : items) {
			this.removeSupersedingItem(item);
		}
	}

	/**
	 * @return the supersedingItems
	 */
	public List<RE_RegisterItem> getSupersedingItems() {
		return Collections.unmodifiableList(supersedingItems);
	}

	/**
	 * @param supersedingItems the supersedingItems to set
	 */
	protected void setSupersedingItems(List<RE_RegisterItem> supersedingItems) {
		this.supersedingItems = supersedingItems;
	}
	
	public RE_Register getTargetRegister() {
		return this.targetRegister;
	}
	
	public void setTargetRegister(RE_Register targetRegister) {
		this.targetRegister = targetRegister;
	}
	
	@Override
	public List<RE_Register> getAffectedRegisters() {
		return Arrays.asList(this.targetRegister);
	}

	public static class SupersessionNameBuildingStrategy extends SimpleNameBuildingStrategy {

		/* (non-Javadoc)
		 * @see de.geoinfoffm.registry.core.model.NameBuildingStrategy#buildName(java.util.Collection)
		 */
		@Override
		public <P extends Proposal> String buildName(Collection<P> proposals) {
			StringBuilder nameBuilder = new StringBuilder();
			
			// Superseded items
			Collection<P> supersededItems = new ArrayList<P>();
			for (P proposal : proposals) {
				if (proposal instanceof SupersessionPart) {
					supersededItems.add(proposal);
				}
			}

			// Superseding items
			Collection<P> supersedingItems = new ArrayList<P>();
			for (P proposal : proposals) {
				if (proposal instanceof Addition) {
					supersedingItems.add(proposal);
				}
			}
			
			nameBuilder.append(super.buildName(supersededItems));
			nameBuilder.append(" --> ");
			nameBuilder.append(super.buildName(supersedingItems));
			
			return nameBuilder.toString();
		}
	}
	
	public List<SupersessionPart> getSupersessionParts() {
		List<SupersessionPart> result = new ArrayList<SupersessionPart>();
		for (Proposal p : this.getProposals()) {
			if (p instanceof SupersessionPart) {
				result.add((SupersessionPart)p);
			}
		}
		
		return result;
	}
	
	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public void accept() throws IllegalOperationException {
		super.accept();

		for (RE_RegisterItem supersededItem : this.getSupersededItems()) {
			supersededItem.addSuccessors(this.getSupersedingItems());
		}
	}

	@Override
	public void accept(String controlBodyDecisionEvent) throws IllegalOperationException {
		super.accept(controlBodyDecisionEvent);

		for (RE_RegisterItem supersededItem : this.getSupersededItems()) {
			supersededItem.addSuccessors(this.getSupersedingItems());
		}
	}
}
