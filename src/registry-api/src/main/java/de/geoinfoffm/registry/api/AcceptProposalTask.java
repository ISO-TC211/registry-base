package de.geoinfoffm.registry.api;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.bespire.LoggerFactory;
import de.geoinfoffm.registry.core.model.Proposal;

@Component
@Scope("prototype")
public class AcceptProposalTask extends ProposalTask<Proposal>
{
	private static final Logger logger = LoggerFactory.make();
	
	private final String controlBodyDecisionEvent;

	public AcceptProposalTask(UUID proposalUuid, String controlBodyDecisionEvent, ProposalService proposalService, Authentication runAs) {
		super(proposalUuid, proposalService, runAs);
		this.controlBodyDecisionEvent = controlBodyDecisionEvent;
	}
	
	@Override
	@Transactional
	public Proposal run() throws Exception {
		try {
			Proposal proposal = proposalService.findOne(proposalUuid);
			return proposalService.acceptProposal(proposal, controlBodyDecisionEvent);
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw t;
		}
	}
}
