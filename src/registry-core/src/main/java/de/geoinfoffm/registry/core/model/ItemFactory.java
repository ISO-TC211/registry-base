/**
 * 
 */
package de.geoinfoffm.registry.core.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author Florian.Esser
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ItemFactory
{
	/**
	 * the item classes that the annotated factory handles.
	 */
	String[] value();
}
