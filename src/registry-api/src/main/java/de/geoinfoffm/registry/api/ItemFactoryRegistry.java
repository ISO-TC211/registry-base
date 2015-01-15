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
