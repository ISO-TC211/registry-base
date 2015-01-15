package de.geoinfoffm.registry.client.web;

import javax.servlet.Filter;

import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public abstract class AbstractWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer 
{
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		
		HiddenHttpMethodFilter methodFilter = new HiddenHttpMethodFilter();
		methodFilter.setMethodParam("_method");
		
		OpenEntityManagerInViewFilter oemivFilter = new OpenEntityManagerInViewFilter();
		OpenSessionInViewFilter openSessionFilter = new OpenSessionInViewFilter();

		return new Filter[] { characterEncodingFilter, methodFilter, oemivFilter, openSessionFilter };
	}
}
