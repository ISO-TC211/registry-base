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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * Creates namespace prefixes for XML export.
 * 
 * @author Florian Esser
 *
 */
public class NamespacePrefixFactoryImpl implements NamespacePrefixFactory
{
	private final Map<String, String> mappedNamespaces = new HashMap<String, String>();
	private long prefixCounter = 0; 

	@Override
	public String getPrefix(String namespaceUrl) {
		String prefix = mappedNamespaces.get(namespaceUrl);
		
		if (prefix == null) {
			do {
				prefix = "ns" + Long.toString(prefixCounter++);
			} while (mappedNamespaces.containsKey(prefix));

			mappedNamespaces.put(namespaceUrl, prefix);
		}
		
		return prefix;
	}
	
	@Override
	public String getPrefix(QName qname) {
		if (!qname.getPrefix().equals(XMLConstants.DEFAULT_NS_PREFIX) && !mappedNamespaces.containsKey(qname.getNamespaceURI())) {
			registerPrefix(qname.getNamespaceURI(), qname.getPrefix());
		}

		return getPrefix(qname.getNamespaceURI());
	}

	@Override
	public void registerPrefix(String namespaceUrl, String prefix) {
		if (!mappedNamespaces.containsKey(namespaceUrl) || mappedNamespaces.get(namespaceUrl).equals(prefix)) {
			mappedNamespaces.put(namespaceUrl, prefix);
		}
		else {
			throw new RuntimeException(String.format("Namespace prefix for namespace %s already registered [is: %s]", namespaceUrl, mappedNamespaces.get(namespaceUrl)));
		}
	}
	
	@Override
	public Collection<String> getNamespaces() {
		return mappedNamespaces.keySet();
	}
}
