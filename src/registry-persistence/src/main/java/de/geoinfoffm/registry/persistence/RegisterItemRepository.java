/** 
 * 
 */
package de.geoinfoffm.registry.persistence;

import java.math.BigInteger;
import java.util.Collection;
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
	
	public Page<RE_RegisterItem> findByRegisterAndItemClassAndStatus(RE_Register register, RE_ItemClass itemClass, RE_ItemStatus status, Pageable pageable);
	public Page<RE_RegisterItem> findByRegisterAndItemClassAndStatusIn(RE_Register register, RE_ItemClass itemClass, Collection<RE_ItemStatus> status, Pageable pageable);

	@Query("SELECT i FROM RE_RegisterItem i WHERE i.register = :register AND i.itemClass IN :itemClass AND i.status IN :status")
	public Page<RE_RegisterItem> findByRegisterAndItemClassInAndStatusIn(@Param("register") RE_Register register, @Param("itemClass") Collection<RE_ItemClass> itemClass, @Param("status") Collection<RE_ItemStatus> status, Pageable pageable);

	@Query("SELECT i FROM RE_RegisterItem i WHERE i.register = :register AND i.itemClass = :itemClass AND i.status IN :status AND (LOWER(i.name) LIKE LOWER(:search) OR LOWER(i.status.name) LIKE LOWER(:search) OR CAST(i.itemIdentifier AS text) LIKE :search)")
	public Page<RE_RegisterItem> findByRegisterAndItemClassAndStatusInFiltered(@Param("register") RE_Register register, @Param("itemClass") RE_ItemClass itemClass, @Param("status") Collection<RE_ItemStatus> status, @Param("search") String search, Pageable pageable);

	@Query("SELECT i FROM RE_RegisterItem i WHERE i.register = :register AND i.itemClass IN :itemClass AND i.status IN :status AND (LOWER(i.name) LIKE LOWER(:search) OR LOWER(i.status.name) LIKE LOWER(:search) OR CAST(i.itemIdentifier AS text) LIKE :search)")
	public Page<RE_RegisterItem> findByRegisterAndItemClassInAndStatusInFiltered(@Param("register") RE_Register register, @Param("itemClass") Collection<RE_ItemClass> itemClass, @Param("status") Collection<RE_ItemStatus> status, @Param("search") String search, Pageable pageable);

	public Page<RE_RegisterItem> findByRegisterAndStatus(RE_Register register, RE_ItemStatus status, Pageable pageable);
	public Page<RE_RegisterItem> findByRegisterAndStatusIn(RE_Register register, Collection<RE_ItemStatus> status, Pageable pageable);
	@Query("SELECT i FROM RE_RegisterItem i WHERE i.register = :register AND i.status IN :status AND (LOWER(i.name) LIKE LOWER(:search) OR LOWER(i.status.name) LIKE LOWER(:search) OR CAST(i.itemIdentifier AS text) LIKE :search)")
	public Page<RE_RegisterItem> findByRegisterAndStatusInFiltered(@Param("register") RE_Register register, @Param("status") Collection<RE_ItemStatus> status, @Param("search") String search, Pageable pageable);
	
	public Page<RE_RegisterItem> findByRegister(RE_Register register, Pageable pageable);
}
