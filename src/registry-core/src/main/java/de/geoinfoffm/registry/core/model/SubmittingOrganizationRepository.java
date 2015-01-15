/**
 * 
 */
package de.geoinfoffm.registry.core.model;

import de.geoinfoffm.registry.core.AuditedRepository;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

/**
 * @author Florian.Esser
 *
 */
public interface SubmittingOrganizationRepository extends AuditedRepository<RE_SubmittingOrganization>
{
	RE_SubmittingOrganization findByName(String name);
}
