package de.geoinfoffm.registry.core.model.iso19135;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;

/**
 * 
 * @created 09-Sep-2013 19:12:51
 */
@XmlType(name = "RE_ItemClass_Type", 
		 namespace = "http://www.isotc211.org/2005/grg",
		 propOrder = { "name", "technicalStandard", "alternativeNames", "describedItem" })
@XmlRootElement(name = "RE_ItemClass", namespace = "http://www.isotc211.org/2005/grg")
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@Audited @Entity
public class RE_ItemClass extends de.geoinfoffm.registry.core.Entity
{

	@XmlElement(name = "name", namespace = "http://www.isotc211.org/2005/grg", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String name;

	@XmlElement(name = "technicalStandard", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("technicalStandard/gmd:CI_Citation")
	@OneToOne
	private CI_Citation technicalStandard;

	@XmlElement(name = "alternativeNames", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("alternativeNames/grg:RE_AlternativeName")
	@ElementCollection
	@AttributeOverrides({
		@AttributeOverride(name = "name", column = @Column(name ="alternativeName_name"))
	})
	private Set<RE_AlternativeName> alternativeNames;

	@JsonBackReference
	@XmlTransient
	@ManyToMany
	@JoinTable(name = "RE_Register_ItemClasses",
	   		   joinColumns = @JoinColumn(name="registerId"),
	   		   inverseJoinColumns = @JoinColumn(name="itemClassId")
	)
	private Set<RE_Register> registers;
	
	@XmlElement(name = "describedItem", namespace = "http://www.isotc211.org/2005/grg")
	@XmlPath("describedItem/grg:RE_RegisterItem")
	@XmlInverseReference(mappedBy = "itemClass")
	@OneToMany(mappedBy = "itemClass")
	private Set<RE_RegisterItem> describedItem;
	
	public RE_ItemClass() {

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
	 * @return the technicalStandard
	 */
	public CI_Citation getTechnicalStandard() {
		return technicalStandard;
	}

	/**
	 * @param technicalStandard the technicalStandard to set
	 */
	public void setTechnicalStandard(CI_Citation technicalStandard) {
		this.technicalStandard = technicalStandard;
	}

	/**
	 * @return the alternativeLanguages
	 */
	public Set<RE_AlternativeName> getAlternativeNames() {
		return alternativeNames;
	}

	/**
	 * @param alternativeLanguages the alternativeLanguages to set
	 */
	public void setAlternativeNames(Set<RE_AlternativeName> alternativeNames) {
		this.alternativeNames = alternativeNames;
	}

	/**
	 * @return the registers
	 */
	public Set<RE_Register> getRegisters() {
		return registers;
	}

	/**
	 * @param registers the registers to set
	 */
	public void setRegisters(Set<RE_Register> registers) {
		this.registers = registers;
	}
	
	public void addRegister(RE_Register register) {
		if (this.registers == null) {
			this.registers = new HashSet<RE_Register>();
		}
		this.registers.add(register);
	}

	public Set<RE_RegisterItem> getDescribedItem() {
		return describedItem;
	}

	public void setDescribedItem(Set<RE_RegisterItem> describedItem) {
		this.describedItem = describedItem;
	}

	public void finalize() throws Throwable {

	}
}//end RE_ItemClass