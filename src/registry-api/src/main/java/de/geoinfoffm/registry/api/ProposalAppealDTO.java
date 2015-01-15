/**
 * 
 */
package de.geoinfoffm.registry.api;

import java.util.UUID;

/**
 * @author Florian Esser
 *
 */
public class ProposalAppealDTO
{
	private UUID proposalUuid;
	private String justification;
	private String situation;
	private String impact;
	
	public ProposalAppealDTO() {
		
	}

	/**
	 * @return the proposalUuid
	 */
	public UUID getProposalUuid() {
		return proposalUuid;
	}

	/**
	 * @param proposalUuid the proposalUuid to set
	 */
	public void setProposalUuid(UUID proposalUuid) {
		this.proposalUuid = proposalUuid;
	}

	/**
	 * @return the justification
	 */
	public String getJustification() {
		return justification;
	}

	/**
	 * @param justification the justification to set
	 */
	public void setJustification(String justification) {
		this.justification = justification;
	}

	/**
	 * @return the situation
	 */
	public String getSituation() {
		return situation;
	}

	/**
	 * @param situation the situation to set
	 */
	public void setSituation(String situation) {
		this.situation = situation;
	}

	/**
	 * @return the impact
	 */
	public String getImpact() {
		return impact;
	}

	/**
	 * @param impact the impact to set
	 */
	public void setImpact(String impact) {
		this.impact = impact;
	}

}
