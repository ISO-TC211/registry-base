/**
 * 
 */
package de.geoinfoffm.registry.persistence;

import java.util.List;

import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;

/**
 * @author Florian Esser
 *
 */
public interface SimpleProposalRepository extends EntityRepository<SimpleProposal>
{
	SimpleProposal findByProposalManagementInformation(RE_ProposalManagementInformation pmi);
}
