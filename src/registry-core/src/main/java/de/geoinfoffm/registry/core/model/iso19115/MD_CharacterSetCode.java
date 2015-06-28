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
 * 
 * @created 11-Sep-2013 09:14:07
 */
@XmlRootElement(name = "MD_CharacterSetCode", namespace = "http://www.isotc211.org/2005/gmd")
@Audited @Embeddable 
public class MD_CharacterSetCode extends CodeListValue  
{
//	ucs2,
//	ucs4,
//	utf7,
//	utf8,
//	utf16,
//	_8859part1,
//	_8859part2,
//	_8859part3,
//	_8859part4,
//	_8859part5,
//	_8859part6,
//	_8859part7,
//	_8859part8,
//	_8859part9,
//	_8859part10,
//	_8859part11,
//	_8859part13,
//	_8859part14,
//	_8859part15,
//	_8859part16,
//	jis,
//	shiftJIS,
//	eucJP,
//	usAscii,
//	ebcdic,
//	eucKR,
//	big5,
//	GB2312;
	
	public static final MD_CharacterSetCode UTF8 = new MD_CharacterSetCode("http://www.isotc211.org/2005/resources/Codelist/gmxCodelists.xml#MD_CharacterSetCode", "utf8", "utf8");

	protected MD_CharacterSetCode() { }
	
	public MD_CharacterSetCode(String codeList, String codeListValue, String code) {
		super(codeList, codeListValue, code, new QName("http://www.isotc211.org/2005/gmd", "MD_CharacterSetCode", "gmd")); 
	}

}//end MD_CharacterSetCode