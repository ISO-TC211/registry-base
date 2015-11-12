package de.geoinfoffm.registry.api;

import java.util.concurrent.Callable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

public abstract class RunAsTask<R> implements Callable<R>
{
	private final Authentication runAs;
	
	public RunAsTask(Authentication runAs) {
		this.runAs = runAs;
	}
	
	public Authentication getRunAs() {
		return runAs;
	}

	@Transactional
	@Override
	public final R call() throws Exception {
		Assert.notNull(runAs, "Authentication must be provided for RunAsTask");
		
		SecurityContextHolder.getContext().setAuthentication(runAs);
		return run();
	}

	protected abstract R run() throws Exception;
}
