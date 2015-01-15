package de.geoinfoffm.registry.core.model.iso19115;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import de.geoinfoffm.registry.core.ValueObject;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;


/**
 * 
 * @created 10-Sep-2013 19:43:44
 */
@Access(AccessType.FIELD)
@Embeddable
public class CI_Series extends ValueObject
{
	@AttributeOverride(name = "value", column = @Column(name = "name", length = 2000))
	private CharacterString name;

	@AttributeOverride(name = "value", column = @Column(name = "issueIdentification", length = 2000))
	private CharacterString issueIdentification;

	@AttributeOverride(name = "value", column = @Column(name = "page", length = 2000))
	private CharacterString page;

	public CI_Series(){

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
	 * @return the issueIdentification
	 */
	public CharacterString getIssueIdentification() {
		return issueIdentification;
	}

	/**
	 * @param issueIdentification the issueIdentification to set
	 */
	public void setIssueIdentification(CharacterString issueIdentification) {
		this.issueIdentification = issueIdentification;
	}

	/**
	 * @return the page
	 */
	public CharacterString getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(CharacterString page) {
		this.page = page;
	}

	public void finalize() throws Throwable {

	}
}//end CI_Series