package de.geoinfoffm.registry.core.model.iso19103;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import de.geoinfoffm.registry.core.ValueObject;

/**
 * This class represents the CharacterString class defined in ISO 19103.
 * 
 * @author Florian Esser
 *
 */
@Embeddable
@Access(AccessType.FIELD)
@XmlRootElement(name = "CharacterString", namespace = "http://www.isotc211.org/2005/gco")
@XmlAccessorType(XmlAccessType.FIELD)
public class CharacterString extends ValueObject implements CharSequence, Serializable
{
	private static final long serialVersionUID = -6488446334222986744L;

	@XmlValue
	@Column(columnDefinition = "text")
	private String value = null;
	
	protected CharacterString() {
		this.value = null;
	}

	public CharacterString(String value) {
		this.value = value;
	}
	
	@Override
	protected CharacterString clone() throws CloneNotSupportedException {
		return new CharacterString(new String(value));
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
		return (value == null || value.isEmpty());
	}

	public static String asString(CharacterString cs) {
		if (cs == null) {
			return null;
		}
		else {
			return cs.toString();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#length()
	 */
	@Override
	public int length() {
		if (value == null) {
			return 0;
		}
		else {
			return value.length();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#charAt(int)
	 */
	@Override
	public char charAt(int index) {
		if (value == null) {
			throw new IndexOutOfBoundsException("The CharacterString has no value");
		}
		else {
			return value.charAt(index);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#subSequence(int, int)
	 */
	@Override
	public CharSequence subSequence(int start, int end) {
		if (value == null) {
			throw new IndexOutOfBoundsException("The CharacterString has no value");
		}
		else {
			return value.subSequence(start, end);
		}
	}
	
}
