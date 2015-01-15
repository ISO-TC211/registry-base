package de.geoinfoffm.registry.persistence.xml;

import org.eclipse.persistence.jaxb.JAXBIntrospector;
import org.eclipse.persistence.jaxb.JAXBMarshaller;
import org.eclipse.persistence.oxm.XMLMarshaller;

public class RegistryMarshaller extends JAXBMarshaller
{
	public RegistryMarshaller(XMLMarshaller newXMLMarshaller, JAXBIntrospector newIntrospector) {
		super(newXMLMarshaller, newIntrospector);
	}

}
