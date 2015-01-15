package de.geoinfoffm.registry.core.model.iso19135;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import de.geoinfoffm.registry.core.ValueObject;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * 
 * @created 09-Sep-2013 19:12:41
 */
@Access(AccessType.FIELD)
@Embeddable
public class RE_FieldOfApplication extends ValueObject
{
	@AttributeOverride(name = "value", column = @Column(name = "name", length = 2000))
	@Embedded
	private CharacterString name;

	@AttributeOverride(name = "value", column = @Column(name = "description", length = 2000))
	@Embedded
	private CharacterString description;

	public RE_FieldOfApplication() {

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

	public void finalize() throws Throwable {

	}
}//end RE_FieldOfApplication