package de.geoinfoffm.registry.core;

import de.geoinfoffm.registry.core.model.Proposal;

public class ProposalSavedEvent extends ProposalRelatedEvent
{

	public ProposalSavedEvent(Proposal proposal) {
		super(proposal);
	}

}
