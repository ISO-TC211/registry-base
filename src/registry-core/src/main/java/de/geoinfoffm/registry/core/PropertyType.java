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
