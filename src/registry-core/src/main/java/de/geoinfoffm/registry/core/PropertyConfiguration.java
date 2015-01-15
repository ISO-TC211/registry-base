package de.geoinfoffm.registry.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "PropertyConfiguration", namespace = "http://www.geoinfoffm.de/registry", propOrder = { "labelKey",
		"placeholderKey", "viewHandlerBeanName", "required" })
public class PropertyConfiguration
{
	@XmlAttribute(required = true, namespace = "http://www.geoinfoffm.de/registry")
	private String name;

	@XmlElement(required = true, nillable = false, namespace = "http://www.geoinfoffm.de/registry")
	private String labelKey;

	@XmlElement(required = false, namespace = "http://www.geoinfoffm.de/registry")
	private String placeholderKey;

	@XmlElement(required = false, namespace = "http://www.geoinfoffm.de/registry")
	private String viewHandlerBeanName;

	@XmlElement(required = true, namespace = "http://www.geoinfoffm.de/registry")
	private boolean required;

	private PropertyConfiguration() {
	}

	public PropertyConfiguration(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabelKey() {
		return labelKey;
	}

	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}

	public String getPlaceholderKey() {
		return placeholderKey;
	}

	public void setPlaceholderKey(String placeholderKey) {
		this.placeholderKey = placeholderKey;
	}

	public String getViewHandlerBeanName() {
		return viewHandlerBeanName;
	}

	public void setViewHandler(String viewHandler) {
		this.viewHandlerBeanName = viewHandler;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
}
