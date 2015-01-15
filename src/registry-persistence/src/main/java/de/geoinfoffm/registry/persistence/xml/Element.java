package de.geoinfoffm.registry.persistence.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.xml.namespace.QName;

/**
 * This class represents an XML element.
 * 
 * @author Florian Esser
 *
 */
public class Element implements XmlSerializable
{
	private static final long serialVersionUID = 2483694198250824434L;

	private QName parentQname;
	private String elementName;
	private Collection<Attribute> attributes = new ArrayList<Attribute>();
	private XmlSerializable xmlValue;

	public Element(String elementName, QName parentQname, Collection<Attribute> attributes, XmlSerializable xmlValue) {
		this.elementName = elementName;
		this.parentQname = parentQname;
		this.attributes.addAll(attributes);
		this.xmlValue = xmlValue;
	}
	
	@Override
	public String toString() {
		return elementName + "//" + xmlValue.toString();
	}

	@Override
	public QName getQName() {
		return new QName(parentQname.getNamespaceURI(), elementName, parentQname.getPrefix());
	}

	@Override
	public XmlSerializable getValue() {
		return xmlValue;
	}

	@Override
	public Collection<Attribute> getXmlAttributes() {
		return Collections.unmodifiableCollection(attributes);
	}

}
