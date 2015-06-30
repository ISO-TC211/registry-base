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
package de.geoinfoffm.registry.core;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

@XmlType(name = "ItemClassConfiguration", namespace = "http://www.geoinfoffm.de/registry",
		 propOrder = { "dtoClass", "viewBeanClass", "xmlType", "viewItemTemplate", "viewProposalTemplate", "createProposalTemplate", "editProposalTemplate", "properties" })
@XmlRootElement(name = "ItemClassConfiguration", namespace = "http://www.geoinfoffm.de/registry")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemClassConfiguration
{
	@XmlAttribute(required = true)
	private String itemClassName;
	
	@XmlTransient
	private Class<? extends RE_RegisterItem> itemClass;
	
	@XmlElement
	private String dtoClass;
	
	@XmlElement
	private String viewBeanClass;
	
	@XmlElement(required = false)
	private String xmlType;

	@XmlElement(required = false)
	private String viewItemTemplate;

	@XmlElement(required = false)
	private String viewProposalTemplate;

	@XmlElement(required = false)
	private String createProposalTemplate;

	@XmlElement(required = false)
	private String editProposalTemplate;

	@XmlElement(name = "property")
	private List<PropertyConfiguration> properties = new ArrayList<PropertyConfiguration>();
	
	@XmlTransient
	private ItemClassRegistry registry;
	
	private ItemClassConfiguration() { }
	
	public ItemClassConfiguration(String itemClassName, Class<? extends RE_RegisterItem> implementationClass) {
		this.setItemClassName(itemClassName);
		this.itemClass = implementationClass;
	}

	public String getItemClassName() {
		return itemClassName;
	}

	public void setItemClassName(String itemClassName) {
		this.itemClassName = itemClassName;
	}

	public Class<? extends RE_RegisterItem> getItemClass() {
		return itemClass;
	}

	public void setItemClass(Class<? extends RE_RegisterItem> itemClass) {
		this.itemClass = itemClass;
	}

	public String getDtoClass() {
		return dtoClass;
	}

	public void setDtoClass(String dtoClass) {
		this.dtoClass = dtoClass;
	}

	public String getViewBeanClass() {
		return viewBeanClass;
	}

	public void setViewBeanClass(String viewBeanClass) {
		this.viewBeanClass = viewBeanClass;
	}


	public String getXmlType() {
		return xmlType;
	}

	public void setXmlType(String xmlType) {
		this.xmlType = xmlType;
	}

	public String getViewItemTemplate() {
		return viewItemTemplate;
	}

	public void setViewItemTemplate(String viewItemTemplate) {
		this.viewItemTemplate = viewItemTemplate;
	}

	public String getViewProposalTemplate() {
		return viewProposalTemplate;
	}

	public void setViewProposalTemplate(String viewProposalTemplate) {
		this.viewProposalTemplate = viewProposalTemplate;
	}

	public String getCreateProposalTemplate() {
		return createProposalTemplate;
	}

	public void setCreateProposalTemplate(String createProposalTemplate) {
		this.createProposalTemplate = createProposalTemplate;
	}

	public String getEditProposalTemplate() {
		return editProposalTemplate;
	}

	public void setEditProposalTemplate(String editProposalTemplate) {
		this.editProposalTemplate = editProposalTemplate;
	}

	public List<PropertyConfiguration> getProperties() {
		return properties;
	}
	
	@SuppressWarnings("unchecked")
	public List<PropertyConfiguration> getMergedProperties() {
		List<PropertyConfiguration> result = new ArrayList<PropertyConfiguration>();
		result.addAll(this.getProperties());
		
		if (registry != null) {
			Class<? extends RE_RegisterItem> itemClass = this.getItemClass();
			while (itemClass.getSuperclass() != RE_RegisterItem.class) {
				itemClass = (Class<? extends RE_RegisterItem>)itemClass.getSuperclass();
				ItemClassConfiguration superConfig = registry.getConfiguration(itemClass);
				
				if (superConfig != null) {
					result.addAll(superConfig.getProperties());
				}
			}
		}
		
		return result;
	}


	public void setProperties(List<PropertyConfiguration> properties) {
		this.properties = properties;
	}
	
	public void addProperty(PropertyConfiguration property) {
		if (this.properties == null) {
			this.properties = new ArrayList<PropertyConfiguration>();
		}
		this.properties.add(property);
	}
	
	public PropertyConfiguration getProperty(String name) {
		for (PropertyConfiguration propertyConfiguration : this.getMergedProperties()) {
			if (propertyConfiguration.getName().equals(name)) {
				return propertyConfiguration;
			}
		}
		
		return null;
	}

	public ItemClassRegistry getRegistry() {
		return registry;
	}

	public void setRegistry(ItemClassRegistry registry) {
		this.registry = registry;
	}
}
