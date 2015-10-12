package de.geoinfoffm.registry.api;

import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.bespire.LoggerFactory;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.Proposal;

@Component
@Scope("prototype")
public class CreateAndSubmitProposalTask extends CreateProposalTask<Proposal>
{
	private static final Logger logger = LoggerFactory.make();

	public CreateAndSubmitProposalTask(RegisterItemProposalDTO proposalDto, ProposalService proposalService, Authentication runAs) {
		super(proposalDto, proposalService, runAs);
	}

	@Override
	@Transactional
	protected Proposal run() throws Exception {
		try {
			Proposal proposal = proposalService.propose(proposalDto);
			return proposalService.submitProposal(proposal);
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw t;
		}
	}

}
