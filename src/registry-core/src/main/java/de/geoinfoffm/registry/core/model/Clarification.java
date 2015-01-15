/**
 * 
 */
package de.geoinfoffm.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19135.RE_ClarificationInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;

/**
 * @author Florian.Esser
 *
 */
@Access(AccessType.FIELD)
@DiscriminatorValue("CLARIFICATION")
@Audited @Entity
public class Clarification extends SimpleProposal
{
	protected Clarification() {
		
	}
	
	public Clarification(RE_ClarificationInformation clarificationInformation) {
		super(clarificationInformation);
	}
	
	public String getProposedChange() {
		RE_ProposalManagementInformation pmi = (RE_ProposalManagementInformation)de.geoinfoffm.registry.core.Entity.unproxify(this.getProposalManagementInformation());
		return CharacterString.asString(((RE_ClarificationInformation)pmi).getProposedChange());
	}
	
	public void setProposedChange(String change) {
		RE_ProposalManagementInformation pmi = (RE_ProposalManagementInformation)de.geoinfoffm.registry.core.Entity.unproxify(this.getProposalManagementInformation());
		((RE_ClarificationInformation)pmi).setProposedChange(new CharacterString(change));
	}
}
