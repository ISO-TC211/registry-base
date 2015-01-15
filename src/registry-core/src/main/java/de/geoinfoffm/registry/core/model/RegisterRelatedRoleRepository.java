package de.geoinfoffm.registry.core.model;

import java.util.List;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@Repository
public interface RegisterRelatedRoleRepository extends EntityRepository<RegisterRelatedRole>
{
	<R extends RE_Register> List<RegisterRelatedRole> findByRegister(R register);
}
