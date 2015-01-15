package de.geoinfoffm.registry.core.model;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;

@Repository
public interface RoleRepository extends EntityRepository<Role>
{
	Role findByName(String name);
}
