/**
 * 
 */
package de.geoinfoffm.registry.persistence;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.exception.RevisionDoesNotExistException;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryImpl;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.history.support.RevisionEntityInformation;

import de.geoinfoffm.registry.core.AuditedRepository;
import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.NonExistentRevisionException;

/**
 * @author Florian.Esser
 *
 */
public class AuditedRepositoryImpl<T extends Entity> extends EnversRevisionRepositoryImpl<T, UUID, Integer> implements AuditedRepository<T>
{
	private final EntityInformation<T, ?> entityInformation;
	private final RevisionEntityInformation revisionEntityInformation;
	private final EntityManager entityManager;

	/**
	 * @param entityInformation
	 * @param revisionEntityInformation
	 * @param entityManager
	 */
	public AuditedRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
			RevisionEntityInformation revisionEntityInformation, EntityManager entityManager) {

		super(entityInformation, revisionEntityInformation, entityManager);
		
		this.entityInformation = entityInformation;
		this.revisionEntityInformation = revisionEntityInformation;
		this.entityManager = entityManager;
	}

	
	@Override
	public T findOne(UUID uuid, Integer revision) throws NonExistentRevisionException {
		Class<T> type = entityInformation.getJavaType();

		AuditReader reader = AuditReaderFactory.get(entityManager);
		return reader.find(type, uuid, revision);
	}

	

	@Override
	public T findOne(UUID uuid, Date revisionDate) throws NonExistentRevisionException {
		AuditReader reader = AuditReaderFactory.get(entityManager);
		Number revision;
		try {
			revision = reader.getRevisionNumberForDate(revisionDate);
		}
		catch (RevisionDoesNotExistException e) {
			throw new NonExistentRevisionException(e.getMessage(), e);
		}
		
		return this.findOne(uuid, (Integer)revision);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.persistence.AuditedBackend#findAll(java.lang.Integer)
	 */
	@Override
	public List<T> findAll(Integer revision) {
		Class<T> type = entityInformation.getJavaType();

		AuditReader reader = AuditReaderFactory.get(entityManager);
		return reader.createQuery().forEntitiesAtRevision(type, revision).getResultList();
	}
	
	@Override
	public List<T> findAll(Date revisionDate) {
		AuditReader reader = AuditReaderFactory.get(entityManager);
		Number revision = reader.getRevisionNumberForDate(revisionDate);
		
		return this.findAll((Integer)revision);
	}
}
