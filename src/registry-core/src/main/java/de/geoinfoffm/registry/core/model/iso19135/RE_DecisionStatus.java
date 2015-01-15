package de.geoinfoffm.registry.core.model.iso19135;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



/**
 * <UsedBy>
 * <NameSpace>ISO 19135 Procedures for registration</NameSpace>
 * <Class>RE_ProposalManagementInformation</Class>
 * <Attribute>status</Attribute>
 * <Type>RE_DecisionStatus</Type>
 * <UsedBy>
 * @created 09-Sep-2013 19:12:25
 */
@XmlEnum
@XmlType(name = "RE_DecisionStatus_Type", namespace = "http://www.isotc211.org/2005/grg")
@XmlRootElement(name = "RE_DecisionStatus", namespace = "http://www.isotc211.org/2005/grg")
public enum RE_DecisionStatus {
	@XmlEnumValue("pending") PENDING,
	@XmlEnumValue("tentative") TENTATIVE,
	@XmlEnumValue("final") FINAL;
}