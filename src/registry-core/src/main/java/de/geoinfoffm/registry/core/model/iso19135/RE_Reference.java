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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.ValueObject;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;


/**
 * 
 * @created 09-Sep-2013 19:13:26
 */
@Access(AccessType.FIELD)
@Audited @Entity
public class RE_Reference extends de.geoinfoffm.registry.core.Entity
{
	@AttributeOverride(name = "value", column = @Column(name = "itemIdentifierAtSource", length = 2000))
	@Embedded
	public CharacterString itemIdentifierAtSource;

	@XmlElement(name = "similarity", namespace = "http://www.isotc211.org/2005/grg")
	@AttributeOverrides({
		@AttributeOverride(name = "codeList", column = @Column(name = "similarity_codelist")),
		@AttributeOverride(name = "codeListValue", column = @Column(name = "similarity_codelistvalue")),
		@AttributeOverride(name = "codeSpace", column = @Column(name = "similarity_codespace")),
		@AttributeOverride(name = "value", column = @Column(name = "similarity_value")),
		@AttributeOverride(name = "qname", column = @Column(name = "similarity_qname")),
	})
	public RE_SimilarityToSource similarity;

	@AttributeOverride(name = "value", column = @Column(name = "referenceText", length = 2000))
	@Embedded
	public CharacterString referenceText;

	@AttributeOverride(name = "value", column = @Column(name = "notes", length = 2000))
	@Embedded
	public CharacterString notes;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "text.value", column = @Column(name = "sourceCitation_text")),
		@AttributeOverride(name = "citation", column = @Column(name = "sourceCitation_citation"))
	})
	public RE_ReferenceSource sourceCitation;

	public RE_Reference() {

	}

	/**
	 * @return the itemIdentifierAtSource
	 */
	public CharacterString getItemIdentifierAtSource() {
		return itemIdentifierAtSource;
	}

	/**
	 * @param itemIdentifierAtSource the itemIdentifierAtSource to set
	 */
	public void setItemIdentifierAtSource(CharacterString itemIdentifierAtSource) {
		this.itemIdentifierAtSource = itemIdentifierAtSource;
	}

	/**
	 * @return the similarity
	 */
	public RE_SimilarityToSource getSimilarity() {
		return similarity;
	}

	/**
	 * @param similarity the similarity to set
	 */
	public void setSimilarity(RE_SimilarityToSource similarity) {
		this.similarity = similarity;
	}

	/**
	 * @return the referenceText
	 */
	public CharacterString getReferenceText() {
		return referenceText;
	}

	/**
	 * @param referenceText the referenceText to set
	 */
	public void setReferenceText(CharacterString referenceText) {
		this.referenceText = referenceText;
	}

	/**
	 * @return the notes
	 */
	public CharacterString getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(CharacterString notes) {
		this.notes = notes;
	}

	/**
	 * @return the sourceCitation
	 */
	public RE_ReferenceSource getSourceCitation() {
		return sourceCitation;
	}

	/**
	 * @param sourceCitation the sourceCitation to set
	 */
	public void setSourceCitation(RE_ReferenceSource sourceCitation) {
		this.sourceCitation = sourceCitation;
	}

	public void finalize() throws Throwable {

	}
}//end RE_Reference