package de.geoinfoffm.registry.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class CreateProposalTask<R> extends RunAsTask<R> implements ParameterizedCallable<R>
{
	protected final ProposalService proposalService;
	protected final RegisterItemProposalDTO proposalDto;

	public CreateProposalTask(RegisterItemProposalDTO proposalDto, ProposalService proposalService, Authentication runAs) {
		super(runAs);

		this.proposalService = proposalService;
		this.proposalDto = proposalDto;
	}
	
	@Override
	public Map<String, Object> getParameters() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("proposalDto", proposalDto);

		return result;
	}
}
