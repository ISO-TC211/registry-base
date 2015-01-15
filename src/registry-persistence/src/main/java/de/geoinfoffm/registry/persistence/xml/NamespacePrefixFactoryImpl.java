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
