package de.geoinfoffm.registry.api;

import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;

public interface ItemClassProposalService<E extends Entity, D>
{
	Addition createAdditionProposal(D proposal) throws InvalidProposalException;
}
