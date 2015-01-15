package de.geoinfoffm.registry.core.model.iso19135;

import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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
	@AttributeOverride(name = "value", column = @Column(name = "fieldOfApplication", length = 2000))
	private Set<CharacterString> fieldOfApplication;

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
	public Set<CharacterString> getFieldOfApplication() {
		return fieldOfApplication;
	}

	/**
	 * @param fieldOfApplication the fieldOfApplication to set
	 */
	public void setFieldOfApplication(Set<CharacterString> fieldOfApplication) {
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