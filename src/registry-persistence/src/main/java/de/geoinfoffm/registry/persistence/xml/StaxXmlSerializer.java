package de.geoinfoffm.registry.persistence.xml;

import java.beans.PropertyDescriptor;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElement.DEFAULT;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Result;

import org.codehaus.stax2.XMLStreamWriter2;
import org.dom4j.Namespace;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeanUtils;

import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.XmlAccessor;
import de.geoinfoffm.registry.persistence.Serializer;
import de.geoinfoffm.registry.persistence.xml.exceptions.XmlSerializationException;

public class StaxXmlSerializer<O> implements Serializer<Object, O> {
	
	private Session session;
	
	public static final class EntityContainer implements Serializable {
		private Object root;
		private SessionFactory sessionFactory;
		
		public EntityContainer(Object root, SessionFactory sessionFactory) {
			this.setRoot(root);
			this.setSessionFactory(sessionFactory);
		}

		public Object getRoot() {
			return root;
		}

		public void setRoot(Object root) {
			this.root = root;
		}

		public SessionFactory getSessionFactory() {
			return sessionFactory;
		}

		public void setSessionFactory(SessionFactory sessionFactory) {
			this.sessionFactory = sessionFactory;
		}
	}
	
	private StaxSerializationStrategy strategy;
	private NamespacePrefixFactory prefixFactory;
	
	public StaxXmlSerializer(StaxSerializationStrategy strategy) {
		this.strategy = strategy;
		
		this.prefixFactory = new NamespacePrefixFactoryImpl();
	}
	
	@Override
	public void serialize(Object input, O output) throws XmlSerializationException {
		if (prefixFactory == null) {
			throw new XmlSerializationException("prefixFactory must not be null");
		}
		
		Object root = input;
		if (input instanceof EntityContainer) {
			root = ((EntityContainer)input).getRoot(); 
			if (((EntityContainer) input).getSessionFactory() != null && Entity.class.isAssignableFrom(root.getClass())) {
				this.session = ((EntityContainer)input).getSessionFactory().openSession();
				root = this.session.merge(root);
			}
		}

		XMLStreamWriter2 sw;
		try {
			sw = createWriter(output);
			serialize(root, sw, new Stack<String>());
			sw.flush();
		}
		catch (XMLStreamException e) {
			throw new XmlSerializationException(e.getMessage(), e);
		}
		
		if (session != null) {
			session.disconnect();
		}
	}
	
	private void serialize(Object value, XMLStreamWriter2 writer, Stack<String> knownNamespaces) throws XmlSerializationException, XMLStreamException {
		if (value == null) {
			return;
		}

		if (hasClassAnnotation(value, XmlRootElement.class)) {
			QName qname = getQName(value);
			if (qname == null) {
				throw new XmlSerializationException(String.format("getQName() of class %s returned null", 
						value.getClass().getCanonicalName()));
			}
			String prefix = prefixFactory.getPrefix(qname);
	
			StaxXmlSerializer<O> subserializer;
			if (strategy.isRoot()) {
				subserializer = new StaxXmlSerializer<O>(strategy.asNonRoot());
				StringWriter tempOutput = new StringWriter();
				XMLOutputFactory ofact = XMLOutputFactory.newInstance();
				XMLStreamWriter2 subWriter = (XMLStreamWriter2)ofact.createXMLStreamWriter(tempOutput);

				writer.writeStartElement(prefix, qname.getLocalPart(), qname.getNamespaceURI());
				writer.writeNamespace(prefix, qname.getNamespaceURI());
				knownNamespaces.push(qname.getNamespaceURI());
				
				for (XmlNs ns : this.getDeclaredNamespaces(value)) {
					if (!knownNamespaces.contains(ns.namespaceURI())) {
						writer.writeNamespace(ns.prefix(), ns.namespaceURI());
						knownNamespaces.push(ns.namespaceURI());
						prefixFactory.registerPrefix(ns.namespaceURI(), ns.prefix());
					}
				}
				writeAttributes(value, writer);
				serializeInner(value, subserializer, writer, knownNamespaces);
			}
			else {
				writer.writeStartElement(prefix, qname.getLocalPart(), qname.getNamespaceURI());
				boolean popNamespace = false;
				if (!knownNamespaces.contains(qname.getNamespaceURI())) {
					writer.writeNamespace(prefix, qname.getNamespaceURI());
					knownNamespaces.push(qname.getNamespaceURI());
					popNamespace = true;
				}
				writeAttributes(value, writer);
				subserializer = this;
				serializeInner(value, subserializer, writer, knownNamespaces);
				if (popNamespace) {
					knownNamespaces.pop();
				}
			}
			
			writer.writeEndElement();
		}	
		else if (value instanceof EntityContainer) {
			Object root = ((EntityContainer)value).getRoot();
			if (this.session == null && ((EntityContainer)value).getSessionFactory() != null && Entity.class.isAssignableFrom(root.getClass())) {
				this.session = ((EntityContainer)value).getSessionFactory().openSession();
				root = this.session.merge(root);
			}
			this.serialize(root, writer, knownNamespaces);
		}
		else if (value instanceof Sequence) {
			for (Element element : (Sequence)value) {
				serialize(element, writer, knownNamespaces);
			}
		}
		else {
			writer.writeCharacters(value.toString());
//			writer.writeRaw(value.toString());
		}
	}

