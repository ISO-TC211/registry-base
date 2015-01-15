package de.geoinfoffm.registry.core.model;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

/**
 * Backend interface for the class {@link Organization}.
 * 
 * @author Florian Esser
 *
 */
@Repository
public interface OrganizationRepository extends EntityRepository<Organization> 
{
	@Query("SELECT o FROM Organization o WHERE LOWER(o.name) = LOWER(:name)")
	Organization findByName(@Param("name") String organizationName);
	
	@Query("SELECT o.uuid, o.name FROM Organization o ORDER BY o.name")
	List<Object[]> getOrganizationNames();
	
	Organization findBySubmittingOrganization(RE_SubmittingOrganization submittingOrganization);
}
