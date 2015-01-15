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
@Inheritance(strategy = InheritanceType.JOINED)
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
