package de.geoinfoffm.registry.persistence.security;

import java.util.List;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.security.AclEntry;

@Repository
public interface AclEntryRepository extends EntityRepository<AclEntry>
{
}
