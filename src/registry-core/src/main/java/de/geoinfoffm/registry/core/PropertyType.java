package de.geoinfoffm.registry.core;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_OnlineResource;
import de.geoinfoffm.registry.core.model.iso19135.RE_AlternativeName;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Locale;
import de.geoinfoffm.registry.core.model.iso19135.RE_ReferenceSource;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterManager;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterOwner;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.model.iso19135.RE_Version;

@XmlType
public class PropertyType<R>
{
	@XmlElementRefs({
		@XmlElementRef(type = CharacterString.class),
		@XmlElementRef(type = RE_AlternativeName.class),
		@XmlElementRef(type = RE_RegisterManager.class),
		@XmlElementRef(type = CI_OnlineResource.class),
		@XmlElementRef(type = RE_Locale.class), 
		@XmlElementRef(type = RE_Version.class),
		@XmlElementRef(type = RE_RegisterItem.class), 
		@XmlElementRef(type = RE_RegisterManager.class), 
		@XmlElementRef(type = RE_AlternativeName.class), 
		@XmlElementRef(type = RE_ReferenceSource.class), 
		@XmlElementRef(type = RE_ItemClass.class), 
		@XmlElementRef(type = RE_SubmittingOrganization.class), 
		@XmlElementRef(type = RE_RegisterOwner.class)
	})
//	@XmlElementRef
	private R ref;
	
	public PropertyType() { }
	
	public PropertyType(R ref) {
		this.setRef(ref);
	}

	public R getRef() {
		return ref;
	}

	public void setRef(R ref) {
		this.ref = ref;
	}
}
