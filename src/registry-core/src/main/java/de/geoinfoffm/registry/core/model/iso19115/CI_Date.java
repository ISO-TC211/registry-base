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

import org.hibernate.envers.Audited;

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
@Audited @Embeddable
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