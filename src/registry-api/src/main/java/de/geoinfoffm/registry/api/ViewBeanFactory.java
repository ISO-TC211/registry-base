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
package de.geoinfoffm.registry.api;

import java.lang.reflect.Constructor;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.geoinfoffm.registry.core.ItemClassConfiguration;
import de.geoinfoffm.registry.core.ItemClassRegistry;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.workflow.ProposalWorkflowManager;

@Component
public class ViewBeanFactory
{
	@Autowired
	private ItemClassRegistry itemClassRegistry;
	
	@Autowired
	private ProposalWorkflowManager workflowManager;

	public ViewBeanFactory() {
	}
	
	private Class<? extends RegisterItemViewBean> determineViewBeanType(RE_ItemClass itemClass) {
		ItemClassConfiguration config = itemClassRegistry.getConfiguration(itemClass.getName());

		String viewBeanClassName = config.getViewBeanClass();
		Class<?> viewBeanClass;		
		try {
			viewBeanClass = this.getClass().getClassLoader().loadClass(viewBeanClassName);
			if (!RegisterItemViewBean.class.isAssignableFrom(viewBeanClass)) {
				throw new RuntimeException(String.format("The configured view bean class %s is not derived from RegisterItemViewBean", viewBeanClassName));
			}
			
			return (Class<? extends RegisterItemViewBean>)viewBeanClass;
		}
		catch (ClassNotFoundException e) {
			return RegisterItemViewBean.class;
		}

	}
	
	private Constructor<?> findConstructor(RE_ItemClass itemClass, Class<?> argumentType) {
		ItemClassConfiguration config = itemClassRegistry.getConfiguration(itemClass.getName());
		
		Constructor<RegisterItemViewBean> defaultConstructor;
		try {
			defaultConstructor = RegisterItemViewBean.class.getConstructor(argumentType);
		}
		catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		if (config == null) {
			return defaultConstructor;
		}

		String viewBeanClassName = config.getViewBeanClass();
		Class<?> viewBeanClass;		
		try {
			viewBeanClass = this.getClass().getClassLoader().loadClass(viewBeanClassName);
			if (!RegisterItemViewBean.class.isAssignableFrom(viewBeanClass)) {
				throw new RuntimeException(String.format("The configured view bean class %s is not derived from RegisterItemViewBean", viewBeanClassName));
			}
		}
		catch (ClassNotFoundException e) {
			return defaultConstructor;
		}

		Constructor<?> ctor;
		try {
			ctor = viewBeanClass.getConstructor(argumentType);
		}
		catch (NoSuchMethodException ex) {
			throw new RuntimeException(String.format("The configured view bean class is missing the constructor %s(%s)", viewBeanClass.getCanonicalName(), argumentType.getCanonicalName()));
		}
		
		return ctor;
	}
	
	public RegisterItemViewBean getViewBean(RE_RegisterItem item) {
		RE_ItemClass itemClass = item.getItemClass();
		return (RegisterItemViewBean)BeanUtils.instantiateClass(findConstructor(itemClass, RE_RegisterItem.class), item);
	}
	
	public RegisterItemViewBean getViewBean(Proposal proposal) {
		if (proposal instanceof SimpleProposal) {
			return getViewBean((SimpleProposal)proposal);
		}
		else if (proposal instanceof Supersession) {
			return getViewBean((Supersession)proposal);
		}
		else if (proposal instanceof ProposalGroup) {
			return getViewBean((ProposalGroup)proposal);
		}
		else {
			throw new RuntimeException("Not yet implemented");
		}
	}
	
	public RegisterItemViewBean getViewBean(SimpleProposal proposal) {
		RE_ItemClass itemClass = proposal.getItem().getItemClass();
		Class<? extends RegisterItemViewBean> viewBeanType = determineViewBeanType(itemClass);
		return RegisterItemViewBean.forProposal(proposal, viewBeanType, workflowManager);
//		return (RegisterItemViewBean)BeanUtils.instantiateClass(findConstructor(itemClass, Proposal.class), proposal);		
	}
	
	public RegisterItemViewBean getViewBean(Supersession supersession) {
		return RegisterItemViewBean.forProposal(supersession, workflowManager);
	}
	
	public RegisterItemViewBean getViewBean(ProposalGroup group) {
		return RegisterItemViewBean.forProposal(group, workflowManager);
	}

}
