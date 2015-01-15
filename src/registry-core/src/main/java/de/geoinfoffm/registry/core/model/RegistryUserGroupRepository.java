package de.geoinfoffm.registry.core.model;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;

@Repository
public interface RegistryUserGroupRepository extends EntityRepository<RegistryUserGroup>
{
	RegistryUserGroup findByName(String name);
}
