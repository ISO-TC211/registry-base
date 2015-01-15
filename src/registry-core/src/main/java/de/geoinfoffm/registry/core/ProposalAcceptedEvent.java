package de.geoinfoffm.registry.core;

import de.geoinfoffm.registry.core.model.Proposal;

public class ProposalAcceptedEvent extends ProposalRelatedEvent
{
	public ProposalAcceptedEvent(Proposal proposal) {
		super(proposal);
	}
	
}
