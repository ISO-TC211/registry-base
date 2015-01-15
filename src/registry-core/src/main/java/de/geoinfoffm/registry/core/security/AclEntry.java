/**
 * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *     * Redistributions of source code must retain the above copyright
 *     	 notice, this list of conditions and the following disclaimer.

 *     * Redistributions in binary form must reproduce the above
 *     	 copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials
 *       provided with the distribution.

 *     * The names "German Federal Agency for Cartography and Geodesy",
 *       "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE",
 *       "GDI-DE Registry" and the names of other contributors must not
 *       be used to endorse or promote products derived from this
 *       software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE GERMAN
 * FEDERAL AGENCY FOR CARTOGRAPHY AND GEODESY BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
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
