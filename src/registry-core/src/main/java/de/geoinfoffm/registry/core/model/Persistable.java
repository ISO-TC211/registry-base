package de.geoinfoffm.registry.core.model;

import java.io.Serializable;

/**
 * Base interface of a persistable entity.
 * 
 * @author Florian Esser
 *
 * @param <K> Type of the primary key field.
 */
public interface Persistable<K extends Serializable> extends Serializable
{
	/**
	 * @return Id of the persisted entity, or null if the entity is not yet persisted.
	 */
	public K getId();
	
	/**
	 * @return true if the entity is persisted, false otherwise
	 */
	public boolean isNew();
}
