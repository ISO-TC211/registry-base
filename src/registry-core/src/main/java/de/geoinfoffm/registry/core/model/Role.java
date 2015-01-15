package de.geoinfoffm.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.envers.Audited;

/**
 * A role assumed by a {@link RegistryUser} after {@link Authorization}
 * by an {@link Organization}
 * 
 * @author Florian Esser
 *
 */
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Audited @Entity
public class Role extends de.geoinfoffm.registry.core.Entity 
{
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 7918942525527566375L;
	
	@Column(unique = true, columnDefinition = "text")
	private String name;
	
	protected Role() { }
	
	public Role(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}
}
