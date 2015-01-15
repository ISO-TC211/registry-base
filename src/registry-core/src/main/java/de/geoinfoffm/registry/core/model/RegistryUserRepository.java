/**
 * 
 */
package de.geoinfoffm.registry.core.model;

import java.util.List;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;

/**
 * Backend interface for the class {@link RegistryUser}.
 * 
 * @author Florian Esser
 *
 */
@Repository
public interface RegistryUserRepository extends EntityRepository<RegistryUser>
{
	public RegistryUser findByName(String name);
	public RegistryUser findByEmailAddress(String emailAddress);
	public List<RegistryUser> findByOrganization(Organization organization);
}
