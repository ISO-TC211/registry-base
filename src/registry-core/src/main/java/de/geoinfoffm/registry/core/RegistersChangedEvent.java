package de.geoinfoffm.registry.core;

/**
 * Notifies the event listeners that the list of registers (may) have changed.
 * 
 * @author Florian Esser
 *
 */
public class RegistersChangedEvent extends AbstractEvent
{
	public RegistersChangedEvent(Object source) {
		super(source);
	}
}
