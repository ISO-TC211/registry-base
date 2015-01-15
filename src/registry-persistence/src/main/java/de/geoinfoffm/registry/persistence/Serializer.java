package de.geoinfoffm.registry.persistence;

import de.geoinfoffm.registry.persistence.xml.exceptions.SerializationException;

public interface Serializer<I, O>
{
	public void serialize(I input, O output) throws SerializationException;
}
