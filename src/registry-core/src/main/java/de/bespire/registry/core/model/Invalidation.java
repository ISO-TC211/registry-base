package de.bespire.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentType;

/**
 * @author Florian Esser
 *
 */
@Access(AccessType.FIELD)
@DiscriminatorValue("INVALIDATION")
@Audited @Entity
public class Invalidation extends SimpleProposal
{
	protected Invalidation() { }
	
	public Invalidation(RE_AmendmentInformation amendmentInformation) {
		super(amendmentInformation);
		
		if (!amendmentInformation.getAmendmentType().equals(RE_AmendmentType.INVALIDATION)) {
			throw new IllegalArgumentException(String.format("Amendment information must have type %s", RE_AmendmentType.INVALIDATION.name()));
		}
	}

}
