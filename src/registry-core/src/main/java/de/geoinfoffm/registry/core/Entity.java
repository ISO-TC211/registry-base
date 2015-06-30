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
package de.geoinfoffm.registry.core;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
import org.hibernate.envers.Audited;
import org.hibernate.proxy.HibernateProxy;

import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;

/**
 * Abstract base class for the entities in the domain model.
 * 
 * @author Florian Esser
 *
 */
@XmlRootElement
@Access(AccessType.FIELD)
@Audited @MappedSuperclass
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
