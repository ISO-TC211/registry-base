package de.geoinfoffm.registry.persistence.xml;

import de.geoinfoffm.registry.persistence.Serializer;
import de.geoinfoffm.registry.persistence.xml.exceptions.XmlSerializationException;

public interface XmlSerializer<O> extends Serializer<XmlSerializable, O> 
{
	public void serialize(XmlSerializable input, O output) throws XmlSerializationException;
}
