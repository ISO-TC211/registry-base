//package de.geoinfoffm.registry.core.model;
//
//import javax.persistence.Column;
//import javax.persistence.MappedSuperclass;
//
//import de.geoinfoffm.registry.core.xml.AbstractXmlSerializable;
//import de.geoinfoffm.registry.core.xml.exceptions.XmlSerializationException;
//
///**
// * Abstract base class for embeddable value objects.
// * 
// * @author Florian Esser
// * 
// */
//@MappedSuperclass
//public abstract class PersistableValueObject<T extends PersistableValueObject<T>> extends AbstractXmlSerializable
//{
//	private static final long serialVersionUID = 6479079768860546255L;
//
//	@Column(name = "value")
//	protected String getXmlValue() throws XmlSerializationException {
//		return serialize(this);
//	}
//	
//
//	protected void setXmlValue(String xmlValue) {
//		Object object = RegistryObjectFactory.getInstance().createObject(xmlValue);
//		if (this.getClass().isAssignableFrom(object.getClass())) {
//			fill((T)object);
//		}
//	}
//
//	protected abstract void fill(T template);
//}
