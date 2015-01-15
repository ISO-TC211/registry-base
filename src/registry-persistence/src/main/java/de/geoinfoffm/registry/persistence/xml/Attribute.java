package de.geoinfoffm.registry.persistence.xml;

/**
 * This class represents an XML attribute.
 * 
 * @author Florian Esser
 *
 */
public class Attribute
{
	private String name;
	private String value;

	public Attribute(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}

}
