package de.geoinfoffm.registry.api;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.security.concurrent.DelegatingSecurityContextCallable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.geoinfoffm.registry.core.model.Proposal;

@Transactional
@Service
public class ProposalTaskManager
{
	private static final ConcurrentHashMap<String, Future<?>> proposalTasks = new ConcurrentHashMap<String, Future<?>>();
	
	private final AsyncTaskExecutor taskExecutor;
	
	@Autowired
	public ProposalTaskManager(AsyncTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public boolean hasActiveTask(Proposal proposal) {
		Future<?> task = proposalTasks.get(proposal.getUuid().toString());
		return (task != null && !task.isDone());
	}
	
	/**
	 * Submits a {@link ProposalTask} to the application's {@link AsyncTaskExecutor}. The task
	 * will be run in a separate thread with a separate {@link SecurityContext}. 
	 * 
	 * @param task {@link ProposalTask} to execute
	 * @return {@link Future} (probably even a {@link FutureTask}) for the submitted task
	 * @throws TaskRejectedException if the task is rejected by the {@link AsyncTaskExecutor}
	 */
	public <R> Future<R> submit(ParameterizedCallable<R> task) throws TaskRejectedException {
		Assert.notNull(task.getParameters(), "Invalid task: getParameters() returns null");
	
		Callable<R> taskWithEmptySecurityContext = new DelegatingSecurityContextCallable<R>(task, SecurityContextHolder.createEmptyContext());
		Future<R> future = (Future<R>)taskExecutor.submit(taskWithEmptySecurityContext);

		Object proposalUuid = task.getParameters().get("proposalUuid");
		if (proposalUuid != null) {
			proposalTasks.put(proposalUuid.toString(), future);
		}
		
		return future;
	}
}
