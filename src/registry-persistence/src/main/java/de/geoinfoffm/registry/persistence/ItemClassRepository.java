/**
 * 
 */
package de.geoinfoffm.registry.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClassRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

/**
 * @author Florian.Esser
 *
 */
@Repository
public interface ItemClassRepository extends RE_ItemClassRepository
{
	public RE_ItemClass findByName(String name);
	
	@Query("SELECT i.uuid, i.name FROM RE_ItemClass i ORDER BY i.name")
	public List<Object[]> findAllOrderByName();

	@Query("SELECT i.uuid, i.name FROM RE_ItemClass i WHERE :register MEMBER OF i.registers ORDER BY i.name")
	public List<Object[]> findByRegisterOrderByName(RE_Register register);

}
