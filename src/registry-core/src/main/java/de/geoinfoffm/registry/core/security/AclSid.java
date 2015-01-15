package de.geoinfoffm.registry.core.security;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

@Access(AccessType.FIELD)
@Table(name = "acl_sid", uniqueConstraints = @UniqueConstraint(columnNames = { "sid", "principal" }))
@Audited @Entity
public class AclSid extends de.geoinfoffm.registry.core.Entity
{
	
	@Basic(optional = false)
	private boolean principal;
	
	@Column(length = 100)
	private String sid;
	
	public AclSid() {
		// TODO Auto-generated constructor stub
	}

	public String getSid() {
		return sid;
	}
}
