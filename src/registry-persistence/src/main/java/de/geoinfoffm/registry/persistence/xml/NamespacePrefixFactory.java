package de.geoinfoffm.registry.persistence.xml;

import java.util.Collection;

import javax.xml.namespace.QName;

public interface NamespacePrefixFactory
{
	public void registerPrefix(String namespaceUrl, String prefix);
	public String getPrefix(QName qname);
	public String getPrefix(String namespaceUrl);
	public Collection<String> getNamespaces();
}
