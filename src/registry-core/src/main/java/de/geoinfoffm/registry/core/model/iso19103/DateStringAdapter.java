package de.geoinfoffm.registry.core.model.iso19103;

import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class DateStringAdapter extends XmlAdapter<String, java.util.Date>
{

	@Override
	public java.util.Date unmarshal(String v) throws Exception {
		return DatatypeConverter.parseDate(v).getTime();
	}

	@Override
	public String marshal(java.util.Date v) throws Exception {
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTime(v);		
		XMLGregorianCalendar xmlcal;
		try {
			xmlcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		}
		catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		return DatatypeConverter.printDate(gcal);
	}

}
