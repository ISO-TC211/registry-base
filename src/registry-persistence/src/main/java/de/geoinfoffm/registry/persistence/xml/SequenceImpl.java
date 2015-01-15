package de.geoinfoffm.registry.persistence.xml;

import java.util.ArrayList;
import java.util.Collection;

public class SequenceImpl extends ArrayList<Element> implements Sequence
{

	public SequenceImpl() {
	}

	public SequenceImpl(int initialCapacity) {
		super(initialCapacity);
	}

	public SequenceImpl(Collection<? extends Element> c) {
		super(c);
	}

//	@Override
//	public String getXmlValue(NamespacePrefixFactory prefixFactory) throws XmlSerializationException {
//		StringBuilder sb = new StringBuilder();
//		for (Element o : this) {
//			sb.append(o.toXml(prefixFactory));
//		}
//		
//		return sb.toString();
//	}

}
