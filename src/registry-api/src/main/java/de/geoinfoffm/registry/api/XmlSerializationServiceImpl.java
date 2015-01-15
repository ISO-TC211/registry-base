//package de.geoinfoffm.registry.api;
//
//import java.io.Writer;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import de.geoinfoffm.registry.core.AbstractEntity;
//import de.geoinfoffm.registry.core.Organization;
//import de.geoinfoffm.registry.persistence.mapper.Mapper;
//import de.geoinfoffm.registry.persistence.mapper.OrganizationMapper;
//import de.geoinfoffm.registry.persistence.xml.AbstractXmlSerializable;
//import de.geoinfoffm.registry.persistence.xml.JaxbSerializationStrategy;
//import de.geoinfoffm.registry.persistence.xml.StaxSerializationStrategy;
//import de.geoinfoffm.registry.persistence.xml.StaxXmlSerializer;
//import de.geoinfoffm.registry.persistence.xml.XmlSerializable;
//import de.geoinfoffm.registry.persistence.xml.XmlSerializationStrategy;
//import de.geoinfoffm.registry.persistence.xml.XmlSerializer;
//import de.geoinfoffm.registry.persistence.xml.exceptions.XmlSerializationException;
//
//public class XmlSerializationServiceImpl implements XmlSerializationService {
//	
//	private XmlSerializationStrategy strategy;
//	
//	public XmlSerializationServiceImpl() {
//		this(new StaxSerializationStrategy(true));
//	}
//	
//	public XmlSerializationServiceImpl(XmlSerializationStrategy strategy) {
//		this.strategy = strategy;
//	}
//
//	public void serialize(XmlSerializable entity, Writer target) throws XmlSerializationException {
//		// TODO: Apply strategy
//		AbstractXmlSerializable.serialize(entity, target);
//	}
//
//}
