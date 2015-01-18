package de.bespire.registry.core;

import java.util.List;

import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.model.Proposal;

public interface ProposalRelatedRoleRepository extends EntityRepository<ProposalRelatedRole>
{
	<P extends Proposal> List<ProposalRelatedRole> findByProposal(P proposal);
}
