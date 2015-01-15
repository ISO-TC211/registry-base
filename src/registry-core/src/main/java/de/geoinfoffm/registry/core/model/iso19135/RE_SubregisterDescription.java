package de.geoinfoffm.registry.core.model.iso19135;

import de.geoinfoffm.registry.core.model.iso19115.CI_OnlineResource;


/**
 * 
 * @created 09-Sep-2013 19:14:37
 */
public class RE_SubregisterDescription extends RE_RegisterItem {

	public CI_OnlineResource uniformResourceIdentifier;
	public RE_Locale operatingLanguage;
	public RE_RegisterManager subregisterManager;
	public RE_ItemClass containedItemClass;

	public RE_SubregisterDescription(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
}//end RE_SubregisterDescription