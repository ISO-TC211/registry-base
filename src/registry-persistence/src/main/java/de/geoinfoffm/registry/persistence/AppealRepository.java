/**
 * 
 */
package de.geoinfoffm.registry.persistence;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.AuditedRepository;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;

/**
 * @author Florian Esser
 *
 */
@Repository
public interface AppealRepository extends AuditedRepository<Appeal>
{
	public Appeal findByAppealedProposal(Proposal proposal);
}
