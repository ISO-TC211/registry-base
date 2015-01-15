package de.geoinfoffm.registry.core.model.iso19103;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import de.geoinfoffm.registry.core.ValueObject;

/**
 * This class represents the Integer class defined in ISO 19103.
 * 
 * @author Florian Esser
 *
 */
@Embeddable
@Access(AccessType.FIELD)
@XmlType(name = "Integer", namespace = "http://www.isotc211.org/2005/gco")
@XmlRootElement(name = "Integer", namespace = "http://www.isotc211.org/2005/gco")
@XmlAccessorType(XmlAccessType.FIELD)
public class Integer extends ValueObject implements Serializable
{
	@XmlValue
	private BigInteger value = null;
	
	protected Integer() {
		this.value = null;
	}

	public Integer(BigInteger value) {
		this.value = value;
	}
	
	@Override
	protected Integer clone() throws CloneNotSupportedException {
		return new Integer(value);
	}

	@Override
	public String toString() {
		if (value == null) {
			return null;
		}
		else {
			return value.toString();
		}
	}
	
	@Transient
	public boolean isEmpty() {
		return (value == null);
	}

}
