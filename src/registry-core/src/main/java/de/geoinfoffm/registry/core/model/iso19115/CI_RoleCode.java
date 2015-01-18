/**
 * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *     * Redistributions of source code must retain the above copyright
 *     	 notice, this list of conditions and the following disclaimer.

 *     * Redistributions in binary form must reproduce the above
 *     	 copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials
 *       provided with the distribution.

 *     * The names "German Federal Agency for Cartography and Geodesy",
 *       "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE",
 *       "GDI-DE Registry" and the names of other contributors must not
 *       be used to endorse or promote products derived from this
 *       software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE GERMAN
 * FEDERAL AGENCY FOR CARTOGRAPHY AND GEODESY BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.geoinfoffm.registry.core.model.iso19115;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19103.CodeListValue;

/**
 * Function performed by the responsible party
 */
@XmlRootElement(name = "CI_RoleCode", namespace = "http://www.isotc211.org/2005/gmd")
@Audited @Embeddable 
public class CI_RoleCode extends CodeListValue 
{
	private static final long serialVersionUID = -6784967061384825527L;

	/**
	 * Party that supplies the data
	 */
	public static final CI_RoleCode RESOURCE_PROVIDER = new CI_RoleCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode", "resourceProvider", "resourceProvider");

	/**
	 * Guardian or keeper responsible for maintaining the data
	 */
	public static final CI_RoleCode CUSTODIAN = new CI_RoleCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode", "custodian", "custodian");

	/**
	 * Person who owns the data
	 */
	public static final CI_RoleCode OWNER = new CI_RoleCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode", "owner", "owner");

	/**
	 * Person who uses the data
	 */
	public static final CI_RoleCode USER = new CI_RoleCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode", "user", "user");
	/**
	 * Person or organisation who distributes the data
	 */
	
	public static final CI_RoleCode DISTRIBUTOR = new CI_RoleCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode", "distributor", "distributor");

	/**
	 * Responsible party who created the dataset or metadata
	 */
	public static final CI_RoleCode ORIGINATOR = new CI_RoleCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode", "originator", "originator");
	
	/**
	 * Responsible party who can be contacted for acquiring knowledge about or
	 * acquisition of the data.
	 */
	public static final CI_RoleCode POINT_OF_CONTACT = new CI_RoleCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode", "pointOfContact", "pointOfContact");

	/**
	 * Key person responsible for gathering information and conducting research
	 */
	public static final CI_RoleCode PRINCIPAL_INVESTIGATOR = new CI_RoleCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode", "principalInvestigator", "principalInvestigator");

	/**
	 * Responsible party who has processed the data in a manner in which the data has
	 * been modified.
	 */
	public static final CI_RoleCode PROCESSOR = new CI_RoleCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode", "processor", "processor");

	/**
	 * Responsible party who published the data
	 */
	public static final CI_RoleCode PUBLISHER = new CI_RoleCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode", "publisher", "publisher");

	public static final CI_RoleCode AUTHOR = new CI_RoleCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#CI_RoleCode", "author", "author");
	
	protected CI_RoleCode() { }
	
	public CI_RoleCode(String codeList, String codeListValue, String code) {
		super(codeList, codeListValue, code, new QName("http://www.isotc211.org/2005/gmd", "CI_RoleCode", "gmd")); 
	}
}//end CI_RoleCode