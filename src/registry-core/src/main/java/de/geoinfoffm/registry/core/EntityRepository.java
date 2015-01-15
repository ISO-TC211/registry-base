package de.geoinfoffm.registry.core;

import org.springframework.data.repository.NoRepositoryBean;

import de.geoinfoffm.registry.core.security.AclMaintainingRepository;

@NoRepositoryBean
public interface EntityRepository<T extends Entity> extends AuditedRepository<T>, AclMaintainingRepository<T>
{

}
