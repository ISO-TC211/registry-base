package de.geoinfoffm.registry.core;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

@XmlSeeAlso(CharacterString.class)
public class CharacterStringAdapter extends XmlAdapter<PropertyType<CharacterString>, String> {

	@Override
	public String unmarshal(PropertyType<CharacterString> v) throws Exception {
		if (v == null) {
			return null;
		}

		return CharacterString.asString(v.getRef());
	}

	@Override
	public PropertyType<CharacterString> marshal(String v) throws Exception {
		if (v == null) {
			return null;
		}

		return new PropertyType(new CharacterString(v));
	}
}
