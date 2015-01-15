package de.geoinfoffm.registry.core;

import de.geoinfoffm.registry.core.model.Proposal;

public class ProposalSubmittedEvent extends ProposalRelatedEvent
{
	public ProposalSubmittedEvent(Proposal proposal) {
		super(proposal);
	}

}
