package de.geoinfoffm.registry.core.model.iso00639;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

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
@Embeddable 
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