package de.geoinfoffm.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.envers.Audited;

@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Audited @Entity
public class RegistryUserGroup extends Role
{

	protected RegistryUserGroup() {
		// TODO Auto-generated constructor stub
	}

	public RegistryUserGroup(String groupName) {
		super(groupName);
	}

}
