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

import java.beans.PropertyDescriptor;
import java.math.BigInteger;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.ItemClassRegistry;
import de.geoinfoffm.registry.core.model.ItemFactory;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;

/**
 * @author Florian Esser
 * 
 */
@Component
public class RegisterItemFactoryImpl<I extends RE_RegisterItem, P extends RegisterItemProposalDTO> 
implements RegisterItemFactory<I, P>, ApplicationContextAware
{
	@Autowired
	private ItemClassRegistry registry;
	
	@Autowired
	private ItemClassRepository itemClassRepository;
	
	@Autowired
	private RegisterRepository registerRepository;
	
	@Autowired
	private RegisterItemService itemService;

	protected ApplicationContext context;
	
	@PersistenceContext
	protected EntityManager entityManager;

	public RegisterItemFactoryImpl() {
	}

	@Override
	public I createRegisterItem(P proposal) {
		RE_ItemClass itemClass = itemClassRepository.findOne(proposal.getItemClassUuid());
		Assert.notNull(itemClass);
		
		Map<String, Object> beans = registry.getBeans();

		for (String beanName : beans.keySet()) {
			Object bean = beans.get(beanName);
			ItemClass itemClassAnnotation = AnnotationUtils.findAnnotation(bean.getClass(), ItemClass.class);
			if (itemClassAnnotation.value().equals(itemClass.getName())) {
				I result;
//				if (!itemClassAnnotation.value().equals(RegisterItemFactory.class)) {
//					RegisterItemFactory factory = context.getBean(itemClassAnnotation.factoryClass());
//					result = factory.createRegisterItem(register, itemClass, beanName, definition, additionInformation, additionalData);
//				}
//				else {
					RE_Register targetRegister = registerRepository.findOne(proposal.getTargetRegisterUuid());
					Assert.notNull(targetRegister);

					result = (I)BeanUtils.instantiateClass(bean.getClass());
					result.setItemClass(itemClass);
					result.setName(proposal.getName());
					result.setDefinition(proposal.getDefinition());
					result.setStatus(RE_ItemStatus.NOT_VALID);
					result.setRegister(targetRegister);
					
					proposal.setAdditionalValues(result, entityManager);
					
					// The following call could lead to the entity being saved prematurely.
					// To prevent a ConstraintException, set a random negative value here.
					result.setItemIdentifier(BigInteger.valueOf(-RandomUtils.nextInt()));
					
					BigInteger maxIdentifier = itemService.findMaxItemIdentifier();
					if (maxIdentifier == null) {
						maxIdentifier = BigInteger.ZERO;
					}
					result.setItemIdentifier(maxIdentifier.add(BigInteger.ONE));

//				}
				
				return result;
			}
		}

		return null;
	}

	// private Map<String, Object> getBeans() {
	// GenericApplicationContext applicationContext = new GenericApplicationContext();
	// ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(applicationContext, false);
	// scanner.addIncludeFilter(new AnnotationTypeFilter(ItemClass.class));
	// scanner.scan("de.geoinfoffm.registry", "de.bund.bkg.gdide.registry");
	// applicationContext.refresh();
	// Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ItemClass.class);
	// return beans;
	// }

	public PropertyDescriptor[] getItemClassProperties(RE_ItemClass itemClass) {
		Map<String, Object> beans = registry.getBeans();

		for (String beanName : beans.keySet()) {
			Object bean = beans.get(beanName);
			ItemClass itemClassAnnotation = AnnotationUtils.findAnnotation(bean.getClass(), ItemClass.class);
			if (itemClassAnnotation.value().equals(itemClass.getName())) {
				return BeanUtils.getPropertyDescriptors(bean.getClass());
			}
		}

		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
}
