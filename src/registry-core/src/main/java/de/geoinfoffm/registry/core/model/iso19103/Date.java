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