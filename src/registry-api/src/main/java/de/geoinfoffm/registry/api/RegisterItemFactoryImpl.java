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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.math.RandomUtils;
import org.isotc211.iso19135.RE_RegisterItem_Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.geoinfoffm.registry.api.converter.BeanConverter;
import de.geoinfoffm.registry.api.iso.IsoXmlFactory;
import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.ItemClassRegistry;
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
public class RegisterItemFactoryImpl<I extends RE_RegisterItem, P extends RegisterItemProposalDTO> implements RegisterItemFactory<I, P>, ApplicationContextAware
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

	@Autowired
	private BeanConverter beanConverter;

	public RegisterItemFactoryImpl() {
	}

	@Override
	public I createRegisterItem(RE_RegisterItem_Type item) {
		return createRegisterItem(item, null);
	}

	@Override
	public I createRegisterItem(final RE_RegisterItem_Type item, final EntityUnitOfWork unitOfWork) {
		if (!item.isSetItemClass()) {
			throw new RuntimeException("item has no itemClass");
		}

		UUID itemClassUuid;
		if (item.getItemClass().isSetUuidref()) {
			itemClassUuid = UUID.fromString(item.getItemClass().getUuidref());
		}
		else if (item.getItemClass().isSetRE_ItemClass()) {
			itemClassUuid = UUID.fromString(item.getItemClass().getRE_ItemClass().getUuid());
		}
		else {
			throw new RuntimeException("item has no itemClass");
		}
		final RE_ItemClass itemClass = itemClassRepository.findOne(itemClassUuid);
		Assert.notNull(itemClass);

		final I result;
		if (item.isSetUuid()) {
			result = instantiateItem(itemClass, UUID.fromString(item.getUuid()));
		}
		else {
			result = instantiateItem(itemClass);
		}
		if (result != null) {
			if (unitOfWork == null) {
				setItemValues(result, item, itemClass, unitOfWork);
			}
			else {
				unitOfWork.registerNew(result);
				unitOfWork.registerRunnable(result, new Runnable() {
					@Override
					public void run() {
						setItemValues(result, item, itemClass,unitOfWork);
//						entityManager.merge(result);
					}
				});
			}
//			entityManager.merge(result);
		}
		return result;
	}

	@Override
	public I createRegisterItem(P proposal) {
		RE_ItemClass itemClass = itemClassRepository.findOne(proposal.getItemClassUuid());
		Assert.notNull(itemClass);
		RE_Register targetRegister = registerRepository.findOne(proposal.getTargetRegisterUuid());
		Assert.notNull(targetRegister);

		I result = instantiateItem(itemClass);
		if (result != null) {
			setItemValues(result, proposal, itemClass);
			result.setRegister(targetRegister);
		}

		return result;
	}

	protected I instantiateItem(RE_ItemClass itemClass) {
		return instantiateItem(itemClass, null);
	}

	protected I instantiateItem(RE_ItemClass itemClass, UUID uuid) {
		Map<String, Object> beans = registry.getBeans();

		for (String beanName : beans.keySet()) {
			Object bean = beans.get(beanName);
			ItemClass itemClassAnnotation = AnnotationUtils.findAnnotation(bean.getClass(), ItemClass.class);
			if (itemClassAnnotation.value().equals(itemClass.getName())) {
				@SuppressWarnings("unchecked")
				I result = (I)BeanUtils.instantiateClass(bean.getClass());
				if (uuid != null) {
					Method setUuidMethod;
					try {
						setUuidMethod = Entity.class.getDeclaredMethod("setUuid", UUID.class);
						setUuidMethod.setAccessible(true);
						setUuidMethod.invoke(result, uuid);
					}
					catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new RuntimeException(e.getMessage(), e);
					}
				}
				return result;
			}
		}

		return null;
	}

	protected void setItemValues(I item, RE_RegisterItem_Type xmlItem, RE_ItemClass itemClass, EntityUnitOfWork unitOfWork) {
		item.setItemClass(itemClass);
		item.setName(xmlItem.getName().getCharacterString().getValue().toString());
		item.setDefinition(xmlItem.getDefinition().getCharacterString().getValue().toString());
		if (xmlItem.isSetDescription() && xmlItem.getDescription().isSetCharacterString()) {
			item.setDescription(xmlItem.getDescription().getCharacterString().getValue().toString());
		}
		item.setStatus(IsoXmlFactory.itemStatus(xmlItem.getStatus()));
		if (xmlItem.isSetDateAccepted()) {
			item.setDateAccepted(IsoXmlFactory.date(xmlItem.getDateAccepted()));
		}
		if (xmlItem.isSetDateAmended()) {
			item.setDateAmended(IsoXmlFactory.date(xmlItem.getDateAmended()));
		}
		item.setItemIdentifier(xmlItem.getItemIdentifier().getInteger());

		// set additional values, copy additional values from xml bean to model
		// bean
		beanConverter.updateModelBeanFromXmlBean(item, xmlItem, unitOfWork, true, true);
	}

	protected void setItemValues(I item, P proposal, RE_ItemClass itemClass) {
		item.setItemClass(itemClass);
		item.setName(proposal.getName());
		item.setDefinition(proposal.getDefinition());
		item.setDescription(proposal.getDescription());
		item.setStatus(RE_ItemStatus.NOT_VALID);

		proposal.setAdditionalValues(item, entityManager);

		// Sets a random negative item identifier. The final (positive) identifier has to be
		// set as part of the proposal workflow.
		item.setItemIdentifier(BigInteger.valueOf(-RandomUtils.nextInt()));
	}

	// private Map<String, Object> getBeans() {
	// GenericApplicationContext applicationContext = new
	// GenericApplicationContext();
	// ClassPathBeanDefinitionScanner scanner = new
	// ClassPathBeanDefinitionScanner(applicationContext, false);
	// scanner.addIncludeFilter(new AnnotationTypeFilter(ItemClass.class));
	// scanner.scan("de.geoinfoffm.registry", "de.bund.bkg.gdide.registry");
	// applicationContext.refresh();
	// Map<String, Object> beans =
	// applicationContext.getBeansWithAnnotation(ItemClass.class);
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
