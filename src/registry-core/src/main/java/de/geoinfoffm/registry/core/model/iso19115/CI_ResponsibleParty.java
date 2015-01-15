package de.geoinfoffm.registry.core.model.iso19115;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * Identification of, and means of communication with, person(s) and organisations
 * associated with the dataset.<br>
 * <br>
 * Objects of this class are entities with a <code>uuid</code>.
 */
@XmlType(name = "CI_ResponsibleParty_Type", 
	     namespace = "http://www.isotc211.org/2005/gmd",
	     propOrder = { "individualName", "organisationName", "positionName", "contactInfo", "role" })
@XmlRootElement(name = "CI_ResponsibleParty", namespace = "http://www.isotc211.org/2005/gmd")
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@Audited @Entity 
public class CI_ResponsibleParty extends de.geoinfoffm.registry.core.Entity
{
	private static final long serialVersionUID = 6893523212825507175L;

	/**
	 * Name of the responsible person- SURNAME, given name, title separated by a
	 * delimiter
	 */
	@XmlElement(name = "individualName", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String individualName;
	/**
	 * Name of the responsible organisation
	 */
	@XmlElement(name = "organisationName", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String organisationName;
	/**
	 * Role or position of the responsible person
	 */
	@XmlElement(name = "positionName", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String positionName;
	/**
	 * Address of the responsible party.
	 * @see #getContactInfo()
	 */
	@XmlElement(name = "contactInfo", namespace = "http://www.isotc211.org/2005/gmd")
	@OneToOne
	private CI_Contact contactInfo;
	/**
	 * Function performed by the responsible party
	 */
	@XmlElement(name = "role", namespace = "http://www.isotc211.org/2005/gmd")
	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "role", length = 2000))
	private CI_RoleCode role;

	protected CI_ResponsibleParty() {
	}
	
	/**
	 * Constrcuts a <code>CI_ResponsibleParty</code> object with the given details.<br>
	 * At least one of the parameters must be non-null and non-empty.
	 * 
	 * @param individualName Name of the person.
	 * @param organisationName Name of the organisation.
	 * @param positionName Name of the position inside the organisation.
	 */
	public CI_ResponsibleParty(String individualName, String organisationName, String positionName, CI_RoleCode role) {
		this.individualName = individualName;
		this.organisationName = organisationName;
		this.positionName = positionName;
		this.role = role;
		
		if (role == null) {
			throw new IllegalArgumentException("role must not be null");
		}
		
		if (!validate()) {
			throw new RuntimeException("CI_ResponsibleParty constraint violation: at least one of the fields "
					+ "'individualName', 'organisationName', and 'positionName' must be non-empty.");
		}
	}

	/**
	 * Check constraint that at least one of the attributes
	 * individualName, organisationName, and positionName
	 * must not be empty.
	 * 
	 * @return true if valid, false otherwise
	 */
	protected boolean validate() {
		return (this.individualName != null && !this.individualName.isEmpty()) 
			|| (this.organisationName != null && !this.organisationName.isEmpty()) 
			|| (this.positionName != null && !this.positionName.isEmpty());
	}
	
	/**
	 * @return The name of the person represented by this object, or null.
	 */
	public String getIndividualName() {
		return individualName;
	}

	/**
	 * Set the name of the person represented by this object.<br>
	 * <br>
	 * Calling this method with a <code>null</code> argument or an empty {@link CharacterString}
	 * leads to a {@link RuntimeException} if both <code>organisationName</code> and
	 * <code>positionName</code> are <code>null</code> as well.
	 * 
	 * @param newName New individual name.
	 */
	public void setIndividualName(String newName) {
		String oldName = this.individualName;
		this.individualName = newName;
		
//		if (!validate() && !isLoadingFromPersistentStorage()) {
//			this.individualName = oldName;
//			throw new RuntimeException("CI_ResponsibleParty constraint violation: at least one of the fields "
//					+ "'individualName', 'organisationName', and 'positionName' must be non-empty.");
//		}
	}

	/**
	 * @return The name of the organisation, or null.
	 */
	public String getOrganisationName() {
		return organisationName;
	}
	
	/**
	 * Set the name of the organisation.<br>
	 * <br>
	 * Calling this method with a <code>null</code> argument or an empty {@link CharacterString}
	 * leads to a {@link RuntimeException} if both <code>individualName</code> and
	 * <code>positionName</code> are <code>null</code> as well.
	 * 
	 * @param newName New organisation name.
	 */
	public void setOrganisationName(String newName) {
		String oldName = this.organisationName;
		this.organisationName = newName;
		
//		if (!validate() && !isLoadingFromPersistentStorage()) {
//			this.organisationName = oldName;
//			throw new RuntimeException("CI_ResponsibleParty constraint violation: at least one of the fields "
//					+ "'individualName', 'organisationName', and 'positionName' must be non-empty.");
//		}
	}

	/**
	 * @return The name of the position, or null.
	 */
	public String getPositionName() {
		return positionName;
	}

	/**
	 * Set the name of the position.<br>
	 * <br>
	 * Calling this method with a <code>null</code> argument or an empty {@link CharacterString}
	 * leads to a {@link RuntimeException} if both <code>organisationName</code> and
	 * <code>individualName</code> are <code>null</code> as well.
	 * 
	 * @param newName New position name.
	 */
	public void setPositionName(String newName) {
		String oldName = this.positionName;
		this.positionName = newName;
		
//		if (!validate() && !isLoadingFromPersistentStorage()) {
//			this.positionName = oldName;
//			throw new RuntimeException("CI_ResponsibleParty constraint violation: at least one of the fields "
//					+ "'individualName', 'organisationName', and 'positionName' must be non-empty.");
//		}
	}

	/**
	 * @return Contact information for the entity represented by this object.
	 */
	public CI_Contact getContactInfo() {
		return contactInfo;
	}
	
	/**
	 * Set the contact information for this entity.
	 * 
	 * @param contactInfo Contact information to set or null.
	 */
	public void setContactInfo(CI_Contact contactInfo) {
//		if (contactInfo == null) {
//			throw new IllegalArgumentException("CI_ReponsibleParty constraint violation: contactInfo "
//					+ "must not be empty");
//		}
//		
		this.contactInfo = contactInfo;
	}
	
	@Transient // Persistence is handled by getRoleXml()!
	public CI_RoleCode getRole() {
		return role;
	}
	
	public void setRole(CI_RoleCode role) {
		this.role = role;
	}
	
}// end CI_ResponsibleParty