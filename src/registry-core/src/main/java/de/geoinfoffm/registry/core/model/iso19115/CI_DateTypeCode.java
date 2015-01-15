package de.geoinfoffm.registry.core.model.iso19115;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import de.geoinfoffm.registry.core.model.iso19103.CodeListValue;

/**
 * 
 * @created 10-Sep-2013 19:42:56
 */
@XmlRootElement(name = "CI_DateTypeCode", namespace = "http://www.isotc211.org/2005/gmd")
@Embeddable 
public class CI_DateTypeCode extends CodeListValue 
{
	/**
	 * Creation
	 */
	public static final CI_RoleCode CREATION = new CI_RoleCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode", "creation", "creation");

	/**
	 * Publication
	 */
	public static final CI_RoleCode PUBLICATION = new CI_RoleCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode", "publication", "publication");

	/**
	 * Revision
	 */
	public static final CI_RoleCode REVISION = new CI_RoleCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode", "revision", "revision");
	
	protected CI_DateTypeCode() { }
	
	public CI_DateTypeCode(String codeList, String codeListValue, String code) {
		super(codeList, codeListValue, code, new QName("http://www.isotc211.org/2005/gmd", "CI_DateTypeCode", "gmd")); 
	}
	
}//end CI_DateTypeCode