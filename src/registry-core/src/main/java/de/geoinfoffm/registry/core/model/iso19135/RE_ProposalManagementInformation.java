/**
 * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *     * Redistributions of source code must retain the above copyright
 *     	 notice, this list of conditions and the following disclaimer.

 *     * Redistributions in binary form must reproduce the above
 *     	 copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials
 *       provided with the distribution.

 *     * The names "German Federal Agency for Cartography and Geodesy",
 *       "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE",
 *       "GDI-DE Registry" and the names of other contributors must not
 *       be used to endorse or promote products derived from this
 *       software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE GERMAN
 * FEDERAL AGENCY FOR CARTOGRAPHY AND GEODESY BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.geoinfoffm.registry.core.model.iso19135;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.model.DateAdapter;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * 
 * @created 09-Sep-2013 19:13:17
 */
@XmlType(name = "AbstractRE_ProposalManagementInformation_Type", namespace = "http://www.isotc211.org/2005/grg",
		 propOrder = { "dateProposed", "justification", "status", "disposition", "dateDisposed", 
					   "controlBodyDecisionEvent", "controlBodyNotes", "registerManagerNotes",
					   "sponsor" })
@XmlRootElement(name = "AbstractRE_ProposalManagementInformation", namespace = "http://www.isotc211.org/2005/grg")
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
@Audited @javax.persistence.Entity
public abstract class RE_ProposalManagementInformation extends Entity
{
	@XmlElement(name = "dateProposed", namespace = "http://www.isotc211.org/2005/grg", type = de.geoinfoffm.registry.core.model.iso19103.Date.class)
	@XmlJavaTypeAdapter(DateAdapter.class)
	@Temporal(TemporalType.DATE)
	private Date dateProposed;

