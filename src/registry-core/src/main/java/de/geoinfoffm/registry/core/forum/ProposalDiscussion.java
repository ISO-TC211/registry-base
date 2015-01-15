/**
 * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *     * Redistributions of source code must retain the above copyright
 *     	 notice, this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above
 *     	 copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
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
package de.geoinfoffm.registry.core.forum;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.Actor;
import de.geoinfoffm.registry.core.model.Proposal;

@Access(AccessType.FIELD)
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "discussedProposal", "discussedStatus" }))
@Audited @Entity
public class ProposalDiscussion extends Discussion
{
	public enum DiscussionType {
		SUBMITTER,
		CONTROLBODY
	}
	
	@ManyToOne
	private Proposal discussedProposal;
	
	@Enumerated(EnumType.STRING)
	private DiscussionType discussionType;
	
	@ManyToOne
	private Actor owner;

	// Maps an invitee's e-mail address to his access secret
	@ElementCollection
	private Map<String, String> invitees;
	
	protected ProposalDiscussion() { }
	
	public ProposalDiscussion(Actor owner, Proposal discussedProposal, DiscussionType discussionType) {
		this.owner = owner;
		this.discussedProposal = discussedProposal;
		this.discussionType = discussionType;
	}

	public Proposal getDiscussedProposal() {
		return discussedProposal;
	}

	public void setDiscussedProposal(Proposal discussedProposal) {
		this.discussedProposal = discussedProposal;
	}

	public Actor getOwner() {
		return owner;
	}

	public void setOwner(Actor owner) {
		this.owner = owner;
	}

	public Map<String, String> getInvitees() {
		return invitees;
	}

	protected void setInvitees(Map<String, String> invitees) {
		this.invitees = invitees;
	}
	
	public String addInvitee(String emailAddress) {
		if (this.invitees == null) {
			this.invitees = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		}
		
		if (this.invitees.containsKey(emailAddress)) {
			return this.invitees.get(emailAddress);
		}
		else {
			String secret = RandomStringUtils.random(10, true, true);
			this.invitees.put(emailAddress, secret);
			
			return secret;
		}
	}
	
	public boolean isInvited(String emailAddress) {
		return this.invitees.containsKey(emailAddress);
	}
	
	public String getInviteeByToken(String token) {
		for (String mail : this.invitees.keySet()) {
			String inviteeSecret = this.invitees.get(mail);
			if (inviteeSecret.equalsIgnoreCase(token)) {
				return mail;
			}
		}
		
		return null;
	}
	
	public String getTokenByInvitee(String invitee) {
		return this.invitees.get(invitee);
	}

	public DiscussionType getDiscussionType() {
		return discussionType;
	}

	public void setDiscussionType(DiscussionType discussionType) {
		this.discussionType = discussionType;
	}
}
