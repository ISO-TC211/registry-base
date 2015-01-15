package de.geoinfoffm.registry.core.security;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Access(AccessType.FIELD)
@Table(name = "acl_class")
@Audited @Entity
public class AclClass extends de.geoinfoffm.registry.core.Entity
{
	@Column(name = "class", unique = true, columnDefinition = "text")
	private String aclClass;
	
	public AclClass() {
		// TODO Auto-generated constructor stub
	}

	public String getAclClass() {
		return aclClass;
	}
}
