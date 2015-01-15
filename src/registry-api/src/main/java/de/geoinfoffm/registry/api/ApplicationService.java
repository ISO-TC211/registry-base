/**
 * 
 */
package de.geoinfoffm.registry.api;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;

import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.NonExistentRevisionException;
import de.geoinfoffm.registry.core.UnauthorizedException;

/**
 * Base interface for {@link Entity} application services.<br>
 * <br>
 * This interface should only be implemented by the root classes of an aggregate.
 * 
 * @author Florian Esser
 * @param <T> Root type of aggregate
 *
 */
public interface ApplicationService<T extends Entity>
{
	public <S extends T> S update(S entity) throws UnauthorizedException;
	
	/**
	 * Deletes an organization.
	 * 
	 * @param id ID of the organization to delete.
	 * @throws UnauthorizedException 
	 * @throws IllegalArgumentException thrown if no organization with the given ID exists
	 */
	public void delete(UUID id) throws UnauthorizedException;
	
	/**
	 * Finds the organization with then given ID.
	 *  
	 * @param id ID of the organization to find
	 * @return the organization or null if no organization with the given ID exists
	 */
	public T findOne(UUID id);
	
	/**
	 * Finds all organizations.
	 * 
	 * @return all organizations
	 */
	public List<T> findAll();
	
	/**
	 * Finds all organizations at a specific backend revision.
	 * 
	 * @param revision Backend revision
	 * @return a historic list of organizations
	 */
	public List<T> findAll(int revision) throws NonExistentRevisionException;

	/**
	 * Finds all organizations at a specific point in time.
	 * 
	 * @param revisionDate Backend revision date
	 * @return a historic list of organizations
	 * @throws NoRevisionAtThisPointInTimeException 
	 */
	public List<T> findAll(Date revisionDate) throws NoRevisionAtThisPointInTimeException;
	
	public List<T> findAll(Sort sort);

	/**
	 * @return the number of organizations 
	 */
	public long count();

}
