package de.geoinfoffm.registry.core.model.iso19115;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import de.geoinfoffm.registry.core.model.iso19103.CodeListValue;



/**
 * Function performed by the resource
 * @created 10-Sep-2013 19:43:04
 */
@XmlRootElement(name = "CI_OnLineFunctionCode", namespace = "http://www.isotc211.org/2005/gmd")
@Embeddable 
public class CI_OnLineFunctionCode extends CodeListValue 
{
	/**
	 * Online instructions provide the information necessary to acquire data
	 */
	public static final CI_OnLineFunctionCode DOWNLOAD = new CI_OnLineFunctionCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_OnLineFunctionCode", "download", "download");
	/**
	 * Online instructions provide more information about the data
	 */
	public static final CI_OnLineFunctionCode INFORMATION = new CI_OnLineFunctionCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_OnLineFunctionCode", "information", "information");
	/**
	 * Online instructions provide the ability to transfer data from one storage
	 * device or system to another
	 */
	public static final CI_OnLineFunctionCode OFFLINEACCESS = new CI_OnLineFunctionCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_OnLineFunctionCode", "offlineAccess", "offlineAccess");
	/**
	 * Online instructions provide the ability to acquire data
	 */
	public static final CI_OnLineFunctionCode ORDER = new CI_OnLineFunctionCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_OnLineFunctionCode", "order", "order");
	/**
	 * Online instructions provide the ability to seek out information about a dataset
	 */
	public static final CI_OnLineFunctionCode SEARCH = new CI_OnLineFunctionCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_OnLineFunctionCode", "search", "search");

	protected CI_OnLineFunctionCode() { }
	
	public CI_OnLineFunctionCode(String codeList, String codeListValue, String code) {
		super(codeList, codeListValue, code, new QName("http://www.isotc211.org/2005/gmd", "CI_OnLineFunctionCode", "gmd")); 
	}

}//end CI_OnLineFunctionCode