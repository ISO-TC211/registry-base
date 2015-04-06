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
package de.geoinfoffm.registry.core.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Sid;

@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Audited @Entity
public abstract class Actor extends de.geoinfoffm.registry.core.Entity
{
	@OneToMany(mappedBy = "actor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Authorization> authorizations = new HashSet<Authorization>();

	protected Actor() { }
	
	public Actor(UUID uuid) {
		super(uuid);
	}
	
	/**
	 * @return the authorizations
	 */
	public Set<Authorization> getAuthorizations() {
		return authorizations;
	}

	/**
	 * @param authorizations the authorizations to set
	 */
	public void setAuthorizations(Set<Authorization> authorizations) {
		this.authorizations = authorizations;
	}
	
	public void assignRole(Role role) {
		Authorization auth = new Authorization(this, role);
		this.addAuthorization(auth);
	}
	
	public void revokeRole(Role role) {
		Authorization auth = new Authorization(this, role);
		this.removeAuthorization(auth);
	}
	
	public boolean hasRole(Role role) {
		for (Authorization auth : this.getAuthorizations()) {
			if (auth.getRole().equals(role)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean hasRole(String roleName) {
		for (Authorization auth : this.getAuthorizations()) {
			if (auth.getRole().getName().equals(roleName)) {
				return true;
			}
		}
		
		return false;		
	}
	
	public void addAuthorization(Authorization authorization) {
		if (this.authorizations == null) {
			this.authorizations = new HashSet<Authorization>();
		}
		
		authorization.setActor(this);
		this.authorizations.add(authorization);
	}
	
	public void removeAuthorization(Authorization authorization) {
		if (this.authorizations == null) {
			return;
		}
		
		authorization.setActor(null);
		this.authorizations.remove(authorization);
	}
	
	public Sid getSid() {
		return new PrincipalSid(this.getUuid().toString());
	}

	public List<Role> getRoles() {
		List<Role> result = new ArrayList<Role>();
		for (Authorization auth : this.getAuthorizations()) {
			result.add(auth.getRole());
		}
		
		return result;
	}
	
	public abstract String getName();
}
