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

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItemRepository;

/**
 * @author Florian Esser
 *
 */
@Repository
@RepositoryRestResource(collectionResourceRel = "items", path = "items")
public interface RegisterItemRepository extends RE_RegisterItemRepository
{
	@Override
	@Query("select max(i.itemIdentifier) from RE_RegisterItem i")
	public BigInteger findMaxItemIdentifier();

	@RestResource(exported = false)
	public Set<RE_RegisterItem> findByStatus(RE_ItemStatus status);
	public Page<RE_RegisterItem> findByStatus(RE_ItemStatus status, Pageable pageable);
	
	public Set<RE_RegisterItem> findByItemClass(RE_ItemClass itemClass);
	
	public Page<RE_RegisterItem> findByRegisterAndItemClassAndStatus(RE_Register register, RE_ItemClass itemClass, RE_ItemStatus status, Pageable pageable);
	public Page<RE_RegisterItem> findByRegisterAndItemClassAndStatusIn(RE_Register register, RE_ItemClass itemClass, Collection<RE_ItemStatus> status, Pageable pageable);
	
	public List<RE_RegisterItem> findByRegisterAndItemIdentifierAndStatus(RE_Register register, BigInteger itemIdentifier, RE_ItemStatus status);
	public List<RE_RegisterItem> findByRegisterAndItemIdentifierAndStatusIn(RE_Register register, BigInteger itemIdentifier, Collection<RE_ItemStatus> status);

	@Query("SELECT i FROM RE_RegisterItem i WHERE i.register = :register AND i.itemClass IN :itemClass AND i.status IN :status")
	public Page<RE_RegisterItem> findByRegisterAndItemClassInAndStatusIn(@Param("register") RE_Register register, @Param("itemClass") Collection<RE_ItemClass> itemClass, @Param("status") Collection<RE_ItemStatus> status, Pageable pageable);

	@Query("SELECT i FROM RE_RegisterItem i WHERE i.register = :register AND i.itemClass = :itemClass AND i.status IN :status AND (LOWER(i.name) LIKE LOWER(:search) OR LOWER(i.status.name) LIKE LOWER(:search) OR CAST(i.itemIdentifier AS text) LIKE :search)")
	public Page<RE_RegisterItem> findByRegisterAndItemClassAndStatusInFiltered(@Param("register") RE_Register register, @Param("itemClass") RE_ItemClass itemClass, @Param("status") Collection<RE_ItemStatus> status, @Param("search") String search, Pageable pageable);

	@Query("SELECT i FROM RE_RegisterItem i WHERE i.register = :register AND i.itemClass IN :itemClass AND i.status IN :status AND (LOWER(i.name) LIKE LOWER(:search) OR LOWER(i.status.name) LIKE LOWER(:search) OR CAST(i.itemIdentifier AS text) LIKE :search)")
	public Page<RE_RegisterItem> findByRegisterAndItemClassInAndStatusInFiltered(@Param("register") RE_Register register, @Param("itemClass") Collection<RE_ItemClass> itemClass, @Param("status") Collection<RE_ItemStatus> status, @Param("search") String search, Pageable pageable);

	public List<RE_RegisterItem> findByRegisterAndStatus(RE_Register register, RE_ItemStatus status);
	public Page<RE_RegisterItem> findByRegisterAndStatus(RE_Register register, RE_ItemStatus status, Pageable pageable);
	public Page<RE_RegisterItem> findByRegisterAndStatusIn(RE_Register register, Collection<RE_ItemStatus> status, Pageable pageable);
	@Query("SELECT i FROM RE_RegisterItem i WHERE i.register = :register AND i.status IN :status AND (LOWER(i.name) LIKE LOWER(:search) OR LOWER(i.status.name) LIKE LOWER(:search) OR CAST(i.itemIdentifier AS text) LIKE :search)")
	public Page<RE_RegisterItem> findByRegisterAndStatusIn(@Param("register") RE_Register register, @Param("status") Collection<RE_ItemStatus> status, @Param("search") String search, Pageable pageable);
	
	public Page<RE_RegisterItem> findByRegister(RE_Register register, Pageable pageable);
	
	@Query("SELECT i FROM RE_RegisterItem i WHERE i.register = :register AND i.itemClass.name = :itemClassName AND i.status IN :status")
	public Page<RE_RegisterItem> findByRegisterAndItemClassNameAndStatusIn(@Param("register") RE_Register register, @Param("itemClassName") String itemClassName,  @Param("status") Collection<RE_ItemStatus> status, Pageable pageable);
	@Query("SELECT i FROM RE_RegisterItem i WHERE i.register = :register AND i.itemClass.name = :itemClassName AND i.status IN :status AND (LOWER(i.name) LIKE LOWER(:search) OR LOWER(i.status.name) LIKE LOWER(:search) OR CAST(i.itemIdentifier AS text) LIKE :search)")
	public Page<RE_RegisterItem> findByRegisterAndItemClassNameAndStatusIn(@Param("register") RE_Register register, @Param("itemClassName") String itemClassName, @Param("status") Collection<RE_ItemStatus> status, @Param("search") String search, Pageable pageable);
}
