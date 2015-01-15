package de.geoinfoffm.registry.core.model.iso19135;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;

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