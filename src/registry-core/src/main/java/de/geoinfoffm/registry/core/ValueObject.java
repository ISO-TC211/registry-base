package de.geoinfoffm.registry.core;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Abstract base class for value objects in the domain model.<br>
 * <br>
 * Derived classes must ensure that their instances are immutable.
 * 
 * @author Florian Esser
 *
 */
public abstract class ValueObject implements Serializable
{
	@Override
	public final boolean equals(Object other) {
		if (other == null) {
			return false;
		}

		if (other == this) {
			return true;
		}

		if (!other.getClass().equals(this.getClass())) {
			return false;
		}
		
		return EqualsBuilder.reflectionEquals(this, other, false);
	}
	
	@Override
	public final int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, false);
	}
}
