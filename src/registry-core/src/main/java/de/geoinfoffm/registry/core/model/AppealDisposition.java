package de.geoinfoffm.registry.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AppealDisposition", namespace = "http://www.geoinfoffm.de/registry/soap")
@XmlAccessorType(XmlAccessType.FIELD)
public enum AppealDisposition {
	PENDING,
	ACCEPTED,
	NOT_ACCEPTED
}