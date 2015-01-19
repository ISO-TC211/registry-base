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
package de.geoinfoffm.registry.api.iso;

import java.lang.annotation.Annotation;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.isotc211.iso19135.AbstractRE_ProposalManagementInformation_Type;
import org.isotc211.iso19135.RE_AdditionInformation_PropertyType;
import org.isotc211.iso19135.RE_AdditionInformation_Type;
import org.isotc211.iso19135.RE_AmendmentInformation_PropertyType;
import org.isotc211.iso19135.RE_AmendmentInformation_Type;
import org.isotc211.iso19135.RE_AmendmentType_PropertyType;
import org.isotc211.iso19135.RE_AmendmentType_Type;
import org.isotc211.iso19135.RE_ClarificationInformation_PropertyType;
import org.isotc211.iso19135.RE_ClarificationInformation_Type;
import org.isotc211.iso19135.RE_DecisionStatus_PropertyType;
import org.isotc211.iso19135.RE_DecisionStatus_Type;
import org.isotc211.iso19135.RE_Disposition_PropertyType;
import org.isotc211.iso19135.RE_Disposition_Type;
import org.isotc211.iso19135.RE_ItemClass_PropertyType;
import org.isotc211.iso19135.RE_ItemStatus_PropertyType;
import org.isotc211.iso19135.RE_ItemStatus_Type;
import org.isotc211.iso19135.RE_RegisterItem_PropertyType;
import org.isotc211.iso19135.RE_SubmittingOrganization_PropertyType;
import org.isotc211.iso19139.common.Boolean_PropertyType;
import org.isotc211.iso19139.common.CharacterString_PropertyType;
import org.isotc211.iso19139.common.Date_PropertyType;
import org.isotc211.iso19139.common.Integer_PropertyType;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentType;
import de.geoinfoffm.registry.core.model.iso19135.RE_ClarificationInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Disposition;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;

/**
 * The class IsoXml.
 *
 * @author Florian Esser
 */
public class IsoXml
{
	private static final org.isotc211.iso19139.common.ObjectFactory gcoObjectFactory = new org.isotc211.iso19139.common.ObjectFactory();
	private static final org.isotc211.iso19135.ObjectFactory grgObjectFactory = new org.isotc211.iso19135.ObjectFactory();

	private IsoXml() {
	}

	public static CharacterString_PropertyType characterString(String s) {
		CharacterString_PropertyType result = gcoObjectFactory.createCharacterString_PropertyType();
		if (!StringUtils.isEmpty(s)) {
			result.setCharacterString(gcoObjectFactory.createCharacterString(s));
		}
		
		return result;
	}
	
	public static String characterString(CharacterString_PropertyType cs) {
		return cs.getCharacterString().getValue().toString();
	}

	public static Boolean_PropertyType booleanProperty(Boolean b) {
		Boolean_PropertyType result = gcoObjectFactory.createBoolean_PropertyType();
		if (b != null) {
			result.setBoolean(b);
		}
		
		return result;
	}
	
	public static Date_PropertyType date(Date date) {
		Date_PropertyType result = gcoObjectFactory.createDate_PropertyType();
		if (date != null) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			result.setDate(df.format(date));
//			GregorianCalendar c = new GregorianCalendar();
//			c.setTime(date);
//			XMLGregorianCalendar xmlDate;
//			try {
//				xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
//				result.setDateTime(xmlDate);
//			}
//			catch (DatatypeConfigurationException e) {
//				e.printStackTrace();
//				result.setDate(date.toString());
//			}
		}
		
