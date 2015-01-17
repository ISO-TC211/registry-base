///**
// * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
// * All rights reserved.
// * 
// * Redistribution and use in source and binary forms, with or without
// * modification, are permitted provided that the following conditions 
// * are met:
// *     * Redistributions of source code must retain the above copyright
// *     	 notice, this list of conditions and the following disclaimer.
// 
// *     * Redistributions in binary form must reproduce the above 
// *     	 copyright notice, this list of conditions and the following 
// *       disclaimer in the documentation and/or other materials 
// *       provided with the distribution.
// 
// *     * The names "German Federal Agency for Cartography and Geodesy", 
// *       "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE", 
// *       "GDI-DE Registry" and the names of other contributors must not 
// *       be used to endorse or promote products derived from this 
// *       software without specific prior written permission.
// *       
// * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
// * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
// * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE GERMAN 
// * FEDERAL AGENCY FOR CARTOGRAPHY AND GEODESY BE LIABLE FOR ANY DIRECT, 
// * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
// * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
// * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
// * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
// * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
// * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
// * THE POSSIBILITY OF SUCH DAMAGE.
// */
//package de.geoinfoffm.registry.core.model;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//
//import javax.persistence.Access;
//import javax.persistence.AccessType;
//import javax.persistence.CascadeType;
//import javax.persistence.Inheritance;
//import javax.persistence.InheritanceType;
//import javax.persistence.ManyToMany;
//import javax.persistence.ManyToOne;
//
//import org.hibernate.envers.Audited;
//
///**
// * A {@link ProposalGroup} that contains a hierarchy of proposals which cannot be proposed separately.
// * 
// * @author Florian Esser
// *
// */
//@Access(AccessType.FIELD)
//@Inheritance(strategy = InheritanceType.JOINED)
//@Audited @javax.persistence.Entity
//public class HierarchicalProposal extends ProposalGroup
//{
//	private static final long serialVersionUID = 1354722605626300072L;
//
//	@ManyToOne
//	private Proposal primaryProposal;
//	
//	@ManyToMany(cascade = CascadeType.ALL)
//	private List<Proposal> dependentProposals;
//	
//	protected HierarchicalProposal() {
//		super();
//		this.setTitle("");
//	}
//
//	public HierarchicalProposal(String title) {
//		super(title);
//	}
//
//	public HierarchicalProposal(NameBuildingStrategy strategy) {
//		super(strategy);
//	}
//
//	public <P extends Proposal> HierarchicalProposal(SimpleProposal primaryProposal, List<P> dependentProposals) {
//		super(dependentProposals);
//		this.addProposal(primaryProposal);
//		
//		this.primaryProposal = primaryProposal;
//		this.dependentProposals = new ArrayList<>();
//		this.dependentProposals.addAll(dependentProposals);
//	}
//
//	public <P extends Proposal> HierarchicalProposal(Collection<P> proposals, NameBuildingStrategy strategy) {
//		super(proposals, strategy);
//		// TODO Auto-generated constructor stub
//	}
//
//	public <P extends Proposal> HierarchicalProposal(Collection<P> proposals, String name) {
//		super(proposals, name);
//		// TODO Auto-generated constructor stub
//	}
//
//	/**
//	 * Because of the mutual dependencies between the proposed items, editing
//	 * a hierarchichal proposal is in most cases undesirable.
//	 * 
//	 * Beispiel: CodeList[1]<-->[n]Code
//	 * Sobald das Proposal eingereicht wird, werden Items für alle Codes angelegt. Soll im Nachhinein
//	 * ein Code aus dem Proposal entfernt werden, kann die Beziehung zwischen entferntem Codes und CodeList
//	 * aber nicht aufgelöst werden, da diese aus Sicht des Codes eine nicht-optionale Beziehung ist.
//	 * Der entfernte Code kann auch nicht einfach vollständig gelöscht werden, da dies auf Basis der ISO 19135
//	 * nicht zulässig ist.
//	 */
//	@Override
//	public boolean isEditable() {
//		return false;
//	}
//
//	public Proposal getPrimaryProposal() {
//		return primaryProposal;
//	}
//
//	public void setPrimaryProposal(Proposal primaryProposal) {
//		this.primaryProposal = primaryProposal;
//	}
//
//	public List<Proposal> getDependentProposals() {
//		return dependentProposals;
//	}
//
//	public void setDependentProposals(List<Proposal> dependentProposals) {
//		this.dependentProposals = dependentProposals;
//	}
//
//}
