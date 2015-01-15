package de.geoinfoffm.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

@Access(AccessType.FIELD)
@Audited @Entity
public class OrganizationRelatedRole extends Role
{
	@ManyToOne(optional = false)
	private Organization organization;

	protected OrganizationRelatedRole() {
		super();
	}

	public OrganizationRelatedRole(String name, Organization organization) {
		super(name);
		
		this.setOrganization(organization);
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