		return result;
	}
	
	public static Integer_PropertyType integer(BigInteger integer) {
		Integer_PropertyType result = gcoObjectFactory.createInteger_PropertyType();
		if (integer != null) {
			result.setInteger(integer);
		}
		
		return result;
	}

	public static Integer_PropertyType integer(Integer integer) {
		return integer(BigInteger.valueOf(integer));
	}
	
	public static Integer_PropertyType integer(Long longInt) {
		return integer(BigInteger.valueOf(longInt));
	}

	public static RE_ItemStatus_PropertyType itemStatus(RE_ItemStatus status) {
		return itemStatus(status.name()); // TODO Won't work
	}

	public static RE_ItemStatus_PropertyType itemStatus(String statusName) {
		RE_ItemStatus_PropertyType result = new RE_ItemStatus_PropertyType();
		result.setRE_ItemStatus(RE_ItemStatus_Type.fromValue(getXmlEnumValue(RE_ItemStatus.class, statusName)));
		
		return result;
	}
	
	public static String getXmlEnumValue(Class<? extends Enum> enumClass, String field) {
		try {
			XmlEnumValue valueAnnotation = enumClass.getField(field).getAnnotation(XmlEnumValue.class);
			if (valueAnnotation != null) {
				return valueAnnotation.value();
			}
		}
		catch (NoSuchFieldException | SecurityException e) {
			throw new IllegalArgumentException("Invalid status: " + field);
		}
		
		return field;
	}
	
	public static RE_Disposition_PropertyType disposition(String dispositionName) {
		RE_Disposition_PropertyType result = new RE_Disposition_PropertyType();
		result.setRE_Disposition(RE_Disposition_Type.fromValue(getXmlEnumValue(RE_Disposition.class, dispositionName)));
		
		return result;
	}

	public static RE_Disposition_PropertyType disposition(RE_Disposition disposition) {
		return (disposition != null) ? disposition(disposition.name()) : null; 
	}

	public static RE_DecisionStatus_PropertyType decisionStatus(String decisionStatusName) {
		RE_DecisionStatus_PropertyType result = new RE_DecisionStatus_PropertyType();
		result.setRE_DecisionStatus(RE_DecisionStatus_Type.fromValue(getXmlEnumValue(RE_DecisionStatus.class, decisionStatusName)));
		
		return result;
	}

	public static RE_DecisionStatus_PropertyType decisionStatus(RE_DecisionStatus decisionStatus) {
		return decisionStatus(decisionStatus.name()); 
	}

	public static RE_AmendmentType_PropertyType amendmentType(String amendmentTypeName) {
		RE_AmendmentType_PropertyType result = new RE_AmendmentType_PropertyType();
		result.setRE_AmendmentType(RE_AmendmentType_Type.fromValue(getXmlEnumValue(RE_AmendmentType.class, amendmentTypeName)));
		
		return result;
	}

	public static RE_AmendmentType_PropertyType amendmentType(RE_AmendmentType amendmentType) {
		return amendmentType(amendmentType.name()); 
	}

	public static RE_ItemClass_PropertyType itemClass(UUID itemClassUuid) {
		RE_ItemClass_PropertyType result = new RE_ItemClass_PropertyType();
		result.setUuidref(itemClassUuid.toString());
		
		return result;
	}
	
	public static RE_SubmittingOrganization_PropertyType submittingOrganization(UUID suborgUuid) {
		RE_SubmittingOrganization_PropertyType result = new RE_SubmittingOrganization_PropertyType();
		result.setUuidref(suborgUuid.toString());
		
		return result;
	}
	
	public static RE_AdditionInformation_PropertyType additionInformation(RE_AdditionInformation additionInformation) {
		RE_AdditionInformation_PropertyType result = new RE_AdditionInformation_PropertyType();
		
		RE_AdditionInformation_Type ai = new RE_AdditionInformation_Type();
		setProposalManagementInformationValues(additionInformation, ai);
		result.setRE_AdditionInformation(ai);
		
		return result;
	}
	
	public static RE_ClarificationInformation_PropertyType clarificationInformation(RE_ClarificationInformation clarificationInformation) {
		RE_ClarificationInformation_PropertyType result = new RE_ClarificationInformation_PropertyType();
		
		RE_ClarificationInformation_Type ci = new RE_ClarificationInformation_Type();
		setProposalManagementInformationValues(clarificationInformation, ci);
		ci.setProposedChange(characterString(CharacterString.asString(clarificationInformation.getProposedChange())));
		
		result.setRE_ClarificationInformation(ci);
		return result;
	}

	public static RE_AmendmentInformation_PropertyType amendmentInformation(RE_AmendmentInformation amendmentInformation) {
		RE_AmendmentInformation_PropertyType result = new RE_AmendmentInformation_PropertyType();
		
		RE_AmendmentInformation_Type ai = new RE_AmendmentInformation_Type();
		setProposalManagementInformationValues(amendmentInformation, ai);
		ai.setAmendmentType(amendmentType(amendmentInformation.getAmendmentType()));

		result.setRE_AmendmentInformation(ai);
		return result;
	}
	
	public static RE_RegisterItem_PropertyType registerItem(UUID itemUuid) {
		RE_RegisterItem_PropertyType result = new RE_RegisterItem_PropertyType();
		result.setUuidref(itemUuid.toString());
		
		return result;
	}
	
	private static class UuidRefType {
		private String uuidref;
		
		public UuidRefType() { }
		
		public UuidRefType(String uuidref) {
			this.uuidref = uuidref;
		}

		public String getUuidref() {
			return uuidref;
		}

		public void setUuidref(String uuidref) {
			this.uuidref = uuidref;
		}
	}
	
	public static <P> P uuidRef(UUID uuid, Class<P> propertyType) {
		P result = BeanUtils.instantiate(propertyType);
		UuidRefType ref = new UuidRefType(uuid.toString());
		BeanUtils.copyProperties(ref, result);
		
		return result;
	}

	private static void setProposalManagementInformationValues(RE_ProposalManagementInformation pmi, AbstractRE_ProposalManagementInformation_Type target) {
		target.setControlBodyDecisionEvent(characterString(pmi.getControlBodyDecisionEvent()));
		target.setControlBodyNotes(characterString(pmi.getControlBodyNotes()));
		target.setDateDisposed(date(pmi.getDateDisposed()));
		target.setDateProposed(date(pmi.getDateProposed()));
		target.setDisposition(disposition(pmi.getDisposition()));
		target.setJustification(characterString(pmi.getJustification()));
		target.setRegisterManagerNotes(characterString(pmi.getRegisterManagerNotes()));
		target.setSponsor(submittingOrganization(pmi.getSponsor().getUuid()));
		target.setStatus(decisionStatus(pmi.getStatus()));
		target.setUuid(pmi.getUuid().toString());
	}
}
