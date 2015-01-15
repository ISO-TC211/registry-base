package de.geoinfoffm.registry.core;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PropertyTypeAdapter<R> extends XmlAdapter<PropertyType<R>, R>
{
	@Override
	public R unmarshal(PropertyType<R> v) throws Exception {
		return v.getRef();
	}

	@Override
	public PropertyType<R> marshal(R v) throws Exception {
		return new PropertyType(v);
	}
}
