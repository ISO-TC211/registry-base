package de.geoinfoffm.registry.core;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class ItemClassRegistry extends AbstractRegistry
{
	private static final Logger logger = LoggerFactory.getLogger(ItemClassRegistry.class);
	
	private Map<String, ItemClassConfiguration> itemClasses = new HashMap<String, ItemClassConfiguration>();
	private Map<Class<?>, ItemClassConfiguration> itemClasses2 = new HashMap<Class<?>, ItemClassConfiguration>();
	
	@SuppressWarnings("unchecked")
	public ItemClassRegistry(ApplicationContext context) {
		super(context, ItemClass.class);
		
		Map<String, Object> itemClassBeans = getBeans();
		
	    for (String beanName : itemClassBeans.keySet()) {
	    	Object bean = itemClassBeans.get(beanName);
	    	
	    	if (!RE_RegisterItem.class.isAssignableFrom(bean.getClass())) {
	    		throw new RuntimeException("Illegal annotation: @ItemClass annotated classes must derive from RE_RegisterItem");
	    	}
	    	
	    	ItemClass itemClassAnnotation = AnnotationUtils.findAnnotation(bean.getClass(), ItemClass.class);
	    	
/*	    	try { 
	            Resource[] resources = context.getResources("classpath:itemclasses/*.xml");
	            for (Resource resource : resources) {
	                    logger.debug(resource.getDescription());
	            }
	    	}
	        catch (Throwable t) {
	        	t.printStackTrace();
	        	// ignore
	        }
*/	    	
	    	// find XML configuration file for item class
//	    	String path = bean.getClass().getCanonicalName().replace(".", "/");
	    	String path = "itemclasses/" + bean.getClass().getCanonicalName() + ".xml";
                logger.debug("Looking for resource '{}'...", path);
	    	InputStream configFile = this.getClass().getClassLoader().getResourceAsStream(path);
/*	    	InputStream configFile = null;
	    	if (context != null) {
		    	try {
                    String resourcePath = "classpath:itemclasses/" + path;

                    Resource[] resources = context.getResources(resourcePath);
                    logger.debug("Looked for resource '{}': found {}", resourcePath, resources.length);

                    if (resources != null && resources.length > 0) {
                            configFile = resources[0].getInputStream();
                    }
		    	}
		    	catch (IOException e) {
		    		e.printStackTrace();
		    		// Ignore
		    	}
	    	}
*/	    	
	    	if (configFile != null) {
                        logger.debug("Found resource '{}'!", path);
	    		try {
					JAXBContext jaxbContext = JAXBContext.newInstance(ItemClassConfiguration.class);
					Marshaller m = jaxbContext.createMarshaller();
					
					Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
					ItemClassConfiguration config = (ItemClassConfiguration)unmarshaller.unmarshal(configFile);
					
					config.setItemClass((Class<? extends RE_RegisterItem>)bean.getClass());
					config.setRegistry(this);
					
					itemClasses.put(itemClassAnnotation.value(), config);
					itemClasses2.put(bean.getClass(), config);
				}
				catch (JAXBException e) {
					e.printStackTrace();
				}
	    	}
	    }
	}
	
	public ItemClassConfiguration getConfiguration(String itemClassName) {
		return this.itemClasses.get(itemClassName);
	}
	
	public ItemClassConfiguration getConfiguration(Class<? extends RE_RegisterItem> itemClass) {
		return this.itemClasses2.get(itemClass);
	}
}
