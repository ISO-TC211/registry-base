package de.geoinfoffm.registry.core.security;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

@Access(AccessType.FIELD)
@Table(name = "acl_entry",
	   uniqueConstraints = @UniqueConstraint(columnNames = { "acl_object_identity", "ace_order" }))
@Audited @Entity
public class AclEntry extends de.geoinfoffm.registry.core.Entity
{
//	@Column(name = "acl_object_identity")
//	@Basic(optional = false)
//	@Type(type = "pg-uuid")	
//	private UUID acl_object_identity;
	@ManyToOne
	@JoinColumn(name = "acl_object_identity")
	private AclObjectIdentity aclObjectIdentity;
	
	@Column(name = "ace_order")
	@Basic(optional = false)
	private int aceOrder;
	
//	@Basic(optional = false)
//	@Type(type = "pg-uuid")	
//	private UUID sid;
	@ManyToOne
	@JoinColumn(name = "sid")
	private AclSid sid;

	@Basic(optional = false)
	private int mask;
	
	@Basic(optional = false)
	private boolean granting;
	
	@Column(name = "audit_success")
	@Basic(optional = false)
	private boolean auditSuccess;

	@Column(name = "audit_failure")
	@Basic(optional = false)
	private boolean auditFailure;
	

	public AclObjectIdentity getAclObjectIdentity() {
		return aclObjectIdentity;
	}


	public int getAceOrder() {
		return aceOrder;
	}


	public AclSid getSid() {
		return sid;
	}


	public int getMask() {
		return mask;
	}


	public boolean isGranting() {
		return granting;
	}


	public boolean isAuditSuccess() {
		return auditSuccess;
	}


	public boolean isAuditFailure() {
		return auditFailure;
	}


	public AclEntry() {
		// TODO Auto-generated constructor stub
	}

}
