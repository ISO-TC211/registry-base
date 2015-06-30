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

import org.springframework.data.domain.Sort;

import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.NonExistentRevisionException;
import de.geoinfoffm.registry.core.UnauthorizedException;

/**
 * Base interface for {@link Entity} application services.<br>
 * <br>
 * This interface should only be implemented by the root classes of an aggregate.
 * 
 * @author Florian Esser
 * @param <T> Root type of aggregate
 *
 */
public interface ApplicationService<T extends Entity>
{
	public <S extends T> S update(S entity) throws UnauthorizedException;
	
	/**
	 * Deletes an organization.
	 * 
	 * @param id ID of the organization to delete.
	 * @throws UnauthorizedException 
	 * @throws IllegalArgumentException thrown if no organization with the given ID exists
	 */
	public void delete(UUID id) throws UnauthorizedException;
	
	/**
	 * Finds the organization with then given ID.
	 *  
	 * @param id ID of the organization to find
	 * @return the organization or null if no organization with the given ID exists
	 */
	public T findOne(UUID id);
	
	/**
	 * Finds all organizations.
	 * 
	 * @return all organizations
	 */
	public List<T> findAll();
	
	/**
	 * Finds all organizations at a specific backend revision.
	 * 
	 * @param revision Backend revision
	 * @return a historic list of organizations
	 */
	public List<T> findAll(int revision) throws NonExistentRevisionException;

	/**
	 * Finds all organizations at a specific point in time.
	 * 
	 * @param revisionDate Backend revision date
	 * @return a historic list of organizations
	 * @throws NoRevisionAtThisPointInTimeException 
	 */
	public List<T> findAll(Date revisionDate) throws NoRevisionAtThisPointInTimeException;
	
	public List<T> findAll(Sort sort);

	/**
	 * @return the number of organizations 
	 */
	public long count();

}
