/**
 * 
 */
package de.geoinfoffm.registry.core.model.iso19135;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.AuditedRepository;

/**
 * @author Florian.Esser
 *
 */
@Repository
public interface ProposalManagementInformationRepository extends AuditedRepository<RE_ProposalManagementInformation>
{
	public List<RE_ProposalManagementInformation> findByItem(RE_RegisterItem item);

	public List<RE_ProposalManagementInformation> findByStatus(RE_DecisionStatus status);
	public List<RE_ProposalManagementInformation> findByStatusIn(Collection<RE_DecisionStatus> status);
	public List<RE_ProposalManagementInformation> findByStatusAndDateProposedIsNotNull(RE_DecisionStatus status);

	public List<RE_ProposalManagementInformation> findByItemAndStatus(RE_RegisterItem item, RE_DecisionStatus status);	
}
