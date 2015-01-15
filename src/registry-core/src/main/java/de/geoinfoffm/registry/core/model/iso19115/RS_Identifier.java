package de.geoinfoffm.registry.core.model.iso19115;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;

/**
 * An identification of a CRS object. The first use of a XX_RS_Identifier for an
 * object, if any, is normally the primary identification code, and any others
 * are aliases.
 * 
 * @created 10-Sep-2013 20:16:25
 */
@Access(AccessType.FIELD)
@Embeddable
public final class RS_Identifier extends MD_Identifier
{

	/**
	 * Identifier of a code space within which one or more codes are defined.
	 * This code space is optional but is normally included. This code space is
	 * often defined by some authority organization, where one organization may
	 * define multiple code spaces. The range and format of each Code Space
	 * identifier is defined by that code space authority.
	 */
	public String codeSpace;
	/**
	 * Identifier of the version of the associated codeSpace or code, as
	 * specified by the codeSpace or code authority. This version is included
	 * only when the "code" or "codeSpace" uses versions. When appropriate, the
	 * version is identified by the effective date, coded using ISO 8601 date
	 * format.
	 */
	public String version;
	
	public RS_Identifier(String code, String codeSpace, CI_Citation authority, String version) {
		super(code, authority);
		
		this.codeSpace = codeSpace;
		this.version = version;
	}

	public String getCodeSpace() {
		return codeSpace;
	}

	public void setCodeSpace(String codeSpace) {
		this.codeSpace = codeSpace;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}// end RS_Identifier