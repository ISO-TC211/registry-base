package de.geoinfoffm.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

/**
 * Persistence class for {@link Organization}.<br>
 * <br>
 * Contains JPA annotations for object-relational mapping and JAXB annotations for
 * XML serialization.
 * 
 * @author Florian Esser
 *
 */
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Access(AccessType.FIELD)
@Table(name = "AuthorizationTable", uniqueConstraints = @UniqueConstraint(name = "PK_AuthorizationTable", columnNames = { "actor_uuid", "role_uuid" }))
@Audited @Entity
public class Authorization extends de.geoinfoffm.registry.core.Entity
{
	private static final long serialVersionUID = 722737579676492034L;

	@ManyToOne(optional = false)
	private Actor actor;
	
	@ManyToOne(optional = false)
	private Role role;
	
	protected Authorization() {
	
	}
	
	public Authorization(Actor actor, Role role) {
		this.actor = actor;
		this.role = role;
	}

	/**
	 * @return the user
	 */
	public Actor getUser() {
		return actor;
	}

	/**
	 * @param actor the user to set
	 */
	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
