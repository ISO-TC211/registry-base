package de.geoinfoffm.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

/**
 * 
 * @author Florian Esser
 *
 */
@Access(AccessType.FIELD)
@Audited @Entity
public class ProposalRelatedRole extends Role
{
	private static final long serialVersionUID = -42056590196592155L;
	
	@ManyToOne(optional = false)
	private Proposal proposal;

	protected ProposalRelatedRole() {
	}

	public ProposalRelatedRole(String rolePrefix, Proposal proposal) {
		super(rolePrefix + proposal.getUuid().toString());
		this.proposal = proposal;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

}
