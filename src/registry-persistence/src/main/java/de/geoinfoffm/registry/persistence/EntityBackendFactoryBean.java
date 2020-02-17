/**
 * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *     * Redistributions of source code must retain the above copyright
 *     	 notice, this list of conditions and the following disclaimer.

 *     * Redistributions in binary form must reproduce the above
 *     	 copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials
 *       provided with the distribution.

 *     * The names "German Federal Agency for Cartography and Geodesy",
 *       "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE",
 *       "GDI-DE Registry" and the names of other contributors must not
 *       be used to endorse or promote products derived from this
 *       software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE GERMAN
 * FEDERAL AGENCY FOR CARTOGRAPHY AND GEODESY BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
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
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.history.support.RevisionEntityInformation;
import org.springframework.security.acls.model.MutableAclService;

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
		protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata,
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
