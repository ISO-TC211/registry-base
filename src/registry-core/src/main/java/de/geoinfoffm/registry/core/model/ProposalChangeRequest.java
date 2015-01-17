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
package de.geoinfoffm.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

/**
 * The class ProposalChangeRequest.
 *
 * @author Florian Esser
 */
@Access(AccessType.FIELD)
@Audited @Entity
public class ProposalChangeRequest extends de.geoinfoffm.registry.core.Entity
{
	@ManyToOne
	private Proposal proposal;
	
	@ManyToOne
	private Actor editor;
	
	private boolean editedBySubmitter;
	
	private int originalRevision;
	
	private boolean reviewed;
	
	protected ProposalChangeRequest() {
	}
	
	public ProposalChangeRequest(Proposal proposal, Actor editor, boolean editedBySubmitter, int originalRevision) {
		this.proposal = proposal;
		this.originalRevision = originalRevision;
		this.editor = editor;
		this.setEditedBySubmitter(editedBySubmitter);
		this.reviewed = false;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public Actor getEditor() {
		return editor;
	}

	public void setEditor(Actor editor) {
		this.editor = editor;
	}

	public boolean isEditedBySubmitter() {
		return editedBySubmitter;
	}

	public void setEditedBySubmitter(boolean editedBySubmitter) {
		this.editedBySubmitter = editedBySubmitter;
	}

	public int getOriginalRevision() {
		return originalRevision;
	}

	public void setOriginalRevision(int originalRevision) {
		this.originalRevision = originalRevision;
	}

	public boolean isReviewed() {
		return reviewed;
	}

	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}

}
