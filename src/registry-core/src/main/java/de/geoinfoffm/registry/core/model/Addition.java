/**
 * 
 */
package de.geoinfoffm.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

/**
 * @author Florian.Esser
 *
 */
@Access(AccessType.FIELD)
@DiscriminatorValue("ADDITION")
@Audited @Entity
public class Addition extends SimpleProposal
{
	protected Addition() {
	}
	
	public Addition(RE_AdditionInformation additionInformation) {
		super(additionInformation);
	}
	
	public static Addition createAddition(RE_RegisterItem proposedItem, RE_SubmittingOrganization sponsor, String justification,
			String registerManagerNotes, String controlBodyNotes) throws InvalidProposalException {

		RE_AdditionInformation additionInformation = new RE_AdditionInformation();
		// The dateProposed property must not be set here because the proposal
		// process will not start before the proposal was reviewed by the 
		// register manager (see sec. 6.2.6.3 of ISO 19135).
		// Due to technical considerations the proposal management record is created
		// at this point in time 
		additionInformation.setSponsor(sponsor);
		additionInformation.setStatus(RE_DecisionStatus.PENDING);
		additionInformation.setJustification(justification);
		additionInformation.setRegisterManagerNotes(registerManagerNotes);
		additionInformation.setControlBodyNotes(controlBodyNotes);
		additionInformation.setItem(proposedItem);
		
		return new Addition(additionInformation);
	}
}
