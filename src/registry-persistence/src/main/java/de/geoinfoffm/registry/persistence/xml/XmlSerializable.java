package de.geoinfoffm.registry.persistence.xml;

import java.io.Serializable;
import java.util.Collection;

import javax.xml.namespace.QName;

import de.geoinfoffm.registry.persistence.xml.exceptions.XmlSerializationException;

/**
 * 
 * @author Florian Esser
 *
 */
public interface XmlSerializable extends Serializable
{
	public QName getQName();
	public Serializable getValue() throws XmlSerializationException;
	public Collection<Attribute> getXmlAttributes();
//	public String toXml(NamespacePrefixFactory prefixFactory) throws XmlSerializationException;
//	public void fromXml(String xml);
}
