/**
 * 
 */
package de.geoinfoffm.registry.core;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.history.RevisionRepository;

/**
 * Interface for audited persistence backends.
 * 
 * @author Florian Esser
 * @param <T> Type held in backend
 *
 */
@NoRepositoryBean 
public interface AuditedRepository<T extends Entity> extends Repository<T>, RevisionRepository<T, UUID, Integer>
{
	public T findOne(UUID uuid, Integer revision) throws NonExistentRevisionException;
	public T findOne(UUID uuid, Date revisionDate) throws NonExistentRevisionException;
	
	public List<T> findAll(Integer revision) throws NonExistentRevisionException;
	public List<T> findAll(Date revisionDate) throws NonExistentRevisionException;
}
