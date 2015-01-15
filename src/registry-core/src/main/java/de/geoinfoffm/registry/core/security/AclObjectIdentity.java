package de.geoinfoffm.registry.core.security;

import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

@Access(AccessType.FIELD)
@Table(name = "acl_object_identity",
	   uniqueConstraints = @UniqueConstraint(columnNames = { "object_id_class", "object_id_identity" }))
@Audited @Entity
public class AclObjectIdentity extends de.geoinfoffm.registry.core.Entity
{
//	@Column(name = "object_id_class")
//	@Basic(optional = false)
//	@Type(type = "pg-uuid")	
//	private UUID objectIdClass;
	@ManyToOne
	@JoinColumn(name = "object_id_class")
	private AclClass objectIdClass;
	
	@Column(name = "object_id_identity")
	@Basic(optional = false)
	@Type(type = "pg-uuid")	
	private UUID objectIdIdentity;
	
//	@Column(name = "parent_object")
//	@Basic(optional = true)
//	@Type(type = "pg-uuid")	
//	private UUID parentObject;
	@ManyToOne
	@JoinColumn(name = "parent_object")
	private AclObjectIdentity parent;
	
//	@Column(name = "owner_sid")
//	@Basic(optional = true)
//	@Type(type = "pg-uuid")	
//	private UUID ownerSid;
	@ManyToOne
	@JoinColumn(name = "owner_sid")
	private AclSid ownerSid;
	
	@Column(name = "entries_inheriting")
	@Basic(optional = false)
	private boolean entriesInheriting;

	public AclObjectIdentity() {
		// TODO Auto-generated constructor stub
	}

	public AclClass getObjectIdClass() {
		return objectIdClass;
	}

	public UUID getObjectIdIdentity() {
		return objectIdIdentity;
	}

	public AclObjectIdentity getParent() {
		return parent;
	}

	public AclSid getOwnerSid() {
		return ownerSid;
	}

	public boolean isEntriesInheriting() {
		return entriesInheriting;
	}


}
