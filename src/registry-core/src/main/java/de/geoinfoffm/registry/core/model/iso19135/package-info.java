@XmlSchema(
    namespace = "http://www.isotc211.org/2005/grg",
    location = "http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19135-2_Schemas/grg.xsd",
    elementFormDefault = XmlNsForm.QUALIFIED,
    attributeFormDefault = XmlNsForm.UNQUALIFIED,
    	    xmlns={
    		@XmlNs(prefix="gmd", namespaceURI="http://www.isotc211.org/2005/gmd"),
    		@XmlNs(prefix="gco", namespaceURI="http://www.isotc211.org/2005/gco"),
    		@XmlNs(prefix="grg", namespaceURI="http://www.isotc211.org/2005/grg"),
    	  })
package de.geoinfoffm.registry.core.model.iso19135;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;

