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

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.isotc211.iso19135.AbstractRE_ProposalManagementInformation_Type;
import org.isotc211.iso19135.RE_AdditionInformation_PropertyType;
import org.isotc211.iso19135.RE_AdditionInformation_Type;
import org.isotc211.iso19135.RE_AlternativeExpression_PropertyType;
import org.isotc211.iso19135.RE_AlternativeExpression_Type;
import org.isotc211.iso19135.RE_AlternativeName_PropertyType;
import org.isotc211.iso19135.RE_AlternativeName_Type;
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
import org.isotc211.iso19135.RE_FieldOfApplication_PropertyType;
import org.isotc211.iso19135.RE_FieldOfApplication_Type;
import org.isotc211.iso19135.RE_ItemClass_PropertyType;
import org.isotc211.iso19135.RE_ItemClass_Type;
import org.isotc211.iso19135.RE_ItemStatus_PropertyType;
import org.isotc211.iso19135.RE_ItemStatus_Type;
import org.isotc211.iso19135.RE_Locale_PropertyType;
import org.isotc211.iso19135.RE_Locale_Type;
import org.isotc211.iso19135.RE_ProposalManagementInformation_PropertyType;
import org.isotc211.iso19135.RE_ReferenceSource_PropertyType;
import org.isotc211.iso19135.RE_ReferenceSource_Type;
import org.isotc211.iso19135.RE_Reference_PropertyType;
import org.isotc211.iso19135.RE_Reference_Type;
import org.isotc211.iso19135.RE_RegisterItem_PropertyType;
import org.isotc211.iso19135.RE_RegisterItem_Type;
import org.isotc211.iso19135.RE_RegisterManager_PropertyType;
import org.isotc211.iso19135.RE_RegisterManager_Type;
import org.isotc211.iso19135.RE_RegisterOwner_PropertyType;
import org.isotc211.iso19135.RE_RegisterOwner_Type;
import org.isotc211.iso19135.RE_Register_Type;
import org.isotc211.iso19135.RE_SimilarityToSource_PropertyType;
import org.isotc211.iso19135.RE_SubmittingOrganization_PropertyType;
import org.isotc211.iso19135.RE_SubmittingOrganization_Type;
import org.isotc211.iso19135.RE_Version_PropertyType;
import org.isotc211.iso19135.RE_Version_Type;
import org.isotc211.iso19139.common.Boolean_PropertyType;
import org.isotc211.iso19139.common.CharacterString_PropertyType;
import org.isotc211.iso19139.common.CodeListValue_Type;
import org.isotc211.iso19139.common.Date_PropertyType;
import org.isotc211.iso19139.common.Integer_PropertyType;
import org.isotc211.iso19139.metadata.CI_Address_PropertyType;
import org.isotc211.iso19139.metadata.CI_Address_Type;
import org.isotc211.iso19139.metadata.CI_Citation_PropertyType;
import org.isotc211.iso19139.metadata.CI_Citation_Type;
import org.isotc211.iso19139.metadata.CI_Contact_PropertyType;
import org.isotc211.iso19139.metadata.CI_Contact_Type;
import org.isotc211.iso19139.metadata.CI_DateTypeCode_PropertyType;
import org.isotc211.iso19139.metadata.CI_Date_PropertyType;
import org.isotc211.iso19139.metadata.CI_Date_Type;
import org.isotc211.iso19139.metadata.CI_OnLineFunctionCode_PropertyType;
import org.isotc211.iso19139.metadata.CI_OnlineResource_PropertyType;
import org.isotc211.iso19139.metadata.CI_OnlineResource_Type;
import org.isotc211.iso19139.metadata.CI_PresentationFormCode_PropertyType;
import org.isotc211.iso19139.metadata.CI_ResponsibleParty_PropertyType;
import org.isotc211.iso19139.metadata.CI_ResponsibleParty_Type;
import org.isotc211.iso19139.metadata.CI_RoleCode_PropertyType;
import org.isotc211.iso19139.metadata.CI_Series_PropertyType;
import org.isotc211.iso19139.metadata.CI_Series_Type;
import org.isotc211.iso19139.metadata.CI_Telephone_PropertyType;
import org.isotc211.iso19139.metadata.CI_Telephone_Type;
import org.isotc211.iso19139.metadata.LanguageCode_PropertyType;
import org.isotc211.iso19139.metadata.MD_CharacterSetCode_PropertyType;
import org.isotc211.iso19139.metadata.URL_PropertyType;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.geoinfoffm.registry.api.ProposalDtoFactory;
import de.geoinfoffm.registry.api.RegisterItemViewBean;
import de.geoinfoffm.registry.api.ViewBeanFactory;
import de.geoinfoffm.registry.core.model.iso00639.LanguageCode;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19103.CodeListValue;
import de.geoinfoffm.registry.core.model.iso19115.CI_Address;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;
import de.geoinfoffm.registry.core.model.iso19115.CI_Contact;
import de.geoinfoffm.registry.core.model.iso19115.CI_Date;
import de.geoinfoffm.registry.core.model.iso19115.CI_DateTypeCode;
import de.geoinfoffm.registry.core.model.iso19115.CI_OnLineFunctionCode;
import de.geoinfoffm.registry.core.model.iso19115.CI_OnlineResource;
import de.geoinfoffm.registry.core.model.iso19115.CI_PresentationFormCode;
import de.geoinfoffm.registry.core.model.iso19115.CI_ResponsibleParty;
import de.geoinfoffm.registry.core.model.iso19115.CI_RoleCode;
import de.geoinfoffm.registry.core.model.iso19115.CI_Series;
import de.geoinfoffm.registry.core.model.iso19115.CI_Telephone;
import de.geoinfoffm.registry.core.model.iso19115.MD_CharacterSetCode;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_AlternativeExpression;
import de.geoinfoffm.registry.core.model.iso19135.RE_AlternativeName;
import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentType;
import de.geoinfoffm.registry.core.model.iso19135.RE_ClarificationInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Disposition;
import de.geoinfoffm.registry.core.model.iso19135.RE_FieldOfApplication;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Locale;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_Reference;
import de.geoinfoffm.registry.core.model.iso19135.RE_ReferenceSource;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterManager;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterOwner;
import de.geoinfoffm.registry.core.model.iso19135.RE_SimilarityToSource;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.model.iso19135.RE_Version;

