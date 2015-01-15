package de.geoinfoffm.registry.core.model.iso19115;

import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * Location of the responsible individual or organisation
 * @created 10-Sep-2013 19:42:17
 */
@Access(AccessType.FIELD)
@Audited @Entity
public class CI_Address extends de.geoinfoffm.registry.core.Entity
{
	private static final long serialVersionUID = 5913450286809443634L;

	/**
	 * Address line for the physical address (Street name, box number, suite)
	 */
	@ElementCollection
	@AttributeOverride(name = "value", column = @Column(name = "address_deliveryPoint", length = 2000))
	private Set<CharacterString> deliveryPoint;
	/**
	 * City of the physical address
	 */	
	@AttributeOverride(name = "value", column = @Column(name = "city", length = 2000))
	private CharacterString city;
	/**
	 * State, province of the physical address
	 */
	@AttributeOverride(name = "value", column = @Column(name = "administrativeArea", length = 2000))
	private CharacterString administrativeArea;
	/**
	 * ZIP or other postal code 
	 */
	@AttributeOverride(name = "value", column = @Column(name = "postalCode", length = 2000))
	private CharacterString postalCode;
	/**
	 * Country of the physical address
	 */
	@AttributeOverride(name = "value", column = @Column(name = "country", length = 2000))
	private CharacterString country;
	/**
	 * Address of the electronic mailbox of the responsible organisation or individual
	 */
	@ElementCollection
	@AttributeOverride(name = "value", column = @Column(name = "address_email", length = 2000))
	private Set<CharacterString> electronicMailAddress;

	protected CI_Address(){

	}

	public Set<CharacterString> getDeliveryPoint() {
		return deliveryPoint;
	}

	public void setDeliveryPoint(Set<CharacterString> deliveryPoint) {
		this.deliveryPoint = deliveryPoint;
	}

	public CharacterString getCity() {
		return city;
	}

	public void setCity(CharacterString city) {
		this.city = city;
	}

	public CharacterString getAdministrativeArea() {
		return administrativeArea;
	}

	public void setAdministrativeArea(CharacterString administrativeArea) {
		this.administrativeArea = administrativeArea;
	}

	public CharacterString getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(CharacterString postalCode) {
		this.postalCode = postalCode;
	}

	public CharacterString getCountry() {
		return country;
	}

	public void setCountry(CharacterString country) {
		this.country = country;
	}

	public Set<CharacterString> getElectronicMailAddress() {
		return electronicMailAddress;
	}

	public void setElectronicMailAddress(Set<CharacterString> electronicMailAddress) {
		this.electronicMailAddress = electronicMailAddress;
	}

	public void finalize() throws Throwable {

	}

}//end CI_Address