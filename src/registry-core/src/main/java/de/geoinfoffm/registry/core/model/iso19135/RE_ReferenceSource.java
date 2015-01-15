package de.geoinfoffm.registry.core.model.iso19135;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.ValueObject;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;


/**
 * 
 * @created 09-Sep-2013 19:13:35
 */
@XmlType(name = "RE_ReferenceSource_Type", namespace = "http://www.isotc211.org/2005/grg",
	     propOrder = { "text", "citation" })
@XmlRootElement(name = "RE_ReferenceSource", namespace = "http://www.isotc211.org/2005/grg")
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@Embeddable
public class RE_ReferenceSource extends ValueObject
{
	@XmlElement(name = "text", namespace = "http://www.isotc211.org/2005/grg", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String text;
	
	@XmlElement(name = "citation", namespace = "http://www.isotc211.org/2005/grg")
	@OneToOne
	private CI_Citation citation;

	public RE_ReferenceSource(){

	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the citation
	 */
	public CI_Citation getCitation() {
		return citation;
	}

	/**
	 * @param citation the citation to set
	 */
	public void setCitation(CI_Citation citation) {
		this.citation = citation;
	}

	public void finalize() throws Throwable {

	}
}//end RE_ReferenceSource