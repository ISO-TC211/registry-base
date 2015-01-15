package de.geoinfoffm.registry.core.model.iso19115;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * Information about online sources from which the dataset, specification, or
 * community profile name and extended metadata elements can be obtained.
 * 
 * @created 10-Sep-2013 19:43:12
 */
@XmlType(name = "CI_OnlineResource_Type", namespace = "http://www.isotc211.org/2005/gmd",
propOrder = { "linkage", "protocol", "applicationProfile",
			  "name", "description", "function" })
@XmlRootElement(name = "CI_OnlineResource", namespace = "http://www.isotc211.org/2005/gmd")
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@Audited @Entity
public class CI_OnlineResource extends de.geoinfoffm.registry.core.Entity
{
	/**
	 * Method, source, or location for online access.
	 * Example: a Uniform Resource Locator (URL) such as http://www,gii.getty.
	 * edu/tgn_browser/
	 */
	@XmlElement(name = "linkage", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@org.hibernate.validator.constraints.URL
	public String linkage;
	/**
	 * Connection protocol to be used
	 */
	@XmlElement(name = "protocol", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	public String protocol;
	/**
	 * Name of an application profile that can be used with the resource
	 */
	@XmlElement(name = "applicationProfile", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	public String applicationProfile;
	/**
	 * Name of the resource
	 */
	@XmlElement(name = "name", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	public String name;
	/**
	 * Description of what the resource is/does
	 */
	@XmlElement(name = "description", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	public String description;
	/**
	 * Function performed by the resource
	 */
	@XmlElement(name = "function", namespace = "http://www.isotc211.org/2005/gmd")
	public CI_OnLineFunctionCode function;
	
	protected CI_OnlineResource() {

	}

	/**
	 * @return the linkage
	 */
	public String getLinkage() {
		return linkage;
	}

	/**
	 * @param linkage the linkage to set
	 */
	public void setLinkage(String linkage) {
		this.linkage = linkage;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the applicationProfile
	 */
	public String getApplicationProfile() {
		return applicationProfile;
	}

	/**
	 * @param applicationProfile the applicationProfile to set
	 */
	public void setApplicationProfile(String applicationProfile) {
		this.applicationProfile = applicationProfile;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the function
	 */
	public CI_OnLineFunctionCode getFunction() {
		return function;
	}

	/**
	 * @param function the function to set
	 */
	public void setFunction(CI_OnLineFunctionCode function) {
		this.function = function;
	}

	public void finalize() throws Throwable {

	}
}// end CI_OnlineResource