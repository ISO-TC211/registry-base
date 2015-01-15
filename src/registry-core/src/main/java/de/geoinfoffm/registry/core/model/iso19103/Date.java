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

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * 
 * @created 10-Sep-2013 20:18:37
 */
@XmlRootElement(name = "Date", namespace = "http://www.isotc211.org/2005/gco")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Date 
{
	private java.util.Date date;
	
	protected Date() {
		this.date = Calendar.getInstance().getTime();
	}
	
	public Date(java.util.Date date) {
		this.date = new java.util.Date(date.getTime());
	}
	
	public Date(String xmlDate) {
		this.setValue(xmlDate);
	}
	
	@XmlValue
	public String getValue() {
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTime(date);		
		XMLGregorianCalendar xmlcal;
		try {
			xmlcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		}
		catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		return DatatypeConverter.printDate(gcal);
	}
	
	@XmlTransient
	public void setValue(String xmlValue) {
		this.date = DatatypeConverter.parseDate(xmlValue).getTime();
	}

	@XmlTransient
	public String year() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		return String.valueOf(cal.get(Calendar.YEAR));
	}
	
	public java.util.Date javaDate() {
		return new java.util.Date(this.date.getTime());
	}
	
//	public CharacterString getCentury();
//	
//	public int getYear();
//	public int getMonth();
//	public int getDay();
//	public DatePrecision getPrecision();

}//end Date