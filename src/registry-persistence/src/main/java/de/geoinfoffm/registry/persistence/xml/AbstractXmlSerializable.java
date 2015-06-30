/**
 * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *     * Redistributions of source code must retain the above copyright
 *     	 notice, this list of conditions and the following disclaimer.

 *     * Redistributions in binary form must reproduce the above
 *     	 copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials
 *       provided with the distribution.

 *     * The names "German Federal Agency for Cartography and Geodesy",
 *       "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE",
 *       "GDI-DE Registry" and the names of other contributors must not
 *       be used to endorse or promote products derived from this
 *       software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE GERMAN
 * FEDERAL AGENCY FOR CARTOGRAPHY AND GEODESY BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.geoinfoffm.registry.persistence.xml;

import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import de.geoinfoffm.registry.persistence.xml.exceptions.XmlSerializationException;

/**
 * Abstract base for all classes that are to be serialized to XML.
 * 
 * @author Florian Esser
 *
 */
@XmlTransient
public abstract class AbstractXmlSerializable implements XmlSerializable
{
	private static final long serialVersionUID = -5630568401785439553L;

	/**
	 * Returns the namespace prefix for a given namespace URI.
	 * 
	 * @param namespaceUri URI to resolve.
	 * @return the prefix for the URI or null if it cannot be resolved
	 */
	@Transient
	private String getNamespacePrefix(String namespaceUri) {
		Annotation schemaAnnotation = this.getClass().getPackage().getAnnotation(XmlSchema.class);
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
	
	/**
	 * @return the {@link QName} for objects of this class or null if the QName could not be determined.
	 */
	@Transient
	@Override
	public QName getQName() {
		Annotation annotation = this.getClass().getAnnotation(XmlRootElement.class);
		if (annotation != null) {
			XmlRootElement xre = (XmlRootElement)annotation;
			String name = xre.name();
			String namespace = xre.namespace();
			String prefix = getNamespacePrefix(namespace);
			
			if (prefix != null) {
				return new QName(namespace, name, prefix);
			}
			else {
				return new QName(namespace, name);
			}
		}

		annotation = this.getClass().getAnnotation(XmlType.class);
		if (annotation != null) {
			XmlType xt = (XmlType)annotation;
			String name = xt.name();
			String namespace = xt.namespace();
			String prefix = getNamespacePrefix(namespace);
			
			if (prefix != null) {
				return new QName(namespace, name, prefix);
			}
			else {
				return new QName(namespace, name);
			}
		}
		
		return null;
	}

	
	/**
	 * Returns a list of {@link Attribute}s that will be serialized
	 * as XML attributes in the DOM element of this object.<br>
	 * <br>
	 * Returns an empty list by default. Must be overwritten by derived classes if necessary.
	 */
	@Transient
	public Collection<Attribute> getXmlAttributes() {
		return new ArrayList<Attribute>();
	}
	
	/**
	 * Helper function to create a {@link Sequence} from a set of objects.
	 * 
	 * @param values Objects to contain in the sequence. Valid objects are either {@link Element}s or a {@link Collection} of {@link Element}s.
	 * @return The created sequence.
	 */
	@Transient
	protected Sequence sequence(Object... values) {
		Sequence result = new SequenceImpl();
		for (Object value : values) {
			if (value instanceof Element) {
				result.add((Element)value);
			}
			else if (value instanceof Collection) {
				for (Object subValue : (Collection<?>)value) {
					if (subValue instanceof Element) {
						result.add((Element)subValue);
					}
				}
			}
			else {
				throw new RuntimeException("Illegal object in Sequence: " + value.getClass().getCanonicalName());
			}
		}
		return result;
	}

	/**
	 * Helper function to create an {@link Element} from an {@link XmlSerializable} value.
	 *
	 * @param elementName Name of the element to create
	 * @param parentQname {@link QName} of the parent element.
	 * @param value Value
	 * @return The created {@link Element}.
	 */
	@Transient
	protected Element element(String elementName, QName parentQname, XmlSerializable value) {
		return element(elementName, parentQname, Collections.EMPTY_LIST, value);
	}

	/**
	 * Helper function to create an {@link Element} from an {@link XmlSerializable} value.
	 *
	 * @param elementName Name of the element to create
	 * @param parentQname {@link QName} of the parent element
	 * @param attributes a set of XML attributes for the element
	 * @param value Value
	 * @return The created {@link Element}.
	 */
	@Transient
	protected Element element(String elementName, QName parentQname, Collection<Attribute> attributes, XmlSerializable value) {
		return new Element(elementName, parentQname, attributes, value);
	}
	
	/**
	 * Helper function to create an {@link Element} from an {@link XmlSerializable} value.
	 *
	 * @param elementName Name of the element to create
	 * @param parentQname {@link QName} of the parent element.
	 * @param value Value
	 * @return The created {@link Element}.
	 */
	@Transient
	protected Collection<Element> elements(String localPart, Collection<? extends XmlSerializable> values) {
		Collection<Element> result = new ArrayList<Element>();
		for (XmlSerializable value : values) {
			result.add(element(localPart, value));
		}
		
		return result;
	}
	
	/**
	 * Helper function to create an {@link Element} from an {@link XmlSerializable} value.
	 *
	 * @param elementName Name of the element to create
	 * @param parentQname {@link QName} of the parent element.
	 * @param value Value
	 * @return The created {@link Element}.
	 */
	@Transient
	protected Element element(String elementName, XmlSerializable value) {
		return new Element(elementName, this.getQName(), Collections.EMPTY_LIST, value);
	}
		
	/**
	 * Creates an XML representation of the given {@link XmlSerializable}.
	 * 
	 * @param o Object to serialize
	 * @return a {@link String} with the XML representation.
	 * @throws XmlSerializationException
	 */
	public static String serialize(XmlSerializable o) throws XmlSerializationException {
		StringWriter sw = new StringWriter();
		serialize(o, sw);
		return sw.toString();
	}
	
	/**
	 * Creates an XML representation of the given {@link XmlSerializable}.
	 * 
	 * @param o Object to serialize
	 * @param w {@link Writer} to write the XML representation to
	 * @throws XmlSerializationException
	 */
	public static void serialize(XmlSerializable o, Writer w) throws XmlSerializationException {
		StaxSerializationStrategy strategy = new StaxSerializationStrategy(true);
		StaxXmlSerializer<Writer> serializer = new StaxXmlSerializer<Writer>(strategy);
		serializer.serialize(o, w);
	}
}
