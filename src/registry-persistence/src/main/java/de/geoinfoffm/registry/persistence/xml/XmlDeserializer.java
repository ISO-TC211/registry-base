//package de.geoinfoffm.registry.persistence.xml;
//
//import java.beans.PropertyDescriptor;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.math.BigInteger;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.xml.bind.annotation.XmlSchema;
//import javax.xml.namespace.QName;
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.XMLStreamException;
//import javax.xml.stream.events.StartElement;
//import javax.xml.transform.dom.DOMSource;
//
//import org.codehaus.stax2.XMLEventReader2;
//import org.codehaus.stax2.evt.XMLEvent2;
//import org.springframework.beans.BeanUtils;
//
//public class XmlDeserializer
//{
//	private Map<String, Package> namespaces = new HashMap<String, Package>();
//	
//	public XmlDeserializer() { 
//		for (Package p : Package.getPackages()) {
//			if (p.isAnnotationPresent(XmlSchema.class)) {
//				XmlSchema xmlSchema = p.getAnnotation(XmlSchema.class);
//				if (!namespaces.containsKey(xmlSchema.namespace())) {
//					namespaces.put(xmlSchema.namespace().toLowerCase(), p);
//				}				
//			}
//		}
//	}
//	
//	public XmlDeserializer(Class<?>... context) {
//		this();
//		
//		for (Class<?> c : context) {
//			if (c.getPackage().isAnnotationPresent(XmlSchema.class)) {
//				XmlSchema xmlSchema = c.getPackage().getAnnotation(XmlSchema.class);
//				if (!namespaces.containsKey(xmlSchema.namespace())) {
//					namespaces.put(xmlSchema.namespace().toLowerCase(), c.getPackage());
//				}
//			}
//		}
//	}
//	
//	public Object deserialize(org.w3c.dom.Element root) throws XMLStreamException {
//		XMLInputFactory ifact = XMLInputFactory.newFactory(); 
//		XMLEventReader2 reader = (XMLEventReader2)ifact.createXMLEventReader(new DOMSource(root)); 
//
//		Object result = null;
//		while (reader.hasNext()) {
//			XMLEvent2 event = (XMLEvent2)reader.next();
//			
//			if (event.isStartElement()) {
//			}
//		}
//		
//		return result;
//	}
//	
//	private Object deserializeBean(XMLEvent2 beanEvent, XMLEventReader2 reader) {
//		Object result = null;
//		
//		StartElement start = beanEvent.asStartElement();
//		QName elementName = start.getName();
//		if (!namespaces.containsKey(elementName.getNamespaceURI().toLowerCase())) {
//			throw new RuntimeException(String.format("Namespace '%s' is unknown", elementName.getNamespaceURI()));
//		}
//
//		Package p = namespaces.get(elementName.getNamespaceURI());
//		
//		Class<?> targetClass; 
//		try {
//			targetClass = this.getClass().getClassLoader().loadClass(p.getName() + "." + elementName.getLocalPart());
//			
//			result = BeanUtils.instantiate(targetClass);
//		}
//		catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
//			throw new RuntimeException(e.getMessage(), e);
//		}
//
//		XMLEvent2 event;
//		do {
//			event = (XMLEvent2)reader.next();
//			
//			if (event.isAttribute()) {
//				
//			}
//			else if (event.isEntityReference()) {
//				
//			}
//			else if (event.isStartElement()) {
//				// property
//				deserializeProperty(result, event.asStartElement(), reader);
//			}
//			else if (event.isCharacters()) {
//				
//			}
//			else if (event.isEndElement()) {
//			}
//		} while (!event.isEndElement());
//	}
//	
//	private void deserializeProperty(Object bean, StartElement propertyElement, XMLEventReader2 reader) {
//		QName propertyName = propertyElement.getName();
//		
//		PropertyDescriptor desc = BeanUtils.getPropertyDescriptor(bean.getClass(), propertyName.getLocalPart());
//		if (desc == null) {
//			throw new RuntimeException(String.format("Class '%s' has no property '%s'", bean.getClass().getCanonicalName(), propertyName.getLocalPart()));
//		}
//		
//		while (reader.hasNext()) {
//			XMLEvent2 event = (XMLEvent2)reader.next();
//			
//			if (event.isAttribute()) {
//				
//			}
//			else if (event.isEntityReference()) {
//				
//			}
//			else if (event.isStartElement()) {
//				Method setter = desc.getWriteMethod();
//				try {
//					setter.invoke(bean, subBean);
//				}
//				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//					throw new RuntimeException(e.getMessage(), e);
//				}
//
//				if (BeanUtils.isSimpleValueType(desc.getPropertyType())) {
//					XMLEvent2 propertyTypeEvent = (XMLEvent2)reader.next();
//					String value = deserializeValue(propertyTypeEvent, desc, reader);
//					
//					if (String.class.isAssignableFrom(desc.getPropertyType())) {
//						setter.invoke(bean, value);
//					}
//					else if (Integer.class.isAssignableFrom(desc.getPropertyType())) {
//						Integer i = Integer.parseInt(value);
//						setter.invoke(bean, i);
//					}
//					else if (BigInteger.class.isAssignableFrom(desc.getPropertyType())) {
//						BigInteger bi = new BigInteger(value);
//						setter.invoke(bean, bi);
//					}
//					else if (Boolean.class.isAssignableFrom(desc.getPropertyType())) {
//						Boolean b = Boolean.parseBoolean(value);
//						setter.invoke(bean, b);
//					}
//				}
//				else {
//					// new bean
//					Object subBean = deserializeBean(event, reader);
//					setter.invoke(bean, subBean);
//				}
//			}
//			else if (event.isCharacters()) {
//				
//			}
//			else if (event.isEndElement()) {
//			}
//		}
//	}
//	
//	private String deserializeValue(XMLEvent2 propertyElement, PropertyDescriptor property, XMLEventReader2 reader) {
//		XMLEvent2 valueEvent = (XMLEvent2)reader.next();
//		
//		if (valueEvent.isCharacters()) {
//			return valueEvent.asCharacters().getData();
//		}
//		else {
//			throw new RuntimeException("Not a value");
//		}
//	}
//
//}
