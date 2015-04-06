package de.geoinfoffm.registry.core.workflow;

import java.util.Date;

import org.dom4j.datatype.DatatypeAttribute;

import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;

public interface ProposalWorkflowManager extends WorkflowManager<Proposal>
{
	public static final String STATUS_NOT_SUBMITTED = "NOT_SUBMITTED";
	public static final String STATUS_UNDER_REVIEW = "UNDER_REVIEW";
	public static final String STATUS_IN_APPROVAL_PROCESS = "IN_APPROVAL_PROCESS";
	public static final String STATUS_APPEALABLE = "APPEALABLE";
	public static final String STATUS_APPEALED = "APPEALED";
	public static final String STATUS_FINISHED = "FINISHED";
	public static final String STATUS_WITHDRAWN = "WITHDRAWN";

	void initialize(Proposal proposal) throws IllegalOperationException;
	void submit(Proposal proposal, Date submissionDate) throws IllegalOperationException;
	void review(Proposal proposal, Date reviewDate) throws IllegalOperationException;
	void accept(Proposal proposal) throws IllegalOperationException;
	void accept(Proposal proposal, String controlBodyDecisionEvent) throws IllegalOperationException;
	void reject(Proposal proposal) throws IllegalOperationException;
	void reject(Proposal proposal, String controlBodyDecisionEvent) throws IllegalOperationException;	
	void withdraw(Proposal proposal) throws IllegalOperationException;
	void conclude(Proposal proposal) throws IllegalOperationException;
	void delete(Proposal proposal) throws IllegalOperationException;
	Appeal appeal(Proposal proposal, String justification, String impact, String situation) throws IllegalOperationException;
	void acceptAppeal(Appeal appeal, Date dispositionDate);
	void rejectAppeal(Appeal appeal, Date dispositionDate);
	
	boolean isSubmitted(Proposal proposal);
	boolean isSubmitted(String status);
	boolean isUnderReview(Proposal proposal);
	boolean isUnderReview(String status);
	boolean isReviewed(Proposal proposal);
	boolean isReviewed(String status);
	boolean isPending(Proposal proposal);
	boolean isPending(String status);
	boolean isTentative(Proposal proposal);
	boolean isTentative(String status);
	boolean isAppealed(Proposal proposal);
	boolean isAppealed(String status);
	boolean isFinal(Proposal proposal);
	boolean isFinal(String status);
	boolean isWithdrawn(Proposal proposal);
	boolean isWithdrawn(String status);
	
	boolean isAppealable(Proposal proposal);
	boolean isAppealable(String status);
	boolean isWithdrawable(Proposal proposal);
	boolean isWithdrawable(String status);
	boolean isEditable(Proposal proposal);
	boolean isEditable(String status);
}
