package de.geoinfoffm.registry.core;

import org.springframework.context.ApplicationEvent;

public class EntityRelatedEvent<E extends Entity> extends AbstractEvent
{
	
	public EntityRelatedEvent(E entity) {
		super(entity);
	}

	@Override
	public E getSource() {
		return (E)super.getSource();
	}
	
}
