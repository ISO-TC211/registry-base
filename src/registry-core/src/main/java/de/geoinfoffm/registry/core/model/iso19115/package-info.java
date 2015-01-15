@XmlSchema(
    namespace = "http://www.isotc211.org/2005/gmd",
    location = "http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/gmd/gmd.xsd",
    elementFormDefault = XmlNsForm.QUALIFIED,
    attributeFormDefault = XmlNsForm.UNQUALIFIED,
    xmlns={
    		@XmlNs(prefix="gmd", namespaceURI="http://www.isotc211.org/2005/gmd"),
    		@XmlNs(prefix="gco", namespaceURI="http://www.isotc211.org/2005/gco"),
    	  })
package de.geoinfoffm.registry.core.model.iso19115;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;

