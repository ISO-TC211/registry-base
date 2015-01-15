package de.geoinfoffm.registry.persistence.security;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.security.AclObjectIdentity;

@Repository
public interface AclObjectIdentityRepository extends EntityRepository<AclObjectIdentity>
{

}
