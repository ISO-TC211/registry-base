package de.geoinfoffm.registry.core.model.iso19135;

import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_ResponsibleParty;


/**
 * 
 * @created 09-Sep-2013 19:14:04
 */
@XmlType(name = "RE_RegisterManager_Type", namespace = "http://www.isotc211.org/2005/grg",
		 propOrder = { "name", "contact" })
@XmlRootElement(name = "RE_RegisterManager", namespace = "http://www.isotc211.org/2005/grg")
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@Audited @Entity
public class RE_RegisterManager extends de.geoinfoffm.registry.core.Entity
{
	@XmlElement(name = "name", namespace = "http://www.isotc211.org/2005/grg", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String name;
	
	@XmlElement(name = "contact", namespace = "http://www.isotc211.org/2005/grg")
	@ManyToOne(cascade = CascadeType.PERSIST)
	private CI_ResponsibleParty contact;
	
	@XmlTransient
	@OneToMany(mappedBy = "manager")
	private Set<RE_Register> managedRegisters;

	public RE_RegisterManager() {

	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the contact
	 */
	public CI_ResponsibleParty getContact() {
		return contact;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(CI_ResponsibleParty contact) {
		this.contact = contact;
	}

	/**
	 * @return the managedRegisters
	 */
	public Set<RE_Register> getManagedRegisters() {
		return managedRegisters;
	}

	/**
	 * @param managedRegisters the managedRegisters to set
	 */
	public void setManagedRegisters(Set<RE_Register> managedRegisters) {
		this.managedRegisters = managedRegisters;
	}

	public void finalize() throws Throwable {

	}
}//end RE_RegisterManager