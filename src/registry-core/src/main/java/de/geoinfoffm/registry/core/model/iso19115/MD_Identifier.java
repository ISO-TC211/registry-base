package de.geoinfoffm.registry.core.model.iso19115;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

import de.geoinfoffm.registry.core.ValueObject;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;


/**
 * 
 * @created 10-Sep-2013 20:16:05
 */
@Access(AccessType.FIELD)
@Embeddable
public class MD_Identifier extends ValueObject
{
	/**
	 * Organization or party responsible for definition and maintenance of the code
	 * space or code. 
	 */
	@OneToOne
	private CI_Citation authority;
	/**
	 * Identifier code or name, often from a controlled list or pattern defined by a
	 * code space.
	 */
	@Column(columnDefinition = "text")
	private String code;
	
	public MD_Identifier(String code, CI_Citation authority) {
		this.code = code;
		this.authority = authority;
	}

	/**
	 * @return the authority
	 */
	public CI_Citation getAuthority() {
		return authority;
	}

	/**
	 * @param authority the authority to set
	 */
	public void setAuthority(CI_Citation authority) {
		this.authority = authority;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
}//end MD_Identifier