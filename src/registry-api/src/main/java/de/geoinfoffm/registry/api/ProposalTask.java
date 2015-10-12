package de.geoinfoffm.registry.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class ProposalTask<R> extends RunAsTask<R> implements ParameterizedCallable<R>
{
	protected final ProposalService proposalService;
	protected final UUID proposalUuid;

	public ProposalTask(UUID proposalUuid, ProposalService proposalService, Authentication runAs) {
		super(runAs);

		this.proposalService = proposalService;
		this.proposalUuid = proposalUuid;
	}
	
	@Override
	public Map<String, Object> getParameters() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("proposalUuid", proposalUuid);

		return result;
	}

}