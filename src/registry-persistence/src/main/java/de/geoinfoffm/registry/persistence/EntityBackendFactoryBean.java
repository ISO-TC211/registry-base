/**
 * 
 */
package de.geoinfoffm.registry.persistence;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.hibernate.envers.DefaultRevisionEntity;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.envers.repository.support.ReflectionRevisionEntityInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.history.support.RevisionEntityInformation;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.stereotype.Component;

import de.geoinfoffm.registry.core.AuditedRepository;

/**
 * {@link FactoryBean} creating {@link AuditedRepository} instances.
 * 
 * @author Florian Esser
 */
public class EntityBackendFactoryBean extends EnversRevisionRepositoryFactoryBean
{
	private Class<?> revisionEntityClass;

	@Autowired
	private MutableAclService aclService;

	/**
	 * Configures the revision entity class. Will default to {@link DefaultRevisionEntity}.
	 * 
	 * @param revisionEntityClass
	 */
	public void setRevisionEntityClass(Class<?> revisionEntityClass) {
		this.revisionEntityClass = revisionEntityClass;
	}
	
	public void setAclService(MutableAclService aclService) {
		this.aclService = aclService;
	}

	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new EntityRepositoryFactory(entityManager, revisionEntityClass, aclService);
	}

	private static class EntityRepositoryFactory extends JpaRepositoryFactory {

		private final RevisionEntityInformation revisionEntityInformation;
		private final MutableAclService aclService;

		/**
		 * Creates a new {@link RevisionRepositoryFactory} using the given {@link EntityManager} and revision entity class.
		 * 
		 * @param entityManager must not be {@literal null}.
		 * @param revisionEntityClass can be {@literal null}, will default to {@link DefaultRevisionEntity}.
		 */
		public EntityRepositoryFactory(EntityManager entityManager, Class<?> revisionEntityClass, MutableAclService aclService) {
			super(entityManager);
			revisionEntityClass = (revisionEntityClass == null ? DefaultRevisionEntity.class : revisionEntityClass);
			this.aclService = aclService;
			this.revisionEntityInformation = DefaultRevisionEntity.class.equals(revisionEntityClass) ? new RevisionEntityInformation() {
				
				/*
				 * (non-Javadoc)
				 * @see org.springframework.data.repository.history.support.RevisionEntityInformation#getRevisionNumberType()
				 */
				public Class<?> getRevisionNumberType() {
					return Integer.class;
				}

				/*
				 * (non-Javadoc)
				 * @see org.springframework.data.repository.history.support.RevisionEntityInformation#isDefaultRevisionEntity()
				 */
				public boolean isDefaultRevisionEntity() {
					return true;
				}

				/*
				 * (non-Javadoc)
				 * @see org.springframework.data.repository.history.support.RevisionEntityInformation#getRevisionEntityClass()
				 */
				public Class<?> getRevisionEntityClass() {
					return DefaultRevisionEntity.class;
				}
			} : new ReflectionRevisionEntityInformation(revisionEntityClass);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.data.jpa.repository.support.JpaRepositoryFactory#getTargetRepository(org.springframework.data.repository.core.RepositoryMetadata, javax.persistence.EntityManager)
		 */
		@Override
		@SuppressWarnings({ "unchecked", "rawtypes" })
		protected <T, ID extends Serializable> JpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata,
				EntityManager entityManager) {

			JpaEntityInformation<T, Serializable> entityInformation = 
					(JpaEntityInformation<T, Serializable>)getEntityInformation(metadata.getDomainType());
			
			return new EntityRepositoryImpl(entityInformation, revisionEntityInformation, entityManager, aclService);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.jpa.repository.support.JpaRepositoryFactory#getRepositoryBaseClass(org.springframework.data.repository.core.RepositoryMetadata)
		 */
		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return EntityRepositoryImpl.class;
		}

		/* (non-Javadoc)
		 * @see org.springframework.data.repository.core.support.RepositoryFactorySupport#getRepository(java.lang.Class, java.lang.Object)
		 */
		@Override
		public <T> T getRepository(Class<T> repositoryInterface, Object customImplementation) {
			if (AuditedRepository.class.isAssignableFrom(repositoryInterface)) {
				Class<?>[] typeArguments = GenericTypeResolver.resolveTypeArguments(repositoryInterface,
						RevisionRepository.class);
				Class<?> revisionNumberType = typeArguments[2];

				if (!revisionEntityInformation.getRevisionNumberType().equals(revisionNumberType)) {
					throw new IllegalStateException(String.format(
							"Configured a revision entity type of %s with a revision type of %s "
									+ "but the repository interface is typed to a revision type of %s!", repositoryInterface,
							revisionEntityInformation.getRevisionNumberType(), revisionNumberType));
				}
			}

			return super.getRepository(repositoryInterface, customImplementation);
		}
	}
}
