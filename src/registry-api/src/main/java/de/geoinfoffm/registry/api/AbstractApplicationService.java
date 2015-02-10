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
package de.geoinfoffm.registry.api;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.security.acls.model.MutableAclService;

import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.NonExistentRevisionException;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.security.RegistrySecurity;

/**
 * Basic service operations for {@link Entity}s in the GDI-DE Registry.<br>
 * <br>
 * All {@link ApplicationService}s should extend this class or {@link AbstractRemoteService}
 * to provide for basic CRUD operations.
 * 
 * @author Florian Esser
 */
public abstract class AbstractApplicationService<E extends Entity, R extends EntityRepository<E>> 
implements ApplicationService<E>, ApplicationEventPublisherAware 
{
	private R repository;
	
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private MutableAclService mutableAclService;
	
	@Autowired
	private RegistrySecurity security;
	
	public AbstractApplicationService(R repository) {
		this.repository = repository;
	}
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}


	protected R repository() {
		return repository;
	}
	
	protected ApplicationEventPublisher eventPublisher() {
		return this.eventPublisher;
	}
	
	@Override
	public <S extends E> S update(S entity) throws UnauthorizedException {
		security.assertMayWrite(entity);
		
		return repository.save(entity);
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ApplicationService#findOne(java.util.UUID)
	 */
	@Override
	public E findOne(UUID id) {
		if (id == null) {
			throw new IllegalArgumentException("id must not be null");
		}

		return repository.findOne(id);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ApplicationService#findAll()
	 */
	@Override
	public List<E> findAll() {
		return repository.findAll();
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ApplicationService#findAll(int)
	 */
	@Override
	public List<E> findAll(int revision) throws NonExistentRevisionException {
		return repository.findAll(revision);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ApplicationService#findAll(java.util.Date)
	 */
	@Override
	public List<E> findAll(Date revisionDate) throws NoRevisionAtThisPointInTimeException {
		try {
			return repository.findAll(revisionDate);
		}
		catch (NonExistentRevisionException e) {
			throw new NoRevisionAtThisPointInTimeException(e.getMessage(), e);
		}
	}

	@Override
	public List<E> findAll(Sort sort) {
		return repository.findAll(sort);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ApplicationService#delete(java.util.UUID)
	 */
	@Override
	public void delete(UUID id) throws UnauthorizedException {
		E entity = repository().findOne(id);
		if (entity == null) {
			throw new EntityNotFoundException(String.format("Cannot delete non-existent entity %s", id));
		}
		
		if (!security.isAdmin()) {
			security.assertMayDelete(entity);
		}
		
		try {
			repository.delete(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException(e.getMessage(), e);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.ApplicationService#count()
	 */
	@Override
	public long count() {
		return repository.count();
	}
	
	protected MutableAclService mutableAclService() {
		return mutableAclService;
	}
}
