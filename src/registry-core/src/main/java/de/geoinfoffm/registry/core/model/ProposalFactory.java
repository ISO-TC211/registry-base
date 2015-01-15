/**
 * 
 */
package de.geoinfoffm.registry.core.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.geoinfoffm.registry.core.model.iso19135.ProposalManagementInformationRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ClarificationInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;

/**
 * @author Florian.Esser
 *
 */
@Component
public class ProposalFactory implements Proposal.Factory
{
	@Autowired
	private ProposalManagementInformationRepository pmiRepository; 
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.Proposal.Factory#createProposal(de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation)
	 */
	@Override
	public Proposal createProposal(RE_ProposalManagementInformation proposalManagementInformation) {
		if (proposalManagementInformation instanceof RE_AdditionInformation) {
			return new Addition((RE_AdditionInformation)proposalManagementInformation);
		}
		else if (proposalManagementInformation instanceof RE_ClarificationInformation) {
			return new Clarification((RE_ClarificationInformation)proposalManagementInformation);
		}
		else if (proposalManagementInformation instanceof RE_AmendmentInformation) {
			RE_AmendmentInformation ai = (RE_AmendmentInformation)proposalManagementInformation;
			switch (ai.getAmendmentType()) {
				case RETIREMENT:
					return new Retirement(ai);
				case SUPERSESSION:
					return new SupersessionPart(ai);
			}
		}

		return null;
	}
}
