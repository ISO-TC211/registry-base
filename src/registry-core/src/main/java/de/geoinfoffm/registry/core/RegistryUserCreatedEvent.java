package de.geoinfoffm.registry.core;

import de.geoinfoffm.registry.core.model.RegistryUser;

public class RegistryUserCreatedEvent extends EntityRelatedEvent<RegistryUser>
{
	public RegistryUserCreatedEvent(RegistryUser entity) {
		super(entity);
	}
}
