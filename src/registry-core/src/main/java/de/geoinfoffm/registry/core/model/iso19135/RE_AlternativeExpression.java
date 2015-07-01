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
package de.geoinfoffm.registry.core.model.iso19135;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;


/**
 * 
 * @created 09-Sep-2013 19:11:38
 */
@Access(AccessType.FIELD)
@Audited @Entity
public class RE_AlternativeExpression extends de.geoinfoffm.registry.core.Entity 
{
	@AttributeOverride(name = "value", column = @Column(name = "name", length = 2000))
	@Embedded
	private CharacterString name;

	@AttributeOverride(name = "value", column = @Column(name = "definition", length = 2000))
	@Embedded
	private CharacterString definition;

	@AttributeOverride(name = "value", column = @Column(name = "description", length = 2000))
	@Embedded
	private CharacterString description;

	@ElementCollection
	@AttributeOverrides({
		@AttributeOverride(name = "name", column = @Column(name = "fieldOfApplication_name")),
		@AttributeOverride(name = "description", column = @Column(name = "fieldOfApplication_description"))
	})
	private Set<RE_FieldOfApplication> fieldOfApplication;

	@OneToOne
	private RE_Locale locale;

	public RE_AlternativeExpression() {

	}

	/**
	 * @return the name
	 */
	public CharacterString getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(CharacterString name) {
		this.name = name;
	}

	/**
	 * @return the definition
	 */
	public CharacterString getDefinition() {
		return definition;
	}

	/**
	 * @param definition the definition to set
	 */
	public void setDefinition(CharacterString definition) {
		this.definition = definition;
	}

	/**
	 * @return the description
	 */
	public CharacterString getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(CharacterString description) {
		this.description = description;
	}

	/**
	 * @return the fieldOfApplication
	 */
	public Set<RE_FieldOfApplication> getFieldOfApplication() {
		if (fieldOfApplication == null) {
			fieldOfApplication = new HashSet<>();
		}
		return fieldOfApplication;
	}

	/**
	 * @param fieldOfApplication the fieldOfApplication to set
	 */
	protected void setFieldOfApplication(Set<RE_FieldOfApplication> fieldOfApplication) {
		this.fieldOfApplication = fieldOfApplication;
	}

	/**
	 * @return the locale
	 */
	public RE_Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(RE_Locale locale) {
		this.locale = locale;
	}

	public void finalize() throws Throwable {

	}
}//end RE_AlternativeExpression