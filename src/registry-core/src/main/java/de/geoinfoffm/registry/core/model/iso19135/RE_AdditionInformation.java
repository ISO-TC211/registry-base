package de.geoinfoffm.registry.core.model.iso19135;

import java.util.Calendar;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.envers.Audited;

@XmlType(name = "RE_AdditionInformation_Type", namespace = "http://www.isotc211.org/2005/grg")
@XmlRootElement(name = "RE_AdditionInformation", namespace = "http://www.isotc211.org/2005/grg")
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@DiscriminatorValue("RE_AdditionInformation")
@Audited @Entity
public class RE_AdditionInformation extends RE_ProposalManagementInformation 
{
	public RE_AdditionInformation() {

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation#onDispositionAccepted()
	 */
	@Override
	protected void onDispositionAccepted() {
		this.getItem().setStatus(RE_ItemStatus.VALID);
		this.getItem().setDateAccepted(Calendar.getInstance().getTime());
		this.getItem().getRegister().addContainedItem(this.getItem());
	}
	
}//end RE_AdditionInformation