/**
 * The class IsoXml.
 *
 * @author Florian Esser
 */
@Component
public class IsoXmlFactory
{
	private static final org.isotc211.iso19139.common.ObjectFactory gcoObjectFactory = new org.isotc211.iso19139.common.ObjectFactory();
	private static final org.isotc211.iso19139.metadata.ObjectFactory gmdObjectFactory = new org.isotc211.iso19139.metadata.ObjectFactory(); 
	private static final org.isotc211.iso19135.ObjectFactory grgObjectFactory = new org.isotc211.iso19135.ObjectFactory();

	private static final String NIL_REASON = "missing";
	
	@Autowired
	private ViewBeanFactory viewBeanFactory;
	
	@Autowired
	private ProposalDtoFactory dtoFactory;

	public IsoXmlFactory() {
	}

	public static CharacterString_PropertyType characterString(String s) {
		CharacterString_PropertyType result = gcoObjectFactory.createCharacterString_PropertyType();
		if (!StringUtils.isEmpty(s)) {
			result.setCharacterString(gcoObjectFactory.createCharacterString(s));
		}
		
		return result;
	}
	
	public static CharacterString_PropertyType characterString(CharacterString cs) {
		return characterString(CharacterString.asString(cs));
	}
	
	public static String characterString(CharacterString_PropertyType cs) {
		if (cs.isSetCharacterString() && cs.getCharacterString().getValue() != null) {
			return cs.getCharacterString().getValue().toString();
		}
		else { 
			return null;
		}
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
	
	public static Date date(Date_PropertyType dateProperty) {
		if (dateProperty.getDateTime() != null) {
			XMLGregorianCalendar xmlDate = dateProperty.getDateTime();
			return xmlDate.toGregorianCalendar().getTime();
		}
		else if (!StringUtils.isEmpty(dateProperty.getDate())) {
			DateTime date = ISODateTimeFormat.date().parseDateTime(dateProperty.getDate());
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(date.getMillis());
			
			return cal.getTime();
		}
		else {
			return null;
		}
	}
	
	public static Integer_PropertyType integer(BigInteger integer) {
		Integer_PropertyType result = gcoObjectFactory.createInteger_PropertyType();
		if (integer != null) {
			result.setInteger(integer);
		}
		
		return result;
	}

	public static Integer_PropertyType integer(Integer integer) {
		return integer(integer == null ? null : BigInteger.valueOf(integer));
	}
	
	public static Integer_PropertyType integer(Long longInt) {
		return integer(longInt == null ? null : BigInteger.valueOf(longInt));
	}

	public static RE_ItemStatus_PropertyType itemStatus(RE_ItemStatus status) {
		return itemStatus(status == null ? null : status.name()); // TODO Won't work
	}

	public static RE_ItemStatus_PropertyType itemStatus(String statusName) {
		RE_ItemStatus_PropertyType result = new RE_ItemStatus_PropertyType();
		result.setRE_ItemStatus(RE_ItemStatus_Type.fromValue(getXmlEnumValue(RE_ItemStatus.class, statusName)));
		
		return result;
	}
	
	public static RE_ItemStatus itemStatus(RE_ItemStatus_PropertyType status) {
		return getEnum(RE_ItemStatus.class, status.getRE_ItemStatus().value());
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
		
		throw new RuntimeException(String.format("No @XmlEnumValue annotation for '%s' exists in enum '%s'", field, enumClass.getCanonicalName()));
	}
	
	public static <E extends Enum<E>> E getEnum(Class<E> enumClass, String xmlEnumValue) {
		try {
			return Enum.valueOf(enumClass, xmlEnumValue);
		}
		catch (Throwable t) {
			// Ignore
		}
		
		for (Field field : enumClass.getFields()) {
			XmlEnumValue valueAnnotation = field.getAnnotation(XmlEnumValue.class);
			if (valueAnnotation == null) {
				continue;
			}
			else if (valueAnnotation.value().equals(xmlEnumValue)) {
				return Enum.valueOf(enumClass, field.getName());
			}
		}
		
		throw new RuntimeException(String.format("No @XmlEnumValue annotation for '%s' exists in enum '%s'", xmlEnumValue, enumClass.getCanonicalName()));
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
	
	public static RE_ReferenceSource_PropertyType referenceSource(RE_ReferenceSource source) {
		RE_ReferenceSource_PropertyType result = grgObjectFactory.createRE_ReferenceSource_PropertyType();
		
		if (source == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		RE_ReferenceSource_Type sourceType = grgObjectFactory.createRE_ReferenceSource_Type();
		sourceType.setCitation(citation(source.getCitation()));
		
		result.setRE_ReferenceSource(sourceType);
		return result;
	}
	
	public static RE_SimilarityToSource_PropertyType similarity(RE_SimilarityToSource similiarity) {
		RE_SimilarityToSource_PropertyType result = grgObjectFactory.createRE_SimilarityToSource_PropertyType();
		
		if (similiarity == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		result.setRE_SimilarityToSource(code(similiarity));
		return result;
	}
	
	public static RE_Reference_PropertyType reference(RE_Reference reference) {
		RE_Reference_PropertyType result = grgObjectFactory.createRE_Reference_PropertyType();
		
		if (reference == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		RE_Reference_Type refType = grgObjectFactory.createRE_Reference_Type();
		refType.setUuid(reference.getUuid().toString());
		refType.setItemIdentifierAtSource(characterString(reference.getItemIdentifierAtSource()));
		refType.setNotes(characterString(reference.getNotes()));
		refType.setReferenceText(characterString(reference.getReferenceText()));
		refType.setSimilarity(similarity(reference.getSimilarity()));
		refType.setSourceCitation(referenceSource(reference.getSourceCitation()));
		
		result.setRE_Reference(refType);
		return result;
	}
	
	public static RE_FieldOfApplication_PropertyType fieldOfApplication(RE_FieldOfApplication field) {
		RE_FieldOfApplication_PropertyType result = grgObjectFactory.createRE_FieldOfApplication_PropertyType();
		
		if (field == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		RE_FieldOfApplication_Type fieldType = grgObjectFactory.createRE_FieldOfApplication_Type();
		fieldType.setDescription(characterString(field.getDescription()));
		fieldType.setName(characterString(field.getName()));
		
		result.setRE_FieldOfApplication(fieldType);
		return result;
	}
	
	
	public static RE_AlternativeExpression_PropertyType alternativeExpression(RE_AlternativeExpression expression) {
		RE_AlternativeExpression_PropertyType result = grgObjectFactory.createRE_AlternativeExpression_PropertyType();
		
		if (expression == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		RE_AlternativeExpression_Type expressionType = grgObjectFactory.createRE_AlternativeExpression_Type();
		
		expressionType.setDefinition(characterString(expression.getDefinition()));
		expressionType.setDescription(characterString(expression.getDescription()));
		expressionType.setLocale(locale(expression.getLocale()));
		expressionType.setName(characterString(expression.getName()));
		if (expressionType.getFieldOfApplication().isEmpty()) {
			expressionType.getFieldOfApplication().add(fieldOfApplication(null));
		}
		for (RE_FieldOfApplication field : expression.getFieldOfApplication()) {
			expressionType.getFieldOfApplication().add(fieldOfApplication(field));
		}
		
		result.setRE_AlternativeExpression(expressionType);
		return result;
	}

	public RE_RegisterItem_PropertyType registerItem(RE_RegisterItem item) {
		RE_RegisterItem_PropertyType result = new RE_RegisterItem_PropertyType();
		
		if (item == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		RegisterItemViewBean viewBean = viewBeanFactory.getViewBean(item);
		RE_RegisterItem_Type itemType = viewBean.toXmlType(dtoFactory);
		
		itemType.setDateAccepted(date(item.getDateAccepted()));
		itemType.setDateAmended(date(item.getDateAmended()));
		itemType.setDefinition(characterString(item.getDefinition()));
		itemType.setDescription(characterString(item.getDescription()));
		itemType.setItemClass(itemClass(item.getItemClass().getUuid()));
		itemType.setItemIdentifier(integer(item.getItemIdentifier()));
		itemType.setName(characterString(item.getName()));
		itemType.setSpecificationSource(reference(item.getSpecificationSource()));
		itemType.setStatus(itemStatus(item.getStatus()));
		itemType.setUuid(item.getUuid().toString());
		
		itemType.getAdditionInformation().clear();
		for (RE_AdditionInformation additionInfo : item.getAdditionInformation()) {
			itemType.getAdditionInformation().add(additionInformation(additionInfo));			
		}
		
		itemType.getClarificationInformation().clear();
		for (RE_ClarificationInformation clarificationInfo : item.getClarificationInformation()) {
			itemType.getClarificationInformation().add(clarificationInformation(clarificationInfo));
		}
		
		itemType.getAmendmentInformation().clear();
		for (RE_AmendmentInformation amendmentInfo : item.getAmendmentInformation()) {
			itemType.getAmendmentInformation().add(amendmentInformation(amendmentInfo));
		}
		
		itemType.getAlternativeExpressions().clear();
		for (RE_AlternativeExpression alternativeExpression : item.getAlternativeExpressions()) {
			itemType.getAlternativeExpressions().add(alternativeExpression(alternativeExpression));
		}
		
		itemType.getFieldOfApplication().clear();
		for (RE_FieldOfApplication field : item.getFieldOfApplication()) {
			itemType.getFieldOfApplication().add(fieldOfApplication(field));
		}
		
		itemType.getPredecessor().clear();
		for (RE_RegisterItem predecessor : item.getPredecessors()) {
			itemType.getPredecessor().add(registerItem(predecessor.getUuid()));
		}
		
		itemType.getSuccessor().clear();
		for (RE_RegisterItem successor : item.getSuccessors()) {
			itemType.getSuccessor().add(registerItem(successor.getUuid()));
		}

		JAXBElement<RE_RegisterItem_Type> jaxbElement = grgObjectFactory.createRE_RegisterItem(itemType);
		result.setRE_RegisterItem(jaxbElement);
		return result;
	}

	public static URL_PropertyType url(String url) {
		URL_PropertyType result = gmdObjectFactory.createURL_PropertyType();
		result.setURL(url);
		
		return result;
	}
	
	public static CI_OnlineResource_PropertyType onlineResource(String url) {
		CI_OnlineResource_PropertyType result = gmdObjectFactory.createCI_OnlineResource_PropertyType();
		
		if (StringUtils.isEmpty(url)) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		CI_OnlineResource_Type or = gmdObjectFactory.createCI_OnlineResource_Type();
		or.setLinkage(url(url));
		
		return result;
	}
	
	public static CI_OnlineResource_PropertyType onlineResource(CI_OnlineResource resource) {
		CI_OnlineResource_PropertyType result = gmdObjectFactory.createCI_OnlineResource_PropertyType();
		
		if (resource == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		CI_OnlineResource_Type resourceType = gmdObjectFactory.createCI_OnlineResource_Type();
		resourceType.setApplicationProfile(characterString(resource.getApplicationProfile()));
		resourceType.setDescription(characterString(resource.getDescription()));
		resourceType.setFunction(onlineFunctionCode(resource.getFunction()));
		resourceType.setLinkage(url(resource.getLinkage()));
		resourceType.setName(characterString(resource.getName()));
		resourceType.setProtocol(characterString(resource.getProtocol()));
		resourceType.setUuid(resource.getUuid().toString());
		
		result.setCI_OnlineResource(resourceType);
		return result;
	}
	
	public static CodeListValue_Type code(CodeListValue code) {
		CodeListValue_Type codeType = gcoObjectFactory.createCodeListValue_Type();
		codeType.setCodeList(code.getCodeList());
		codeType.setCodeListValue(code.getCodeListValue());
		codeType.setCodeSpace(code.getCodeSpace());
		codeType.setValue(code.getCodeListValue());
		
		return codeType;
	}
	
	public static CI_DateTypeCode_PropertyType dateTypeCode(CI_DateTypeCode code) {
		CI_DateTypeCode_PropertyType result = gmdObjectFactory.createCI_DateTypeCode_PropertyType();
		
		if (code == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		result.setCI_DateTypeCode(code(code));
		return result;
	}

	public static CI_PresentationFormCode_PropertyType presentationFormCode(CI_PresentationFormCode code) {
		CI_PresentationFormCode_PropertyType result = gmdObjectFactory.createCI_PresentationFormCode_PropertyType();
		
		if (code == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		result.setCI_PresentationFormCode(code(code));
		return result;
	}

	public static LanguageCode_PropertyType languageCode(String code) {
		LanguageCode_PropertyType result = gmdObjectFactory.createLanguageCode_PropertyType();
		
		CodeListValue_Type codeType = gcoObjectFactory.createCodeListValue_Type();
		result.setLanguageCode(codeType);
		
		return result;
	}
	
	public static LanguageCode_PropertyType languageCode(LanguageCode language) {
		LanguageCode_PropertyType result = gmdObjectFactory.createLanguageCode_PropertyType();
		
		if (language == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		result.setLanguageCode(code(language));
		return result;
	}

	public static CI_RoleCode_PropertyType roleCode(CI_RoleCode role) {
		CI_RoleCode_PropertyType result = gmdObjectFactory.createCI_RoleCode_PropertyType();
		
		if (role == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		result.setCI_RoleCode(code(role));
		return result;
	}
	
	public static MD_CharacterSetCode_PropertyType characterSetCode(MD_CharacterSetCode cs) {
		MD_CharacterSetCode_PropertyType result = gmdObjectFactory.createMD_CharacterSetCode_PropertyType();

		if (cs == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}

		result.setMD_CharacterSetCode(code(cs));
		return result;
	}
	
	public static CI_OnLineFunctionCode_PropertyType onlineFunctionCode(CI_OnLineFunctionCode code) {
		CI_OnLineFunctionCode_PropertyType result = gmdObjectFactory.createCI_OnLineFunctionCode_PropertyType();
		
		if (code == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		result.setCI_OnLineFunctionCode(code(code));
		return result;
	}
	
	public static RE_Locale_PropertyType locale(String name, String languageCode) {
		RE_Locale_PropertyType result = grgObjectFactory.createRE_Locale_PropertyType();
		
		RE_Locale_Type localeType = grgObjectFactory.createRE_Locale_Type();
		localeType.setName(characterString(name));
		localeType.setLanguage(languageCode(languageCode));

		result.setRE_Locale(localeType);
		
		return result;
	}
	
	public static Date_PropertyType date(String date) {
		Date_PropertyType result = gcoObjectFactory.createDate_PropertyType();
	
		if (StringUtils.isEmpty(date)) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		result.setDate(date);
	
		return result;
	}
	
	public static CI_Date_PropertyType ciDate(CI_Date date) {
		CI_Date_PropertyType result = gmdObjectFactory.createCI_Date_PropertyType();
		
		if (date == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		CI_Date_Type dateType = gmdObjectFactory.createCI_Date_Type();
		dateType.setDate(date(date.getDate()));
		dateType.setDateType(dateTypeCode(date.getDateType()));
		
		result.setCI_Date(dateType);
		return result;
	}
	
	public static CI_Series_PropertyType series(CI_Series series) {
		CI_Series_PropertyType result = gmdObjectFactory.createCI_Series_PropertyType();
		
		if (series == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		CI_Series_Type seriesType = gmdObjectFactory.createCI_Series_Type();
		seriesType.setIssueIdentification(characterString(series.getIssueIdentification()));
		seriesType.setName(characterString(series.getName()));
		seriesType.setPage(characterString(series.getPage()));
		
		result.setCI_Series(seriesType);
		return result;
	}
	
	public static CI_Citation_PropertyType citation(CI_Citation citation) {
		CI_Citation_PropertyType result = gmdObjectFactory.createCI_Citation_PropertyType();
		
		if (citation == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		CI_Citation_Type citationType = gmdObjectFactory.createCI_Citation_Type();
		citationType.setCollectiveTitle(characterString(citation.getCollectiveTitle()));
		citationType.setEdition(characterString(citation.getEdition()));
		for (CI_Date date : citation.getDate()) {
			citationType.getDate().add(ciDate(date));
		}
		citationType.setEditionDate(date(citation.getEditionDate()));
		citationType.setISBN(characterString(citation.getISBN()));
		citationType.setISSN(characterString(citation.getISSN()));
		citationType.setOtherCitationDetails(characterString(citation.getOtherCitationDetails()));
		citationType.setSeries(series(citation.getSeries()));
		citationType.setTitle(characterString(citation.getTitle()));
		citationType.setUuid(citation.getUuid().toString());
		for (String altTitle : citation.getAlternateTitle()) {
			citationType.getAlternateTitle().add(characterString(altTitle));
		}
		for (CI_ResponsibleParty party : citation.getCitedResponsibleParty()) {
			citationType.getCitedResponsibleParty().add(responsibleParty(party));
		}
		for (CI_PresentationFormCode form : citation.getPresentationForm()) {
			citationType.getPresentationForm().add(presentationFormCode(form));
		}
		
		result.setCI_Citation(citationType);
		return result;
	}
	
	public static RE_Locale_PropertyType locale(RE_Locale locale) {
		RE_Locale_PropertyType result = grgObjectFactory.createRE_Locale_PropertyType();
		
		if (locale == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		RE_Locale_Type localeType = grgObjectFactory.createRE_Locale_Type();
		localeType.setCharacterEncoding(characterSetCode(locale.getCharacterEncoding()));
		localeType.setCitation(citation(locale.getCitation()));
		localeType.setCountry(characterString(locale.getCountry()));
		localeType.setLanguage(languageCode(locale.getLanguage()));
		localeType.setName(characterString(locale.getName()));
		localeType.setUuid(locale.getUuid().toString());
		
		result.setRE_Locale(localeType);
		return result;
	}
	
	public static RE_RegisterOwner_PropertyType owner(RE_RegisterOwner owner) {
		RE_RegisterOwner_PropertyType result = grgObjectFactory.createRE_RegisterOwner_PropertyType();
		
		if (owner == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		RE_RegisterOwner_Type ownerType = grgObjectFactory.createRE_RegisterOwner_Type();
		ownerType.setName(characterString(owner.getName()));
		ownerType.setContact(responsibleParty(owner.getContact()));
		ownerType.setUuid(owner.getUuid().toString());
		
		result.setRE_RegisterOwner(ownerType);		
		return result;
	}
	
	public static CI_ResponsibleParty_PropertyType responsibleParty(CI_ResponsibleParty party) {
		CI_ResponsibleParty_PropertyType result = gmdObjectFactory.createCI_ResponsibleParty_PropertyType();
		
		if (party == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		CI_ResponsibleParty_Type partyType = gmdObjectFactory.createCI_ResponsibleParty_Type();
		partyType.setUuid(party.getUuid().toString());
		partyType.setIndividualName(characterString(party.getIndividualName()));
		partyType.setOrganisationName(characterString(party.getOrganisationName()));
		partyType.setPositionName(characterString(party.getPositionName()));
		partyType.setRole(roleCode(party.getRole()));
		partyType.setContactInfo(contact(party.getContactInfo()));

		result.setCI_ResponsibleParty(partyType);
		return result;
	}
	
	public static CI_Address_PropertyType address(CI_Address address) {
		CI_Address_PropertyType result = gmdObjectFactory.createCI_Address_PropertyType();
		
		if (address == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		CI_Address_Type addressType = gmdObjectFactory.createCI_Address_Type();
		addressType.setAdministrativeArea(characterString(address.getAdministrativeArea()));
		addressType.setCity(characterString(address.getCity()));
		addressType.setCountry(characterString(address.getCountry()));
		addressType.setPostalCode(characterString(address.getPostalCode()));
		addressType.setUuid(address.getUuid().toString());
		for (String deliveryPoint : address.getDeliveryPoint()) {
			addressType.getDeliveryPoint().add(characterString(deliveryPoint));
		}
		for (String email : address.getElectronicMailAddress()) {
			addressType.getElectronicMailAddress().add(characterString(email));
		}
		
		result.setCI_Address(addressType);
		return result;
	}
	
	public static CI_Telephone_PropertyType phone(CI_Telephone phone) {
		CI_Telephone_PropertyType result = gmdObjectFactory.createCI_Telephone_PropertyType();
		
		if (phone == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		CI_Telephone_Type phoneType = gmdObjectFactory.createCI_Telephone_Type();
		phoneType.setUuid(phone.getUuid().toString());
		for (CharacterString fax : phone.getFacsimile()) {
			phoneType.getFacsimile().add(characterString(fax));
		}
		for (CharacterString voice : phone.getVoice()) {
			phoneType.getVoice().add(characterString(voice));
		}
		
		result.setCI_Telephone(phoneType);
		return result;
	}
	
	public static CI_Contact_PropertyType contact(CI_Contact contact) {
		CI_Contact_PropertyType result = gmdObjectFactory.createCI_Contact_PropertyType();
				
		if (contact == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		CI_Contact_Type contactType = gmdObjectFactory.createCI_Contact_Type();
		contactType.setUuid(contact.getUuid().toString());
		contactType.setAddress(address(contact.getAddress()));
		contactType.setContactInstructions(characterString(contact.getContactInstructions()));
		contactType.setHoursOfService(characterString(contact.getHoursOfService()));
		contactType.setOnlineResource(onlineResource(contact.getOnlineResource()));
		contactType.setPhone(phone(contact.getPhone()));
		
		result.setCI_Contact(contactType);		
		return result;
	}
	
	public static RE_SubmittingOrganization_PropertyType submittingOrganization(RE_SubmittingOrganization submitter) {
		RE_SubmittingOrganization_PropertyType result = grgObjectFactory.createRE_SubmittingOrganization_PropertyType();
				
		if (submitter == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		RE_SubmittingOrganization_Type submitterType = grgObjectFactory.createRE_SubmittingOrganization_Type();
		submitterType.setContact(responsibleParty(submitter.getContact()));
		submitterType.setName(characterString(submitter.getName()));
		submitterType.setUuid(submitter.getUuid().toString());
		
		result.setRE_SubmittingOrganization(submitterType);
		return result;
	}
	
	public static RE_AlternativeName_PropertyType alternativeName(RE_AlternativeName altName) {
		RE_AlternativeName_PropertyType result = grgObjectFactory.createRE_AlternativeName_PropertyType();
		
		if (altName == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		RE_AlternativeName_Type altNameType = grgObjectFactory.createRE_AlternativeName_Type();
		altNameType.setName(characterString(altName.getName()));
		altNameType.setLocale(locale(altName.getLocale()));
		
		result.setRE_AlternativeName(altNameType);
		return result;
	}
	
	public RE_ItemClass_PropertyType itemClass(RE_ItemClass itemClass) {
		RE_ItemClass_PropertyType result = grgObjectFactory.createRE_ItemClass_PropertyType();
		
		if (itemClass == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		RE_ItemClass_Type itemClassType = grgObjectFactory.createRE_ItemClass_Type();
		itemClassType.setName(characterString(itemClass.getName()));
		itemClassType.setTechnicalStandard(citation(itemClass.getTechnicalStandard()));
		itemClassType.setUuid(itemClass.getUuid().toString());
		if (itemClass.getAlternativeNames().isEmpty()) {
			itemClassType.getAlternativeNames().add(alternativeName(null));
		}
		for (RE_AlternativeName altName : itemClass.getAlternativeNames()) {
			itemClassType.getAlternativeNames().add(alternativeName(altName));
		}
		if (itemClass.getDescribedItem().isEmpty()) {
			itemClassType.getDescribedItem().add(registerItem((RE_RegisterItem)null));
		}
		for (RE_RegisterItem item : itemClass.getDescribedItem()) {
			itemClassType.getDescribedItem().add(registerItem(item.getUuid()));
		}
		
		result.setRE_ItemClass(itemClassType);
		return result;
	}
	
	public static RE_RegisterManager_PropertyType manager(RE_RegisterManager manager) {
		RE_RegisterManager_PropertyType result = grgObjectFactory.createRE_RegisterManager_PropertyType();
		
		if (manager == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		RE_RegisterManager_Type managerType = grgObjectFactory.createRE_RegisterManager_Type();
		managerType.setContact(responsibleParty(manager.getContact()));
		managerType.setName(characterString(manager.getName()));
		managerType.setUuid(manager.getUuid().toString());
		
		result.setRE_RegisterManager(managerType);
		return result;
	}
	
	public static RE_Version_PropertyType version(RE_Version version) {
		RE_Version_PropertyType result = grgObjectFactory.createRE_Version_PropertyType();
		
		if (version == null) {
			result.getNilReason().add(NIL_REASON);
			return result;
		}
		
		RE_Version_Type versionType = grgObjectFactory.createRE_Version_Type();
		versionType.setVersionDate(date(version.getVersionDate()));
		versionType.setVersionNumber(characterString(version.getVersionNumber()));
		
		result.setRE_Version(versionType);
		return result;
	}
	
	/**
	 * 
	 * @param register
	 * @param urlPattern e.g. "https://localhost:8080/register/%s" where %s is a placeholder for the register's UUID
	 * @return
	 */
	public RE_Register_Type register(RE_Register register, String urlPattern) {
		RE_Register_Type result = grgObjectFactory.createRE_Register_Type();
		
		result.setUuid(register.getUuid().toString());
		result.setName(characterString(register.getName()));
		result.setContentSummary(characterString(register.getContentSummary()));
		result.getUniformResourceIdentifier().add(onlineResource(String.format(urlPattern, register.getUuid().toString())));
		result.setOperatingLanguage(locale(register.getOperatingLanguage()));
		if (register.getAlternativeLanguages().isEmpty()) {
			result.getAlternativeLanguages().add(locale(null));
		}
		for (RE_Locale alternativeLanguage : register.getAlternativeLanguages()) {
			result.getAlternativeLanguages().add(locale(alternativeLanguage));
		}
		result.setVersion(version(register.getVersion()));
		result.setDateOfLastChange(date(register.getDateOfLastChange()));
		result.setOwner(owner(register.getOwner()));
		if (register.getSubmitter().isEmpty()) {
			result.getSubmitter().add(submittingOrganization((RE_SubmittingOrganization)null));
		}
		for (RE_SubmittingOrganization submitter : register.getSubmitter()) {
			result.getSubmitter().add(submittingOrganization(submitter));
		}
		if (register.getContainedItemClasses().isEmpty()) {
			result.getContainedItemClass().add(itemClass((RE_ItemClass)null));
		}
		for (RE_ItemClass itemClass : register.getContainedItemClasses()) {
			result.getContainedItemClass().add(itemClass(itemClass));
		}
		result.setManager(manager(register.getManager()));
		if (register.getContainedItems().isEmpty()) {
			result.getContainedItem().add(registerItem((RE_RegisterItem)null));
		}
		for (RE_RegisterItem containedItem : register.getContainedItems()) {
			result.getContainedItem().add(registerItem(containedItem.getUuid()));
		}
		
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
	
	public static XMLGregorianCalendar xmlGregorianCalendar(Date date) {
		if (date == null) return null;
		
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(date);
		XMLGregorianCalendar xmlDate;

		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		}
		catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
