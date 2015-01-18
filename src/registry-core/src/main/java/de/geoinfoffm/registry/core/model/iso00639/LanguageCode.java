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
package de.geoinfoffm.registry.core.model.iso00639;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19103.CodeListValue;

/**
 * <UsedBy>
 * <NameSpace>ISO 19135 Procedures for registration</NameSpace>
 * <Class>RE_Locale</Class>
 * <Attribute>language</Attribute>
 * <Type>LanguageCode</Type>
 * <UsedBy>
 * @created 11-Sep-2013 09:15:33
 */
@XmlRootElement(name = "LanguageCode", namespace = "http://www.isotc211.org/2005/gmd")
@Audited @Embeddable 
public class LanguageCode extends CodeListValue 
{

//	Afrikaans,
//	Albanian,
//	Arabic,
//	Basque,
//	Belarusian,
//	Bulgarian,
//	Catalan,
//	Chinese,
//	Croatian,
//	Czech,
//	Danish,
//	Dutch,
//	English,
//	Estonian,
//	Faeroese,
//	French,
//	French_Canadian,
//	Finnish,
//	German,
//	Greek,
//	Hawaian,
//	Hebrew,
//	Hungarian,
//	Icelandic,
//	Indonesian,
//	Italian,
//	Japanese,
//	Korean,
//	Latvian,
//	Lithuanian,
//	Malaysian,
//	Norwegian,
//	Polish,
//	Portuguese,
//	Romanian,
//	Russian,
//	Serbian,
//	Slovak,
//	Slovenian,
//	Spanish,
//	Swahili,
//	Swedish,
//	Turkish,
//	Ukranian;
	
	protected LanguageCode() { }
	
	public LanguageCode(String codeList, String codeListValue, String code) {
		super(codeList, codeListValue, code, new QName("http://www.isotc211.org/2005/gmd", "LanguageCode", "gmd")); 
	}

}//end LanguageCode