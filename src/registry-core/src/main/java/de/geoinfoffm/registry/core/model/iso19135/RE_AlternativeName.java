package de.geoinfoffm.registry.core.model.iso19135;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.PropertyType;
import de.geoinfoffm.registry.core.ValueObject;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

@Embeddable
@XmlRootElement(name = "RE_AlternativeName", namespace = "http://www.isotc211.org/2005/grg")
public class RE_AlternativeName extends ValueObject
{
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Basic(optional = false)
	@Column(columnDefinition = "text")
	private String name;
	
	protected RE_AlternativeName() { }
	
	public RE_AlternativeName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
