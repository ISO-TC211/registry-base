package de.geoinfoffm.registry.core.model;

import de.geoinfoffm.registry.core.EntityRelatedEvent;

public class DelegationRequestedEvent extends EntityRelatedEvent<Delegation> {

	public DelegationRequestedEvent(Delegation entity) {
		super(entity);
	}

}
