//package de.geoinfoffm.registry.core.model;
//
//import java.io.StringReader;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;
//import javax.xml.namespace.QName;
//import javax.xml.stream.XMLStreamException;
//
//import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
//import de.geoinfoffm.registry.core.model.iso19115.CI_Contact;
//
///**
// * Factory class to (re)create registry objects from their XML representation.
// * 
// * TODO: Implement plugin mechanism such that future extension may register
// * methods for object creation.
// * 
// * @author Florian Esser
// *
// */
//public class RegistryObjectFactory
//{
//	private static RegistryObjectFactory instance;
//	
//	private final Map<QName, Class<? extends XmlSerializable>> creators = Collections.synchronizedMap(new HashMap<QName, Class<? extends XmlSerializable>>()); 
//	
//	protected RegistryObjectFactory() { }
//	
//	public static RegistryObjectFactory getInstance() {
//		if (instance == null) {
//			instance = new RegistryObjectFactory();
//		}
//		
//		return instance;
//	}
//	
//	/**
//	 * Creates a registry object from its XML representation.
//	 * 
//	 * @param xml XML fragment representing the object.
//	 * @return Registry object.
//	 * @throws XMLStreamException 
//	 */
//	public XmlSerializable createObject(String xml) {
//		try {
//			JAXBContext jContext = JAXBContext.newInstance(CI_Contact.class, CharacterString.class);
//			Unmarshaller unmarshaller = jContext.createUnmarshaller();
//			Object object = unmarshaller.unmarshal(new StringReader(xml));
//			if (object instanceof XmlSerializable) {
//				return (XmlSerializable)object;
//			}
//		}
//		catch (JAXBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return null;
////		StringReader xmlReader = new StringReader(xml);
////		XMLInputFactory ifact = XMLInputFactory2.newInstance();
////		XMLStreamReader2 sr = (XMLStreamReader2)ifact.createXMLStreamReader(xmlReader);
////		QName rootQname = sr.getElementAsQName();
////		
////		if (creators.containsKey(rootQname)) {
////			Class<? extends XmlSerializable> creator = creators.get(rootQname);
////			try {
////				Constructor<? extends XmlSerializable> con = creator.getDeclaredConstructor();
////				XmlSerializable object = con.newInstance();
////				object.fromXml(xml);
////				return object;
////			}
////			catch (NoSuchMethodException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			catch (SecurityException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			catch (InstantiationException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			catch (IllegalAccessException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			catch (IllegalArgumentException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			catch (InvocationTargetException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////		}
////		
////		return null;
//	}
//	
//	public void registerClass(QName qname, Class<? extends XmlSerializable> theClass) {
//		creators.put(qname, theClass);
//	}
//}
