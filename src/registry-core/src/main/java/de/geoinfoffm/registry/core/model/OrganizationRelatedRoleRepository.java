package de.geoinfoffm.registry.core.model;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;

@Repository
public interface OrganizationRelatedRoleRepository extends EntityRepository<OrganizationRelatedRole>
{
	Collection<OrganizationRelatedRole> findByOrganization(Organization organization);
}
