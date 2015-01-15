/**
 * 
 */
package de.geoinfoffm.registry.core.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19135.RE_Disposition;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;

/**
 * @author Florian.Esser
 *
 */
@Audited @javax.persistence.Entity
public class Appeal extends Entity
{
	public enum AppealDisposition {
		PENDING,
		ACCEPTED,
		NOT_ACCEPTED
	}
	
	@OneToOne
	private Proposal appealedProposal;
	
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date appealDate;
	
	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "situation"))
	@NotNull
	private CharacterString situation;
	
	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "justification"))
	@NotNull
	private CharacterString justification;
	
	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "impact"))
	@NotNull
	private CharacterString impact;

	@Enumerated(EnumType.STRING)
	@NotNull
	private AppealDisposition disposition;
	
	@Temporal(TemporalType.DATE)
	private Date dispositionDate;
	
	protected Appeal() {
		
	}
	
	public Appeal(Proposal appealedProposal, String justification, String situation, String impact) {
		this.setAppealDate(Calendar.getInstance().getTime());
		this.setJustification(new CharacterString(justification));
		this.setSituation(new CharacterString(situation));
		this.setImpact(new CharacterString(impact));
		this.setDisposition(AppealDisposition.PENDING);
		this.setAppealedProposal(appealedProposal);						
	}
	
	public void accept(Date dispositionDate) throws IllegalOperationException {
		this.setDisposition(AppealDisposition.ACCEPTED);
		this.setDispositionDate(dispositionDate);
		appealedProposal.accept();
	}
	
	public void reject(Date dispositionDate) throws IllegalOperationException {
		this.setDisposition(AppealDisposition.NOT_ACCEPTED);
		this.setDispositionDate(dispositionDate);
		appealedProposal.conclude();	
	}

	/**
	 * @return the appealedProposal
	 */
	public Proposal getAppealedProposal() {
		return appealedProposal;
	}

	/**
	 * @param appealedProposal the appealedProposal to set
	 */
	public void setAppealedProposal(Proposal appealedProposal) {
		this.appealedProposal = appealedProposal;
	}

	/**
	 * @return the appealDate
	 */
	public Date getAppealDate() {
		return appealDate;
	}

	/**
	 * @param appealDate the appealDate to set
	 */
	public void setAppealDate(Date appealDate) {
		this.appealDate = appealDate;
	}

	/**
	 * @return the situation
	 */
	public CharacterString getSituation() {
		return situation;
	}

	/**
	 * @param situation the situation to set
	 */
	public void setSituation(CharacterString situation) {
		this.situation = situation;
	}

	/**
	 * @return the justification
	 */
	public CharacterString getJustification() {
		return justification;
	}

	/**
	 * @param justification the justification to set
	 */
	public void setJustification(CharacterString justification) {
		this.justification = justification;
	}

	/**
	 * @return the impact
	 */
	public CharacterString getImpact() {
		return impact;
	}

	/**
	 * @param impact the impact to set
	 */
	public void setImpact(CharacterString impact) {
		this.impact = impact;
	}

	/**
	 * @return the appealDisposition
	 */
	public AppealDisposition getDisposition() {
		return disposition;
	}

	/**
	 * @param disposition the appealDisposition to set
	 */
	public void setDisposition(AppealDisposition disposition) {
		this.disposition = disposition;
	}

	/**
	 * @return the appealDispositionDate
	 */
	public Date getDispositionDate() {
		return dispositionDate;
	}

	/**
	 * @param dispositionDate the appealDispositionDate to set
	 */
	public void setDispositionDate(Date dispositionDate) {
		this.dispositionDate = dispositionDate;
	}
	
	public boolean isPending() {
		return this.disposition.equals(AppealDisposition.PENDING);
	}

	public boolean isAccepted() {
		return this.disposition.equals(AppealDisposition.ACCEPTED);
	}

	public boolean isNotAccepted() {
		return this.disposition.equals(AppealDisposition.NOT_ACCEPTED);
	}

	public boolean isDecided() {
		return this.isAccepted() || this.isNotAccepted();
	}

}
