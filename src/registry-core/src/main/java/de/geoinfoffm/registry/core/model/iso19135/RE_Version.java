package de.geoinfoffm.registry.core.model.iso19135;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.ValueObject;
import de.geoinfoffm.registry.core.model.DateAdapter;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * <UsedBy>
 * <NameSpace>ISO 19135 Procedures for registration</NameSpace>
 * <Class>RE_Register</Class>
 * <Attribute>version[0..1]</Attribute>
 * <Type>RE_Version</Type>
 * <UsedBy>
 * @created 09-Sep-2013 19:14:44
 */
@XmlType(name = "RE_Version_Type", namespace = "http://www.isotc211.org/2005/grg",
		 propOrder = { "versionNumber", "versionDate" })
@XmlRootElement(name = "RE_Version", namespace = "http://www.isotc211.org/2005/grg")
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@Embeddable
public class RE_Version extends ValueObject
{
	@XmlElement(name = "versionNumber", namespace = "http://www.isotc211.org/2005/grg", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String versionNumber;

	@XmlElement(name = "versionDate", namespace = "http://www.isotc211.org/2005/grg", type = de.geoinfoffm.registry.core.model.iso19103.Date.class)
	@XmlJavaTypeAdapter(DateAdapter.class)
	@Temporal(TemporalType.DATE)
	private Date versionDate;

	public RE_Version() {

	}

	/**
	 * @return the versionNumber
	 */
	public String getVersionNumber() {
		return versionNumber;
	}

	/**
	 * @param versionNumber the versionNumber to set
	 */
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * @return the versionDate
	 */
	public Date getVersionDate() {
		return versionDate;
	}

	/**
	 * @param versionDate the versionDate to set
	 */
	public void setVersionDate(Date versionDate) {
		this.versionDate = versionDate;
	}

	public void finalize() throws Throwable {

	}
}//end RE_Version