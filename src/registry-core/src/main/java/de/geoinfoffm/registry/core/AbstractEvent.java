package de.geoinfoffm.registry.core;

import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.springframework.context.ApplicationEvent;

public class AbstractEvent extends ApplicationEvent
{
	private UUID eventId;
	
	private List<Object> annotations = new Vector<Object>();
	
	public AbstractEvent(Object source) {
		super(source);
		
		this.eventId = UUID.randomUUID();
	}
	
	public UUID getId() {
		return eventId;
	}
	
	public void annotate(Object annotation) {
		this.annotations.add(annotation);
	}
	
	public boolean isAnnotated(Object annotation) {
		return this.annotations.contains(annotation);
	}
}
