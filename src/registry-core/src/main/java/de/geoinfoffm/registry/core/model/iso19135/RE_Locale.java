package de.geoinfoffm.registry.core.model.iso19135;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.model.iso00639.LanguageCode;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;
import de.geoinfoffm.registry.core.model.iso19115.MD_CharacterSetCode;

/**
 * <UsedBy>
 * <NameSpace>ISO 19135 Procedures for registration</NameSpace>
 * <Class>RE_Register</Class>
 * <Attribute>operatingLanguage</Attribute>
 * <Type>RE_Locale</Type>
 * <UsedBy>
 * 
 * <UsedBy>
 * <NameSpace>ISO 19135 Procedures for registration</NameSpace>
 * <Class>RE_SubregisterDescription</Class>
 * <Attribute>operatingLanguage</Attribute>
 * <Type>RE_Locale</Type>
 * <UsedBy>
 * @created 09-Sep-2013 19:13:09
 */
@XmlType(name = "RE_Locale_Type", namespace = "http://www.isotc211.org/2005/grg",
		 propOrder = { "name", "language", "country", "characterEncoding", "citation" })
@XmlRootElement(name = "RE_Locale", namespace = "http://www.isotc211.org/2005/grg")
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@Audited @Entity
public class RE_Locale extends de.geoinfoffm.registry.core.Entity
{
	private static final long serialVersionUID = 1718486761482773461L;

	@XmlElement(name = "name", namespace = "http://www.isotc211.org/2005/grg", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String name;

	@XmlElement(name = "language", namespace = "http://www.isotc211.org/2005/grg")
	private LanguageCode language;

	@XmlElement(name = "country", namespace = "http://www.isotc211.org/2005/grg", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String country;

	@XmlElement(name = "characterEncodig", namespace = "http://www.isotc211.org/2005/grg")
	private MD_CharacterSetCode characterEncoding;

	@XmlElement(name = "citation", namespace = "http://www.isotc211.org/2005/grg")
	@OneToOne
	private CI_Citation citation;

	public RE_Locale(){

	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the language
	 */
	public LanguageCode getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(LanguageCode language) {
		this.language = language;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the characterEncoding
	 */
	public MD_CharacterSetCode getCharacterEncoding() {
		return characterEncoding;
	}

	/**
	 * @param characterEncoding the characterEncoding to set
	 */
	public void setCharacterEncoding(MD_CharacterSetCode characterEncoding) {
		this.characterEncoding = characterEncoding;
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

}//end RE_Locale