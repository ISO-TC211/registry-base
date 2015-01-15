package de.geoinfoffm.registry.core;

import de.geoinfoffm.registry.core.model.Proposal;

public class ProposalCreatedEvent extends ProposalRelatedEvent
{

	public ProposalCreatedEvent(Proposal proposal) {
		super(proposal);
	}

}
