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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.ValueObject;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;


/**
 * Telephone numbers for contacting the responsible individual or organisation
 * @created 10-Sep-2013 19:43:51
 */
@Access(AccessType.FIELD)
@Audited @Entity
public class CI_Telephone extends de.geoinfoffm.registry.core.Entity
{
	/**
	 * Telephone number by which individuals can speak to the responsible organisation
	 * or individual
	 */
	@ElementCollection
	@AttributeOverride(name = "value", column = @Column(name = "telephone_voice", length = 255))
	private final Set<CharacterString> voice = new HashSet<CharacterString>();
	/**
	 * Telephone number of a facsimile machine for the responsible organisation or
	 * individual
	 */
	@ElementCollection
	@AttributeOverride(name = "value", column = @Column(name = "telephone_fax", length = 255))
	private final Set<CharacterString> facsimile = new HashSet<CharacterString>();

	protected CI_Telephone() {

	}
	
	public CI_Telephone(Set<CharacterString> voices, Set<CharacterString> facsimiles) {
		this.voice.addAll(voices);
		this.facsimile.addAll(facsimiles);
	}
	
	public Set<CharacterString> getVoice() {
		return Collections.unmodifiableSet(voice);
	}
	
	protected void setVoice(Set<CharacterString> voice) {
		this.voice.clear();
		this.voice.addAll(voice);
	}
	
	public Set<CharacterString> getFacsimile() {
		return Collections.unmodifiableSet(facsimile);
	}
	
	protected void setFacsimile(Set<CharacterString> facsimile) {
		this.facsimile.clear();
		this.facsimile.addAll(facsimile);
	}
	
	public void finalize() throws Throwable {

	}
}//end CI_Telephone