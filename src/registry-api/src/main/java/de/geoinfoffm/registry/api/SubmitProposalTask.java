package de.geoinfoffm.registry.api;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.bespire.LoggerFactory;
import de.geoinfoffm.registry.core.model.Proposal;

@Component
@Scope("prototype")
public class SubmitProposalTask extends ProposalTask<Proposal>
{
	private static final Logger logger = LoggerFactory.make();

	public SubmitProposalTask(UUID proposalUuid, ProposalService proposalService, Authentication runAs) {
		super(proposalUuid, proposalService, runAs);
	}

	@Override
	@Transactional
	public Proposal run() throws Exception {
		try {
			Proposal proposal = proposalService.findOne(proposalUuid);
			return proposalService.submitProposal(proposal);
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw t;
		}
	}
}
