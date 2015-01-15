package de.geoinfoffm.registry.core.model.iso19135;

import java.util.Calendar;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.envers.Audited;

@Access(AccessType.FIELD)
@DiscriminatorValue("RE_AmendmentInformation")
@Audited @Entity
public class RE_AmendmentInformation extends RE_ProposalManagementInformation {

	@Enumerated(EnumType.STRING)
	private RE_AmendmentType amendmentType;

	public RE_AmendmentInformation() {

	}

	/**
	 * @return the amendmentType
	 */
	public RE_AmendmentType getAmendmentType() {
		return amendmentType;
	}

	/**
	 * @param amendmentType the amendmentType to set
	 */
	public void setAmendmentType(RE_AmendmentType amendmentType) {
		this.amendmentType = amendmentType;
	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation#onDispositionAccepted()
	 */
	@Override
	protected void onDispositionAccepted() {
		RE_RegisterItem item = this.getItem();
		
		item.setDateAmended(Calendar.getInstance().getTime());
		switch (this.amendmentType) {
			case RETIREMENT:
				item.setStatus(RE_ItemStatus.RETIRED);
				break;
			case SUPERSESSION:
				item.setStatus(RE_ItemStatus.SUPERSEDED);
				break;
		}
	}
}//end RE_AmendmentInformation