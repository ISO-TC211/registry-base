package de.geoinfoffm.registry.core.model.iso19135;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



/**
 * <UsedBy>
 * <NameSpace>ISO 19135 Procedures for registration</NameSpace>
 * <Class>RE_ProposalManagementInformation</Class>
 * <Attribute>disposition[0..1]</Attribute>
 * <Type>RE_Disposition</Type>
 * <UsedBy>
 * @created 09-Sep-2013 19:12:33
 */
@XmlEnum
@XmlType(name = "RE_Disposition_Type", namespace = "http://www.isotc211.org/2005/grg")
@XmlRootElement(name = "RE_Disposition", namespace = "http://www.isotc211.org/2005/grg")
public enum RE_Disposition {
	@XmlEnumValue("withdrawn") WITHDRAWN,
	@XmlEnumValue("accepted") ACCEPTED,
	@XmlEnumValue("not_accepted") NOT_ACCEPTED,
}