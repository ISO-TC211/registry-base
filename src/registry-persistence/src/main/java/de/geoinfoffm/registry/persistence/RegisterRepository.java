package de.geoinfoffm.registry.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

/**
 * @author Florian Esser
 *
 */
@Repository
public interface RegisterRepository extends EntityRepository<RE_Register>
{
	public RE_Register findByName(String registerName);
	
	@Query("SELECT r.uuid, r.name FROM RE_Register r ORDER BY r.name")
	public List<Object[]> getRegisterNames();
}
