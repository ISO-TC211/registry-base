package de.geoinfoffm.registry.core.model;

import java.util.List;

import de.geoinfoffm.registry.core.EntityRepository;

public interface ProposalRelatedRoleRepository extends EntityRepository<ProposalRelatedRole>
{
	<P extends Proposal> List<ProposalRelatedRole> findByProposal(P proposal);
}
