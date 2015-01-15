/**
 * 
 */
package de.geoinfoffm.registry.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Disposition;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

/**
 * @author Florian.Esser
 *
 */
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.JOINED)
@Audited @javax.persistence.Entity
public class ProposalGroup extends Proposal
{
	@OneToMany(mappedBy = "group", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
//	@JoinTable(name = "ProposalGroup_Proposals", joinColumns = @JoinColumn(name = "groupId"), inverseJoinColumns = @JoinColumn(name = "proposalId"))
	private final List<Proposal> proposals = new ArrayList<Proposal>();
	
	@Column(columnDefinition = "text")
	private String name;
	
	@Transient
	private NameBuildingStrategy nameBuildingStrategy;
	
	protected ProposalGroup() {
		this.nameBuildingStrategy = new SimpleNameBuildingStrategy();
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
			proposal.setGroup(this);
			this.proposals.add(proposal);
		}
		
		this.name = buildName();		
	}

	public <P extends Proposal> ProposalGroup(Collection<P> proposals, String name) {
		this();
		
		this.setProposals(new ArrayList<Proposal>());
		this.proposals.addAll(proposals);
		
		this.name = name;
	}

	@Override
	public void submit(Date submissionDate) throws IllegalOperationException {
		super.submit(submissionDate);
		
		for (Proposal proposal : this.proposals) {
			proposal.submit(submissionDate);
		}
	}

	/**
	 * @return the proposals
	 */
	public List<Proposal> getProposals() {
		return Collections.unmodifiableList(proposals);
	}

	/**
	 * @param proposals the proposals to set
	 */
	protected void setProposals(List<Proposal> proposals) {
		this.proposals.clear();
		for (Proposal proposal : proposals) {
			this.proposals.add(proposal);
			proposal.setGroup(this);
		}
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
	 * @see de.geoinfoffm.registry.core.model.Proposal#getSponsor()
	 */
	@Override
	public RE_SubmittingOrganization getSponsor() {
		return getProposals().get(0).getSponsor();
	}

	@Override
	public void setSponsor(RE_SubmittingOrganization sponsor) {
		this.sponsor = sponsor;
		for (Proposal proposal : this.proposals) {
			proposal.setSponsor(sponsor);
		}
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#getStatus()
	 */
	@Override
	public RE_DecisionStatus getDecisionStatus() {
		return getProposals().get(0).getDecisionStatus();
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#review(java.util.Date)
	 */
	@Override
	public void review(Date reviewDate) throws IllegalOperationException {
		this.setStatus(STATUS_IN_APPROVAL_PROCESS);
		for (Proposal proposal : getProposals()) {
			proposal.review(reviewDate);
		}
	}	

	@Override
	public void accept() throws IllegalOperationException {
		this.setConcluded(true);
		this.setStatus(STATUS_FINISHED);
		for (Proposal proposal : getProposals()) {
			proposal.accept();
		}
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#accept()
	 */
	@Override
	public void accept(String controlBodyDecisionEvent) throws IllegalOperationException {
		for (Proposal proposal : getProposals()) {
			proposal.accept(controlBodyDecisionEvent);
		}
	}


	@Override
	public void reject() throws IllegalOperationException {
		this.setStatus(STATUS_APPEALABLE);
		for (Proposal proposal : getProposals()) {
			proposal.reject();
		}
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#reject()
	 */
	@Override
	public void reject(String controlBodyDecisionEvent) throws IllegalOperationException {
		this.setStatus(STATUS_APPEALABLE);
		for (Proposal proposal : getProposals()) {
			proposal.reject(controlBodyDecisionEvent);
		}
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#withdraw()
	 */
	@Override
	public void withdraw() throws IllegalOperationException {
		this.setStatus(STATUS_WITHDRAWN);
		for (Proposal proposal : getProposals()) {
			proposal.withdraw();
		}
	}
	
	@Override
	public void delete() throws IllegalOperationException {
		for (Proposal proposal : getProposals()) {
			proposal.delete();
		}
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#conclude()
	 */
	@Override
	public void conclude() throws IllegalOperationException {
		for (Proposal proposal : getProposals()) {
			proposal.conclude();
		}
		
		this.setStatus(STATUS_FINISHED);
		this.setConcluded(true);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#appeal()
	 */
	@Override
	public Appeal appeal(String justification, String impact, String situation) throws IllegalOperationException {
		this.setStatus(STATUS_APPEALED);		
		return new Appeal(this, justification, situation, impact);
	}
	
	public void addProposal(Proposal proposal) throws IllegalOperationException {
		if (proposal.getGroup() != null && !proposal.getGroup().getUuid().equals(this.getUuid())) {
			throw new IllegalOperationException(String.format("Proposal %s already belongs to group %s", proposal.getUuid(), proposal.getGroup().getUuid()));
		}
		this.proposals.add(proposal);
		proposal.setGroup(this);
	}
	
	public void removeProposal(Proposal proposal) {
		if (this.proposals == null) {
			return;
		}
		
		this.proposals.remove(proposal);
		proposal.setGroup(null);
	}
	
	public void removeProposals(Collection<Proposal> proposals) {
		if (this.proposals == null) {
			return;
		}
		
		this.proposals.removeAll(proposals);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#isReviewed()
	 */
	@Override
	public boolean isReviewed() {
		if (this.proposals == null || this.proposals.isEmpty()) {
			return false;
		}
		
		return getProposals().get(0).isReviewed();
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#isInRegister(de.geoinfoffm.registry.core.model.iso19135.RE_Register)
	 */
	@Override
	public boolean isContainedIn(RE_Register register) {
		if (this.proposals == null || this.proposals.isEmpty()) {
			return false;
		}

		return getProposals().get(0).isContainedIn(register);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#isUnderReview()
	 */
	@Override
	public boolean isUnderReview() {
		if (this.proposals == null || this.proposals.isEmpty()) {
			return false;
		}

		return getProposals().get(0).isUnderReview();
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#isPending()
	 */
	@Override
	public boolean isPending() {
		if (this.proposals == null || this.proposals.isEmpty()) {
			return false;
		}

		return getProposals().get(0).isPending();
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#isTentative()
	 */
	@Override
	public boolean isTentative() {
		if (this.proposals == null || this.proposals.isEmpty()) {
			return false;
		}

		return getProposals().get(0).isTentative();
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal#isFinal()
	 */
	@Override
	public boolean isFinal() {
		if (this.proposals == null || this.proposals.isEmpty()) {
			return false;
		}

		return getProposals().get(0).isFinal();
	}
	
	@Override
	public List<RE_Register> getAffectedRegisters() {
		List<RE_Register> result = new ArrayList<RE_Register>();
		for (Proposal proposal : this.getProposals()) {
			result.addAll(proposal.getAffectedRegisters());
		}
		
		return result;
	}
	
	public String buildName() {
		return nameBuildingStrategy.buildName(this.proposals);
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
}
