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