	private void serializeInner(Object value, StaxXmlSerializer<O> subserializer, XMLStreamWriter2 writer, Stack<String> knownNamespaces)
			throws XMLStreamException, XmlSerializationException {

		if (value instanceof EntityContainer) {
			if (this.session == null && ((EntityContainer)value).getSessionFactory() != null) {
				this.session = ((EntityContainer)value).getSessionFactory().openSession();
			}
			Object innerElement = ((EntityContainer)value).getRoot();
			innerElement = this.session.merge(innerElement);
			this.serialize(innerElement, writer, knownNamespaces);
		}
		else if (hasXmlValueAnnotation(value)) {
			Object xmlValue = getXmlValue(value);
			if (xmlValue instanceof EntityContainer) {
				this.serialize(xmlValue, writer, knownNamespaces);
			}
			else {
				writer.writeCharacters(xmlValue.toString());
			}
		}
		else if (hasClassAnnotation(value, XmlEnum.class)) {
			// Value is an enum
			writer.writeCharacters(getEnumValue(value));
		}
		else {
			for (AccessibleObject ao : getElementProperties(value)) {
				XmlElement element = ao.getAnnotation(XmlElement.class);
				String localName = element.name();
				String localNs = element.namespace();
				QName subQname = new QName(localNs, localName);
				String subPrefix = prefixFactory.getPrefix(subQname);
				
				boolean isInverseRef = ao.isAnnotationPresent(XmlInverseReference.class);
				String propertyName;
				if (ao instanceof Field) {
					propertyName = ((Field)ao).getName();
				}
				else if (ao instanceof Method) {
					propertyName = ((Method)ao).getName();					
				}
				else {
					throw new RuntimeException("Illegal annotation: Cannot annotate constructor with @XmlElement");
				}
				
				Object subValue;
				if (ao.isAnnotationPresent(XmlAccessor.class)) {
					XmlAccessor xmlacc = ao.getAnnotation(XmlAccessor.class);
					if (xmlacc.value() == XmlAccessType.PROPERTY) {
						PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(value.getClass(), propertyName);
						Method readMethod = pd.getReadMethod();
						try {
							subValue = readMethod.invoke(value);
						}
						catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							throw new XmlSerializationException(e.getMessage(), e);
						}
					}
					else {
						subValue = getPropertyValue(value, ao);
					}
				}
				else {
					subValue = getPropertyValue(value, ao);
				}
				
				if (subValue != null) {
					if (subValue instanceof Collection<?>) {
						if (((Collection<?>)subValue).isEmpty()) {
							if (!knownNamespaces.contains(localNs)) {
								writer.writeStartElement(subPrefix, localName, localNs);
								writer.writeNamespace(subPrefix, localNs);
								writer.writeEndElement();
							}
							else {
								writer.writeEmptyElement(subPrefix, localName, localNs);
							}
						}
						else {
							for (Object member : (Collection<?>)subValue) {
								serializeSubValue(writer, subserializer, element, subPrefix, subQname, member, isInverseRef, knownNamespaces);
							}
						}
					}
					else {
						serializeSubValue(writer, subserializer, element, subPrefix, subQname, subValue, isInverseRef, knownNamespaces);
					}
				}
				else {
					if (!knownNamespaces.contains(localNs)) {
						writer.writeStartElement(subPrefix, localName, localNs);
						writer.writeNamespace(subPrefix, localNs);
						writer.writeEndElement();
					}
					else {
						writer.writeEmptyElement(subPrefix, localName, localNs);
					}
				}
			}
		}
	}

	private void writeAttributes(Object value, XMLStreamWriter2 writer) throws XMLStreamException {
		for (AccessibleObject ao : getAnnotatedProperties(value, XmlAttribute.class)) {
			XmlAttribute attribute = ao.getAnnotation(XmlAttribute.class);
			String localName = attribute.name();
			String localNs = attribute.namespace();
			String prefix = getNamespacePrefix(value, localNs);
			QName subQname;
			if (prefix != null) { 
				subQname = new QName(localNs, localName, prefix);
			}
			else {
				subQname = new QName(localNs, localName);
				System.out.println(localNs + "/" + localName);
			}
			
			String subPrefix = prefixFactory.getPrefix(subQname);

			Object attributeValue = getPropertyValue(value, ao);
			if (attributeValue != null) {
				// TODO What about attributes that require namespace prefix?
				writer.writeAttribute(localName, attributeValue.toString());
			}
		}
	}

	private Object getPropertyValue(Object object, AccessibleObject property) {
		if (!property.isAccessible()) {
			property.setAccessible(true);
		}
		
		Object subValue;
		try {
			if (property instanceof Field) {
				subValue = ((Field)property).get(object);
			}
			else if (property instanceof Method) {
				subValue = ((Method)property).invoke(object);
			}
			else {
				throw new RuntimeException("Not implemented");
			}
			
			if (((Field)property).getType().equals(Date.class) && (subValue instanceof java.sql.Date)) {
				subValue = new Date(((java.sql.Date)subValue).getTime());
			}
		}
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return subValue;
	}

	private void serializeSubValue(/*O output*/ XMLStreamWriter2 writer, StaxXmlSerializer<O> subserializer,
			XmlElement elementAnnotation, String subPrefix, QName subQname, Object subValue, boolean asUuidRef, Stack<String> knownNamespaces) 
	throws XMLStreamException, XmlSerializationException {

		writer.writeStartElement(subPrefix, subQname.getLocalPart(), subQname.getNamespaceURI());
		boolean popNamespace = false;
		if (!knownNamespaces.contains(subQname.getNamespaceURI())) {
			writer.writeNamespace(subPrefix, subQname.getNamespaceURI());
			knownNamespaces.push(subQname.getNamespaceURI());
			popNamespace = true;
		}

		if (asUuidRef) {
			AccessibleObject idProperty = getAnnotatedField(subValue, XmlID.class);
			if (idProperty == null) {
				idProperty = getAnnotatedMethod(subValue, XmlID.class);
				if (idProperty == null) {
					throw new RuntimeException("Cannot handle inverse references without @XmlID annotated property");
				}
			}
				
			Object idValue = getPropertyValue(subValue, idProperty);
			if (idValue == null) {
				throw new RuntimeException("Cannot handle inverse references when value of @XmlID annotated property is null");
			}
			
			writer.writeAttribute("uuidref", idValue.toString());
		}
		else {
			if (elementAnnotation.type() != null && !elementAnnotation.type().equals(DEFAULT.class)) {
				Class<?> surrogateType = elementAnnotation.type();
				Constructor<?> surrogateConstructor;
				try {
					surrogateConstructor = surrogateType.getDeclaredConstructor(subValue.getClass());
					if (surrogateConstructor == null) {
						throw new RuntimeException("Surrogate type must have matching one-argument constructor");
					}

					subValue = surrogateConstructor.newInstance(subValue);
				}
				catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
			subserializer.serialize(subValue, writer, knownNamespaces);
		}
		
		writer.writeEndElement();
		if (popNamespace) {
			knownNamespaces.pop();
		}
	}

	private XMLStreamWriter2 createWriter(O target) throws FactoryConfigurationError, XMLStreamException {
		if (target == null) {
			throw new IllegalArgumentException("target must not be null");
		}
		
		XMLOutputFactory ofact = XMLOutputFactory.newInstance();
		XMLStreamWriter2 sw;
		if (target instanceof Writer) {
			sw = (XMLStreamWriter2)ofact.createXMLStreamWriter((Writer)target);
		}
		else if (target instanceof OutputStream) {
			sw = (XMLStreamWriter2)ofact.createXMLStreamWriter((OutputStream)target, "UTF-8");
		}
		else if (target instanceof Result) {
			sw = (XMLStreamWriter2)ofact.createXMLStreamWriter((Result)target);
		}
		else {
			throw new RuntimeException(String.format("Target of class %s is not supported.", target.getClass().getCanonicalName()));
		}

		return sw;
	}
	
	private QName getQName(Object o) {
		Annotation annotation = o.getClass().getAnnotation(XmlRootElement.class);
		if (annotation != null) {
			XmlRootElement xre = (XmlRootElement)annotation;
			String name = xre.name();
			String namespace = xre.namespace();
			String prefix = getNamespacePrefix(o, namespace);
			
			if (prefix != null) {
				return new QName(namespace, name, prefix);
			}
			else {
				return new QName(namespace, name);
			}
		}

		annotation = o.getClass().getAnnotation(XmlType.class);
		if (annotation != null) {
			XmlType xt = (XmlType)annotation;
			String name = xt.name();
			String namespace = xt.namespace();
			String prefix = getNamespacePrefix(o, namespace);
			
			if (prefix != null) {
				return new QName(namespace, name, prefix);
			}
			else {
				return new QName(namespace, name);
			}
		}
		
		return null;
	}
	
	public Collection<Namespace> getNamespaces() {
		List<Namespace> result = new ArrayList<Namespace>();
		
		for (String ns : prefixFactory.getNamespaces()) {
			result.add(new Namespace(prefixFactory.getPrefix(ns), ns));
		}
		
		return result;
	}

	private String getNamespacePrefix(Object o, String namespaceUri) {
		Annotation schemaAnnotation = o.getClass().getPackage().getAnnotation(XmlSchema.class);
		if (schemaAnnotation != null) {
			XmlSchema sa = (XmlSchema)schemaAnnotation;
			for (XmlNs ns : sa.xmlns()) {
				if (ns.namespaceURI().equals(namespaceUri)) {
					return ns.prefix();
				}
			}
		}
		
		return null;
	}
	
	private List<XmlNs> getDeclaredNamespaces(Object o) {
		List<XmlNs> result = new ArrayList<XmlNs>();
		
		Annotation schemaAnnotation = o.getClass().getPackage().getAnnotation(XmlSchema.class);
		if (schemaAnnotation != null) {
			XmlSchema sa = (XmlSchema)schemaAnnotation;
			result.addAll(Arrays.asList(sa.xmlns()));
		}

		return result;
	}
	
	private boolean hasClassAnnotation(Object o, Class<? extends Annotation> annotationClass) {
		return o.getClass().getAnnotation(annotationClass) != null;
	}
	
	private boolean hasXmlValueAnnotation(Object o) {
		return getXmlValueField(o) != null || getXmlValueMethod(o) != null;
	}
	
	private Field getXmlValueField(Object o) {
		return getAnnotatedField(o, XmlValue.class);
	}
	
	private Field getAnnotatedField(Object o, Class<? extends Annotation> annotation) {
		for (Field field : getAllFields(o)) {
			if (field.getAnnotation(annotation) != null) {
				return field;
			}
		}
		
		return null;
	}
	
	private Method getXmlValueMethod(Object o) {
		return getAnnotatedMethod(o, XmlValue.class);
	}
	
	private Method getAnnotatedMethod(Object o, Class<? extends Annotation> annotation) {
		for (Method method : getAllMethods(o)) {
			if (method.getAnnotation(annotation) != null) {
				return method;
			}
		}
		
		return null;
	}
	
	private Serializable getXmlValue(Object o) {
		Field f = getXmlValueField(o);
		if (f != null) {
			if (!f.isAccessible()) {
				f.setAccessible(true);
			}
			try {
				return (Serializable)f.get(o);
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		
		Method m = getXmlValueMethod(o);
		if (m != null) {
			if (!m.isAccessible()) {
				m.setAccessible(true);
			}
			
			if (!Arrays.asList(m.getParameterTypes()).isEmpty()) {
				throw new RuntimeException("XmlValue annotation of method with parameters is not supported");
			}
			
			try {
				return (Serializable)m.invoke(o);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		
		throw new RuntimeException(String.format("No @XmlValue annotation present in object class %s", o.getClass().getCanonicalName()));
	}

	private List<AccessibleObject> getAnnotatedProperties(Object o, Class<? extends Annotation> annotation) {
		ArrayList<AccessibleObject> result = new ArrayList<AccessibleObject>();

		for (Field f : getAllFields(o)) {
			if (f.isAnnotationPresent(annotation)) {
				result.add(f);
			}
		}
		
		for (Method m : getAllMethods(o)) {
			if (m.isAnnotationPresent(annotation)) {
				result.add(m);
			}
		}

		return result;
	}
	
	
	private List<AccessibleObject> getElementProperties(Object o) {
		ArrayList<AccessibleObject> result = new ArrayList<AccessibleObject>();

		Stack<Class<?>> hierarchy = new Stack<Class<?>>();
		Class<?> clazz = o.getClass(); 
		do {
			hierarchy.push(clazz);
			clazz = clazz.getSuperclass();
		} while (clazz != null);
		
		List<String> propOrder = new ArrayList<String>();
		do {
			clazz = hierarchy.pop();
			if (clazz.isAnnotationPresent(XmlType.class)) {
				XmlType xmlType = clazz.getAnnotation(XmlType.class);
				if (xmlType.propOrder() != null) {
					propOrder.addAll(Arrays.asList(xmlType.propOrder()));
				}
			}
		} while (!hierarchy.isEmpty());
		
		if (!propOrder.isEmpty()) {
			Map<String, Field> fieldMappings = new HashMap<String, Field>();
			for (Field f : getAllFields(o)) {
				if (f.isAnnotationPresent(XmlElement.class)) {
//					XmlElement element = f.getAnnotation(XmlElement.class);
					fieldMappings.put(f.getName(), f);
				}
			}
			
			Map<String, Method> methodMappings = new HashMap<String, Method>();
			for (Method m : getAllMethods(o)) {
				if (m.isAnnotationPresent(XmlElement.class)) {
//					XmlElement element = m.getAnnotation(XmlElement.class);
					methodMappings.put(BeanUtils.findPropertyForMethod(m).getName(), m);
				}
			}

			for (String prop : propOrder) {
				if (fieldMappings.containsKey(prop)) {
					result.add(fieldMappings.get(prop));
				}
				else if (methodMappings.containsKey(prop)) {
					result.add(methodMappings.get(prop));
				}
			}
		}
		else {
			for (Field f : getAllFields(o)) {
				if (f.isAnnotationPresent(XmlElement.class)) {
					result.add(f);
				}
			}
			
			for (Method m : getAllMethods(o)) {
				if (m.isAnnotationPresent(XmlElement.class)) {
					result.add(m);
				}
			}
		}
		
		return result;
	}
	
	private String getEnumValue(Object enumValue) {
		if (!enumValue.getClass().isEnum()) {
			throw new RuntimeException("Value is not an enum constant");			
		}
		
		if (!enumValue.getClass().isAnnotationPresent(XmlEnum.class)) {
			throw new RuntimeException("Type must be annotated with @XmlEnum");
		}

		for (Field f : enumValue.getClass().getDeclaredFields()) {
			if (f.getName().equals(enumValue.toString())) {
				if (f.isAnnotationPresent(XmlEnumValue.class)) {
					return f.getAnnotation(XmlEnumValue.class).value();
				}
			}
		}

		return enumValue.toString();
	}
	
	private List<Field> getAllFields(Object o) {
		List<Field> result = new ArrayList<Field>();
		Class<?> clazz = o.getClass();
		
		do {
			if (clazz.getDeclaredFields() != null) {
				result.addAll(Arrays.asList(clazz.getDeclaredFields()));
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null);
		
		return result;
	}

	private List<Method> getAllMethods(Object o) {
		List<Method> result = new ArrayList<Method>();
		Class<?> clazz = o.getClass();
		
		do {
			if (clazz.getDeclaredFields() != null) {
				result.addAll(Arrays.asList(clazz.getDeclaredMethods()));
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null);
		
		return result;
	}
}	
