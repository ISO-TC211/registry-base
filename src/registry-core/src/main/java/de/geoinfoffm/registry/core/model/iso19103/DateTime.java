package de.geoinfoffm.registry.core.model.iso19103;

import java.util.TimeZone;

import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @created 10-Sep-2013 20:18:53
 */
@XmlType
public final class DateTime extends java.util.Date {
	
	private TimeZone _timeZone = TimeZone.getDefault();
	
	public DateTime() {
		super();
	}
	
	public DateTime(java.util.Date javaDate) {
		super(javaDate.getTime());
	}
	
	public DateTime(TimeZone timeZone) {
		super();
		_timeZone = timeZone;
	}

	public CharacterString getHour() {
		return null;
	}

	public CharacterString getMinute() {
		// TODO Auto-generated method stub
		return null;
	}

	public CharacterString getSecond() {
		// TODO Auto-generated method stub
		return null;
	}

	public CharacterString getTimeZone() {
		// TODO Auto-generated method stub
		return null;
	}

	public CharacterString getCentury() {
		return null;
	}

	public DatePrecision getPrecision() {
		// TODO Auto-generated method stub
		return null;
	}

}//end DateTime