/**
 * 
 */
package de.geoinfoffm.registry.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

/**
 * @author Florian.Esser
 *
 */
public interface ProposalRepository extends EntityRepository<Proposal>
{
	List<Proposal> findByDateSubmittedIsNotNullAndIsConcludedIsFalse();

	List<Proposal> findByDateSubmittedIsNotNullAndGroupIsNullAndIsConcludedIsTrue();
	List<Proposal> findByDateSubmittedIsNotNullAndGroupIsNullAndIsConcludedIsFalse();
	
	List<Proposal> findBySponsorAndStatusAndDateSubmittedIsNotNullAndGroupIsNullAndIsConcludedIsFalse(RE_SubmittingOrganization sponsor, String status);
	
	List<Proposal> findBySponsorAndGroupIsNullAndIsConcludedIsFalse(RE_SubmittingOrganization sponsor);
	Page<Proposal> findBySponsorAndGroupIsNullAndIsConcludedIsFalse(RE_SubmittingOrganization sponsor, Pageable pageable);
	
	Page<Proposal> findBySponsorAndStatusAndGroupIsNull(RE_SubmittingOrganization sponsor, String status, Pageable pageable);
	List<Proposal> findByStatusAndGroupIsNull(String status);
	Page<Proposal> findByStatusAndGroupIsNull(String status, Pageable pageable);
}
