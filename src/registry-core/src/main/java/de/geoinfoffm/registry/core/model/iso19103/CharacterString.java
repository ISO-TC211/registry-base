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

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.ValueObject;

/**
 * This class represents the CharacterString class defined in ISO 19103.
 * 
 * @author Florian Esser
 *
 */
@Access(AccessType.FIELD)
@XmlRootElement(name = "CharacterString", namespace = "http://www.isotc211.org/2005/gco")
@XmlAccessorType(XmlAccessType.FIELD)
@Audited @Embeddable
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
