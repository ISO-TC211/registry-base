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
package de.geoinfoffm.registry.persistence;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.exception.RevisionDoesNotExistException;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryImpl;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.history.support.RevisionEntityInformation;

import de.geoinfoffm.registry.core.AuditedRepository;
import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.NonExistentRevisionException;

/**
 * @author Florian Esser
 *
 */
public class AuditedRepositoryImpl<T extends Entity> extends EnversRevisionRepositoryImpl<T, UUID, Integer> implements AuditedRepository<T>
{
	private final EntityInformation<T, ?> entityInformation;
	private final RevisionEntityInformation revisionEntityInformation;
	private final EntityManager entityManager;

	/**
	 * @param entityInformation
	 * @param revisionEntityInformation
	 * @param entityManager
	 */
	public AuditedRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
			RevisionEntityInformation revisionEntityInformation, EntityManager entityManager) {

		super(entityInformation, revisionEntityInformation, entityManager);
		
		this.entityInformation = entityInformation;
		this.revisionEntityInformation = revisionEntityInformation;
		this.entityManager = entityManager;
	}

	
	@Override
	public T findOne(UUID uuid, Integer revision) throws NonExistentRevisionException {
		Class<T> type = entityInformation.getJavaType();

		AuditReader reader = AuditReaderFactory.get(entityManager);
		return reader.find(type, uuid, revision);
	}

	

	@Override
	public T findOne(UUID uuid, Date revisionDate) throws NonExistentRevisionException {
		AuditReader reader = AuditReaderFactory.get(entityManager);
		Number revision;
		try {
			revision = reader.getRevisionNumberForDate(revisionDate);
		}
		catch (RevisionDoesNotExistException e) {
			throw new NonExistentRevisionException(e.getMessage(), e);
		}
		
		return this.findOne(uuid, (Integer)revision);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.persistence.AuditedBackend#findAll(java.lang.Integer)
	 */
	@Override
	public List<T> findAll(Integer revision) {
		Class<T> type = entityInformation.getJavaType();

		AuditReader reader = AuditReaderFactory.get(entityManager);
		return reader.createQuery().forEntitiesAtRevision(type, revision).getResultList();
	}
	
	@Override
	public List<T> findAll(Date revisionDate) {
		AuditReader reader = AuditReaderFactory.get(entityManager);
		Number revision = reader.getRevisionNumberForDate(revisionDate);
		
		return this.findAll((Integer)revision);
	}
}
