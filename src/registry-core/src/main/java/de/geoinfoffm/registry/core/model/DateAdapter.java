package de.geoinfoffm.registry.core.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import de.geoinfoffm.registry.core.PropertyType;
import de.geoinfoffm.registry.core.model.iso19103.Date;

public class DateAdapter extends XmlAdapter<PropertyType<Date>, java.util.Date>
{

	@Override
	public java.util.Date unmarshal(PropertyType<Date> v) throws Exception {
		if (v == null) {
			return null;
		}
		return v.getRef().javaDate();
	}

	@Override
	public PropertyType<Date> marshal(java.util.Date v) throws Exception {
		if (v == null) {
			return null;
		}
		
		return new PropertyType<Date>(new Date(v));
	}

}
