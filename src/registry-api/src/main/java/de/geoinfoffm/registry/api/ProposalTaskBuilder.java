package de.geoinfoffm.registry.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.model.Proposal;

@Component
public class ProposalTaskBuilder
{
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private ProposalService proposalService;
	
	protected ProposalTaskBuilder() {
	}
	
	public ParameterizedCallable<Proposal> buildSubmitTask(Proposal proposal) {
		return buildSubmitTask(proposal, SecurityContextHolder.getContext().getAuthentication());
	}
	
	public ParameterizedCallable<Proposal> buildSubmitTask(Proposal proposal, Authentication runAs) {
		Assert.notNull(runAs, "Authentication must not be null. Use argument 'SecurityContextHolder.getContext().getAuthentication()' to use thread's current authentication.");
		return getBean("submitProposalTask", SubmitProposalTask.class, proposal.getUuid(), proposalService, runAs);
	}

	public ParameterizedCallable<Proposal> buildReviewTask(Proposal proposal) {
		return buildReviewTask(proposal, SecurityContextHolder.getContext().getAuthentication());
	}

	public ParameterizedCallable<Proposal> buildReviewTask(Proposal proposal, Authentication runAs) {
		Assert.notNull(runAs, "Authentication must not be null. Use argument 'SecurityContextHolder.getContext().getAuthentication()' to use thread's current authentication.");
		return getBean("reviewProposalTask", ReviewProposalTask.class, proposal.getUuid(), proposalService, runAs);
	}
	
	public ParameterizedCallable<Proposal> buildAcceptTask(Proposal proposal, String controlBodyDecisionEvent) {
		return buildAcceptTask(proposal, controlBodyDecisionEvent, SecurityContextHolder.getContext().getAuthentication());
	}

	public ParameterizedCallable<Proposal> buildAcceptTask(Proposal proposal, String controlBodyDecisionEvent, Authentication runAs) {
		Assert.notNull(runAs, "Authentication must not be null. Use argument 'SecurityContextHolder.getContext().getAuthentication()' to use thread's current authentication.");
		return getBean("acceptProposalTask", AcceptProposalTask.class, proposal.getUuid(), controlBodyDecisionEvent, proposalService, runAs);
	}

	public ParameterizedCallable<Proposal> buildRejectTask(Proposal proposal, String controlBodyDecisionEvent) {
		return buildRejectTask(proposal, controlBodyDecisionEvent, SecurityContextHolder.getContext().getAuthentication());
	}
	
	public ParameterizedCallable<Proposal> buildRejectTask(Proposal proposal, String controlBodyDecisionEvent, Authentication runAs) {
		Assert.notNull(runAs, "Authentication must not be null. Use argument 'SecurityContextHolder.getContext().getAuthentication()' to use thread's current authentication.");
		return getBean("rejectProposalTask", RejectProposalTask.class, proposal.getUuid(), controlBodyDecisionEvent, proposalService, runAs);
	}
	
	public ParameterizedCallable<Void> buildDeleteTask(Proposal proposal) {
		return buildDeleteTask(proposal, SecurityContextHolder.getContext().getAuthentication());
	}
	
	public ParameterizedCallable<Void> buildDeleteTask(Proposal proposal, Authentication runAs) {
		Assert.notNull(runAs, "Authentication must not be null. Use argument 'SecurityContextHolder.getContext().getAuthentication()' to use thread's current authentication.");
		return getBean("deleteProposalTask", DeleteProposalTask.class, proposal.getUuid(), proposalService, runAs);
	}

	public ParameterizedCallable<Proposal> buildWithdrawTask(Proposal proposal) {
		return buildWithdrawTask(proposal, SecurityContextHolder.getContext().getAuthentication());		
	}

	public ParameterizedCallable<Proposal> buildWithdrawTask(Proposal proposal, Authentication runAs) {
		Assert.notNull(runAs, "Authentication must not be null. Use argument 'SecurityContextHolder.getContext().getAuthentication()' to use thread's current authentication.");
		return getBean("withdrawProposalTask", WithdrawProposalTask.class, proposal.getUuid(), proposalService, runAs);		
	}
	
	public ParameterizedCallable<Proposal> buildCreateAndSubmitProposalTask(RegisterItemProposalDTO proposal) {
		return buildCreateAndSubmitProposalTask(proposal, SecurityContextHolder.getContext().getAuthentication());
	}
	
	public ParameterizedCallable<Proposal> buildCreateAndSubmitProposalTask(RegisterItemProposalDTO proposal, Authentication runAs) {
		Assert.notNull(runAs, "Authentication must not be null. Use argument 'SecurityContextHolder.getContext().getAuthentication()' to use thread's current authentication.");
		return getBean("createAndSubmitProposalTask", CreateAndSubmitProposalTask.class, proposal, proposalService, runAs);
	}
	
	private <T> T getBean(String name, Class<T> beanClass, Object... args) {
		Object bean = context.getBean(name, args);
		
		return (T)Entity.unproxify(bean);
	}
}
