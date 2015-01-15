package de.geoinfoffm.registry.core;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;

public abstract class AbstractRegistry
{
	private Class<? extends Annotation> annotation;
	private RegistryConfiguration configuration;
	private Map<String, Object> beans;
	
	public AbstractRegistry(ApplicationContext context, Class<? extends Annotation> annotation) {
		this.configuration = new RegistryConfiguration();
		this.annotation = annotation;
		getBeans();
	}

	public Map<String, Object> getBeans() {
		if (beans == null) {
			this.beans = new HashMap<String, Object>();
			GenericApplicationContext applicationContext = new GenericApplicationContext();
		    ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(applicationContext, false);
		    scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));
		    
		    String[] packages = configuration.getBasePackages();
		    scanner.scan(packages);
		    
		    applicationContext.refresh();
		    beans.putAll(applicationContext.getBeansWithAnnotation(annotation));
		}
		
		return beans;
	}

}