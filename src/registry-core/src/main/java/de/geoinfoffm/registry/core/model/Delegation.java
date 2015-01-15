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
