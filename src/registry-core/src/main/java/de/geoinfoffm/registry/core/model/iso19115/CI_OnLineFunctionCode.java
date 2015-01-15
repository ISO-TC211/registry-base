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