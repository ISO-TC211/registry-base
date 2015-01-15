///**
// * 
// */
//package de.geoinfoffm.registry.persistence;
//
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//import de.geoinfoffm.registry.core.Entity;
//
///**
// * @author Florian.Esser
// *
// */
//public abstract class AbstractAuditedRepositoryMapper<E extends Entity> implements AuditedRepository<E>
//{
//	private AuditedRepository<E> backend;
//	
//	public AbstractAuditedRepositoryMapper(AuditedRepository<E> backend) {
//		this.backend = backend;
//	}
//	
//	/* (non-Javadoc)
//	 * @see de.geoinfoffm.registry.persistence.AuditedRepository#findAll(int)
//	 */
//	@Override
//	public List<E> findAll(Integer revision) throws NonExistentRevisionException {
//		return backend.findAll(revision);
//	}
//
//	/* (non-Javadoc)
//	 * @see de.geoinfoffm.registry.persistence.AuditedRepository#findAll(java.util.Date)
//	 */
//	@Override
//	public List<E> findAll(Date revisionDate) throws NonExistentRevisionException {
//		return backend.findAll(revisionDate);
//	}
//
//	/* (non-Javadoc)
//	 * @see org.springframework.data.repository.CrudRepository#save(java.lang.Object)
//	 */
//	@Override
//	public <S extends E> S save(S entity) {
//		return backend.save(entity);
//	}
//
//	/* (non-Javadoc)
//	 * @see org.springframework.data.repository.CrudRepository#save(java.lang.Iterable)
//	 */
//	@Override
//	public <S extends E> List<S> save(Iterable<S> entities) {
//		return backend.save(entities);
//	}
//
//	/* (non-Javadoc)
//	 * @see org.springframework.data.repository.CrudRepository#findOne(java.io.Serializable)
//	 */
//	@Override
//	public E findOne(UUID id) {
//		return backend.findOne(id);
//	}
//
//	/* (non-Javadoc)
//	 * @see org.springframework.data.repository.CrudRepository#exists(java.io.Serializable)
//	 */
//	@Override
//	public boolean exists(UUID id) {
//		return backend.exists(id);
//	}
//
//	/* (non-Javadoc)
//	 * @see org.springframework.data.repository.CrudRepository#findAll()
//	 */
//	@Override
//	public List<E> findAll() {
//		return backend.findAll();	
//	}
//
//	/* (non-Javadoc)
//	 * @see org.springframework.data.repository.CrudRepository#findAll(java.lang.Iterable)
//	 */
//	@Override
//	public List<E> findAll(Iterable<UUID> ids) {
//		return backend.findAll(ids);
//	}
//
//	/* (non-Javadoc)
//	 * @see org.springframework.data.repository.CrudRepository#count()
//	 */
//	@Override
//	public long count() {
//		return backend.count();
//	}
//
//	/* (non-Javadoc)
//	 * @see org.springframework.data.repository.CrudRepository#delete(java.io.Serializable)
//	 */
//	@Override
//	public void delete(UUID id) {
//		backend.delete(id);
//	}
//
//	/* (non-Javadoc)
//	 * @see org.springframework.data.repository.CrudRepository#delete(java.lang.Object)
//	 */
//	@Override
//	public void delete(E entity) {
//		backend.delete(entity.getId());
//	}
//
//	/* (non-Javadoc)
//	 * @see org.springframework.data.repository.CrudRepository#delete(java.lang.Iterable)
//	 */
//	@Override
//	public void delete(Iterable<? extends E> entities) {
//		for (E entity : entities) {
//			this.delete(entity);
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see org.springframework.data.repository.CrudRepository#deleteAll()
//	 */
//	@Override
//	public void deleteAll() {
//		backend.deleteAll();
//	}
//}