	@XmlElement(name = "justification", namespace = "http://www.isotc211.org/2005/grg", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@NotNull
	@Column(columnDefinition = "text")
	private String justification;
	
	@XmlElement(name = "status", namespace = "http://www.isotc211.org/2005/grg")
	@Enumerated(EnumType.STRING)
	private RE_DecisionStatus status;
	
	@XmlElement(name = "disposition", namespace = "http://www.isotc211.org/2005/grg")
	@Enumerated(EnumType.STRING)
	private RE_Disposition disposition;
	
	@XmlElement(name = "dateDisposed", namespace = "http://www.isotc211.org/2005/grg", type = de.geoinfoffm.registry.core.model.iso19103.Date.class)
	@XmlJavaTypeAdapter(DateAdapter.class)
	@Temporal(TemporalType.DATE)
	private Date dateDisposed;
	
	@XmlElement(name = "controlBodyDecisionEvent", namespace = "http://www.isotc211.org/2005/grg", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String controlBodyDecisionEvent;
	
	@XmlElement(name = "controlBodyNotes", namespace = "http://www.isotc211.org/2005/grg", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String controlBodyNotes;
	
	@XmlElement(name = "registerManagerNotes", namespace = "http://www.isotc211.org/2005/grg", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String registerManagerNotes;

	@XmlElement(name = "sponsor", namespace = "http://www.isotc211.org/2005/grg")
	@ManyToOne
	private RE_SubmittingOrganization sponsor;

	@JsonIgnore
	@XmlTransient
	@ManyToOne(cascade = CascadeType.PERSIST)
//	@JoinTable(name="RE_RegisterItem_ProposalManagementInfos",
//	 joinColumns=@JoinColumn(name="pmiId"),
//	 inverseJoinColumns=@JoinColumn(name="itemId")
//	)
	private RE_RegisterItem item;
	
	protected RE_ProposalManagementInformation() {

	}

	/**
	 * @return the dateProposed
	 */
	public Date getDateProposed() {
		return dateProposed;
	}

	/**
	 * @param dateProposed the dateProposed to set
	 */
	public void setDateProposed(Date dateProposed) {
		this.dateProposed = dateProposed;
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
	 * @return the status
	 */
	public RE_DecisionStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(RE_DecisionStatus status) {
		this.status = status;
	}

	/**
	 * @return the disposition
	 */
	public RE_Disposition getDisposition() {
		return disposition;
	}

	/**
	 * @param disposition the disposition to set
	 */
	public void setDisposition(RE_Disposition disposition) {
		this.disposition = disposition;
		this.setDateDisposed(Calendar.getInstance().getTime());
	}

	/**
	 * @return the dateDisposed
	 */
	public Date getDateDisposed() {
		return dateDisposed;
	}

	/**
	 * @param dateDisposed the dateDisposed to set
	 */
	protected void setDateDisposed(Date dateDisposed) {
		this.dateDisposed = dateDisposed;
	}

	/**
	 * @return the controlBodyDecisionEvent
	 */
	public String getControlBodyDecisionEvent() {
		return controlBodyDecisionEvent;
	}

	/**
	 * @param controlBodyDecisionEvent the controlBodyDecisionEvent to set
	 */
	public void setControlBodyDecisionEvent(String controlBodyDecisionEvent) {
		this.controlBodyDecisionEvent = controlBodyDecisionEvent;
	}

	/**
	 * @return the controlBodyNotes
	 */
	public String getControlBodyNotes() {
		return controlBodyNotes;
	}

	/**
	 * @param controlBodyNotes the controlBodyNotes to set
	 */
	public void setControlBodyNotes(String controlBodyNotes) {
		this.controlBodyNotes = controlBodyNotes;
	}

	/**
	 * @return the registerManagerNotes
	 */
	public String getRegisterManagerNotes() {
		return registerManagerNotes;
	}

	/**
	 * @param registerManagerNotes the registerManagerNotes to set
	 */
	public void setRegisterManagerNotes(String registerManagerNotes) {
		this.registerManagerNotes = registerManagerNotes;
	}

	/**
	 * @return the sponsor
	 */
	public RE_SubmittingOrganization getSponsor() {
		return sponsor;
	}

	/**
	 * @param sponsor the sponsor to set
	 */
	public void setSponsor(RE_SubmittingOrganization sponsor) {
		this.sponsor = sponsor;
	}

	/**
	 * @return the item
	 */
	public RE_RegisterItem getItem() {
		return this.item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(RE_RegisterItem item) {
		this.item = item;
	}
	
	public boolean isReviewed() {
		return this.getDateProposed() != null;
	}
	
	public void review(Date reviewDate) throws IllegalOperationException {
		if (this.isReviewed()) {
			throw new IllegalOperationException("The proposal was already reviewed.");
		}

		this.setDateProposed(reviewDate);
	}
	
	public boolean isPending() {
		return isReviewed() && this.getStatus().equals(RE_DecisionStatus.PENDING);
	}
	
	public void makeDisposition(RE_Disposition disposition) throws IllegalOperationException {
//		if (this.getStatus().equals(RE_DecisionStatus.FINAL)) {
//			throw new IllegalOperationException("Cannot make disposition on finalized proposal.");
//		}
		
		this.setDisposition(disposition);
		switch (disposition) {
			case ACCEPTED:
				this.setStatus(RE_DecisionStatus.FINAL);
				onDispositionAccepted();
				break;
			case WITHDRAWN:
				this.setStatus(RE_DecisionStatus.FINAL);
				onDispositionWithdrawn();
				break;
			case NOT_ACCEPTED:
				this.setStatus(RE_DecisionStatus.TENTATIVE);
				onDispositionNotAccepted();
				break;
		}
	}
	
	protected abstract void onDispositionAccepted();
	
	protected void onDispositionNotAccepted() { 
		// Nothing usually happens here
	}
	
	protected void onDispositionWithdrawn() {
		// Nothing usually happens here
	}
	
	public boolean isTentative() {
		return this.getStatus().equals(RE_DecisionStatus.TENTATIVE);
	}
	
	public void finalizeDisposition() throws IllegalOperationException {
		if (!isTentative()) {
			throw new IllegalOperationException(String.format("The disposition may only be finalized when the status is %s", RE_DecisionStatus.TENTATIVE.name()));
		}
		this.setStatus(RE_DecisionStatus.FINAL);
	}

	public boolean isFinal() {
		return this.getStatus().equals(RE_DecisionStatus.FINAL);
	}

	public boolean isAccepted() {
		return this.getDisposition().equals(RE_Disposition.ACCEPTED);
	}
	
	public boolean isNotAccepted() {
		return this.getDisposition().equals(RE_Disposition.NOT_ACCEPTED);
	}
	
	public boolean isWithdrawn() {
		return this.getDisposition().equals(RE_Disposition.WITHDRAWN);
	}

}//end RE_ProposalManagementInformation