package de.geoinfoffm.registry.core.model.iso19115;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.geoinfoffm.registry.core.ValueObject;
import de.geoinfoffm.registry.core.model.DateAdapter;

/**
 * 
 * @created 10-Sep-2013 19:42:48
 */
@XmlType(name = "CI_Date_Type",  namespace = "http://www.isotc211.org/2005/gmd", 
		 propOrder = { "date", "dateType" })
@XmlRootElement(name = "CI_Date", namespace = "http://www.isotc211.org/2005/gmd")
@XmlAccessorType(XmlAccessType.FIELD)
@Embeddable
public class CI_Date extends ValueObject
{
	@XmlElement(name = "date",  namespace = "http://www.isotc211.org/2005/gmd", type = de.geoinfoffm.registry.core.model.iso19103.Date.class)
	@XmlJavaTypeAdapter(DateAdapter.class)
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@XmlElement(name = "dateType",  namespace = "http://www.isotc211.org/2005/gmd")
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "value", column = @Column(name = "dateType_value", length = 2000)),
		@AttributeOverride(name = "codeList", column = @Column(name = "dateType_codeList", length = 2000)),
		@AttributeOverride(name = "codeListValue", column = @Column(name = "dateType_codeListValue", length = 2000)),
		@AttributeOverride(name = "codeSpace", column = @Column(name = "dateType_codeSpace", length = 2000)),
		@AttributeOverride(name = "type", column = @Column(name = "dateType_type")),
	})
	private CI_DateTypeCode dateType;

	public CI_Date() {

	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public CI_DateTypeCode getDateType() {
		return dateType;
	}

	public void setDateType(CI_DateTypeCode dateType) {
		this.dateType = dateType;
	}

	public void finalize() throws Throwable {

	}
}//end CI_Date