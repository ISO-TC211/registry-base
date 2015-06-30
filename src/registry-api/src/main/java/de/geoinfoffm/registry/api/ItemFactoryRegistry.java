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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;
import de.geoinfoffm.registry.core.model.ItemFactory;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class ItemFactoryRegistry
{
	private static final Logger logger = LoggerFactory.getLogger(ItemFactoryRegistry.class);
	
	private ApplicationContext context;
	private RegistryConfiguration configuration;
	
	private Map<String, RegisterItemFactory<?, ?>> itemFactories = new HashMap<String, RegisterItemFactory<?, ?>>();

	@SuppressWarnings("unchecked")
	public ItemFactoryRegistry(ApplicationContext context, RegistryConfiguration configuration) {
		this.context = context;
		this.configuration = configuration;
		
		Map<String, Object> beans = this.getBeans();
		
	    for (String beanName : beans.keySet()) {
	    	Object bean = beans.get(beanName);
	    	
	    	if (!RegisterItemFactory.class.isAssignableFrom(bean.getClass())) {
	    		throw new RuntimeException(String.format("Illegal annotation on class %s: @ItemFactory annotated classes must implement the RegisterItemFactory interface", bean.getClass().getCanonicalName()));
	    	}
	    	
	    	ItemFactory annotation = AnnotationUtils.findAnnotation(bean.getClass(), ItemFactory.class);
	    	for (String itemClassName : annotation.value()) {
	    		itemFactories.put(itemClassName, (RegisterItemFactory<?, ?>)bean);
	    	}
	    	
	    }
	}

	public Map<String, Object> getBeans() {
		GenericApplicationContext applicationContext = new GenericApplicationContext();
		applicationContext.setParent(context);
	    ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(applicationContext, false);
	    scanner.addIncludeFilter(new AnnotationTypeFilter(ItemFactory.class));
	    
	    String[] packages = configuration.getBasePackages();
	    scanner.scan(packages);
	    
	    applicationContext.refresh();
	    Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ItemFactory.class);
		return beans;
	}

	public RegisterItemFactory<? extends RE_RegisterItem, ? extends RegisterItemProposalDTO> getFactory(String itemClassName) {
		if (itemFactories.containsKey(itemClassName)) {
			return itemFactories.get(itemClassName);
		}
		else {
//			return new RegisterItemFactoryImpl<RE_RegisterItem, RegisterItemProposalDTO>();
			return context.getBean(RegisterItemFactoryImpl.class);
//			return BeanUtils.instantiateClass(RegisterItemFactoryImpl.class);
		}
	}


}
