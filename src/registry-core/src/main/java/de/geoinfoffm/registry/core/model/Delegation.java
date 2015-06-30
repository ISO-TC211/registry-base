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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Access(AccessType.FIELD)
@Audited @Entity
public class Delegation extends Authorization
{
	@ManyToOne(optional = false)
	private Organization delegatingOrganization;

	private boolean isApproved = false;
	
	protected Delegation() {
		super();
	}

	public Delegation(RegistryUser user, Role authorizedRole, Organization delegatingOrganization) {
		super(user, authorizedRole);
		this.delegatingOrganization = delegatingOrganization;
	}

	public Organization getDelegatingOrganization() {
		return delegatingOrganization;
	}

	public void setDelegatingOrganization(Organization delegatingOrganization) {
		this.delegatingOrganization = delegatingOrganization;
	}
	
	public boolean isSame(Delegation other) {
		return this.delegatingOrganization.equals(other.getDelegatingOrganization())
			&& this.getUser().equals(other.getUser())
			&& this.getRole().equals(other.getRole());
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	@Override
	public String toString() {
		String orgName = (this.getDelegatingOrganization() != null ? this.getDelegatingOrganization().getName() : "(null)");
		String roleName = (this.getRole() != null ? this.getRole().getName() : "(null)");
		String userName = (this.getUser() != null ? this.getUser().getName() : "(null)");
		return String.format("%s [%s delegates %s to %s, approved = %s]", this.getClass().getCanonicalName(), orgName, roleName, userName, this.isApproved);
	}
	
	
}
