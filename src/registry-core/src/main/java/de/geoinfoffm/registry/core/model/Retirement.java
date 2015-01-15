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

import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentType;

/**
 * @author Florian.Esser
 *
 */
@Access(AccessType.FIELD)
@DiscriminatorValue("RETIREMENT")
@Audited @Entity
public class Retirement extends SimpleProposal
{
	protected Retirement() {
		
	}
	
	public Retirement(RE_AmendmentInformation amendmentInformation) {
		super(amendmentInformation);
		
		if (!amendmentInformation.getAmendmentType().equals(RE_AmendmentType.RETIREMENT)) {
			throw new IllegalArgumentException("Amendment information must have type RETIREMENT");
		}
	}
}
