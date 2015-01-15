/**
 * 
 */
package de.geoinfoffm.registry.api;

import java.beans.PropertyDescriptor;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
//					result.addAdditionInformation(additionInformation);
					result.setStatus(RE_ItemStatus.NOT_VALID);
					result.setRegister(targetRegister);
					
					proposal.setAdditionalValues(result, entityManager);
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
