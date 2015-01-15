package de.geoinfoffm.registry.core.model.iso19135;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



/**
 * <UsedBy>
 * <NameSpace>ISO 19135 Procedures for registration</NameSpace>
 * <Class>RE_RegisterItem</Class>
 * <Attribute>status</Attribute>
 * <Type>RE_ItemStatus</Type>
 * <UsedBy>
 * @created 09-Sep-2013 19:13:01
 */
@XmlEnum
@XmlType(name = "RE_ItemStatus_Type", namespace = "http://www.isotc211.org/2005/grg")
@XmlRootElement(name = "RE_ItemStatus", namespace = "http://www.isotc211.org/2005/grg")
public enum RE_ItemStatus 
{
	@XmlEnumValue("notValid") NOT_VALID,
	@XmlEnumValue("valid") VALID,
	@XmlEnumValue("superseded") SUPERSEDED,
	@XmlEnumValue("retired") RETIRED;
}