package de.geoinfoffm.registry.api;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import de.bespire.LoggerFactory;
import de.geoinfoffm.registry.core.model.Proposal;

@Component
@Scope("prototype")
public class DeleteProposalTask extends ProposalTask<Void>
{
	private static final Logger logger = LoggerFactory.make();

	public DeleteProposalTask(UUID proposalUuid, ProposalService proposalService, Authentication runAs) {
		super(proposalUuid, proposalService, runAs);
	}

	@Override
	protected Void run() throws Exception {
		try {
			Proposal proposal = proposalService.findOne(proposalUuid);
			proposalService.deleteProposal(proposal);
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw t;
		}
		
		return null;
	}

}
