package de.geoinfoffm.registry.core.model;

import java.math.BigInteger;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import de.geoinfoffm.registry.core.PropertyType;

public class BigIntegerAdapter extends XmlAdapter<PropertyType<BigInteger>, BigInteger>
{

	@Override
	public BigInteger unmarshal(PropertyType<BigInteger> v) throws Exception {
		if (v == null) {
			return null;
		}

		return v.getRef();
	}

	@Override
	public PropertyType<BigInteger> marshal(BigInteger v) throws Exception {
		if (v == null) {
			return null;
		}
		return new PropertyType<BigInteger>(v);
	}

}
