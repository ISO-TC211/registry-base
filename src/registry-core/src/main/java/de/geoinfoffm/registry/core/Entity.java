package de.geoinfoffm.registry.core;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.proxy.HibernateProxy;

import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;

/**
 * Abstract base class for the entities in the domain model.
 * 
 * @author Florian Esser
 *
 */
@MappedSuperclass
@XmlRootElement
public abstract class Entity implements Serializable
{
	@XmlID @XmlAttribute(name = "uuid", namespace = "http://www.isotc211.org/2005/gco")
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "uuid")
	@Type(type = "pg-uuid")	
	private UUID uuid;
	
	protected Entity() {
//		this.uuid = UUID.randomUUID();
	}
	
	/**
	 * Create a new entity with the given ID.
	 * 
	 * @param id ID of the entity.
	 */
	protected Entity(UUID id) {
		if (id == null) {
			throw new IllegalArgumentException("Null id not allowed");
		}
		
		this.uuid = id;
	}

	/**
	 * @return the ID of the entity.
	 */
	public UUID getUuid() {
		return uuid;
	}
	
	public UUID getId() {
		return getUuid();
	}
	
	@Transient
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}

		if (o == this) {
			return true;
		}

		if (!o.getClass().equals(this.getClass())) {
			return false;
		}

		Entity other = (Entity)o;

		if (this.getUuid() == null || other.getUuid() == null) {
			// Entities without an ID cannot be compared
			return false;
		}

		return other.getUuid().equals(this.getUuid());
	}

	@Transient
	@Override
	public int hashCode() {
		if (uuid != null) {
			return uuid.hashCode();
		}
		else {
			return HashCodeBuilder.reflectionHashCode(this, false);
		}
	}	

	public static Object unproxify(Object candidate) {
		if (candidate instanceof HibernateProxy) {
			Hibernate.initialize(candidate);
	        return ((HibernateProxy)candidate)
	                  .getHibernateLazyInitializer()
	                  .getImplementation();
		}
		
		return candidate;
	}
}
