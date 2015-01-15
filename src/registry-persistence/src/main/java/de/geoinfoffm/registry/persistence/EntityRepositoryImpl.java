package de.geoinfoffm.registry.persistence;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.repository.history.support.RevisionEntityInformation;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.util.Assert;

import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.security.AclMaintainingRepository;
import de.geoinfoffm.registry.core.security.RegistrySecurity;

/**
 * @author Florian Esser
 *
 */
public class EntityRepositoryImpl<T extends Entity> extends AuditedRepositoryImpl<T> implements AclMaintainingRepository<T>
{
	private final MutableAclService aclService;

	public EntityRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
			RevisionEntityInformation revisionEntityInformation, EntityManager entityManager,
			MutableAclService aclService) {
		super(entityInformation, revisionEntityInformation, entityManager);

		this.aclService = aclService;
	}

	@Override
	public <S extends T> S save(S entity) {
		entity = super.save(entity);
		createAcl(entity);
		return entity;
	}

	@Override
	public T saveAndFlush(T entity) {
		entity = super.saveAndFlush(entity);
		createAcl(entity);
		return entity;
	}

	@Override
	public <S extends T> List<S> save(Iterable<S> entities) {
		List<S> result = super.save(entities);
		createAcl(result);
		return result;
	}
	
	
	
	@Override
	public void delete(T entity) {
		deleteAcl(entity);
		super.delete(entity);
	}

	private <S extends T> MutableAcl createAcl(S entity) {
		if (aclService == null) {
			return null;
		}
		
		MutableAcl acl; 
		try {
			acl = (MutableAcl)aclService.readAclById(new ObjectIdentityImpl(entity));
		}
		catch (NotFoundException e) {
			acl = aclService.createAcl(new ObjectIdentityImpl(entity));
			Sid sid = new GrantedAuthoritySid(RegistrySecurity.ADMIN_ROLE);
			acl.insertAce(0, BasePermission.ADMINISTRATION, sid, true);
			acl.insertAce(1, BasePermission.READ, sid, true);
			acl.insertAce(2, BasePermission.WRITE, sid, true);
			acl.insertAce(3, BasePermission.DELETE, sid, true);
			aclService.updateAcl(acl);
		}
		
		return acl;
	}
	
	private void deleteAcl(T entity) {
		aclService.deleteAcl(new ObjectIdentityImpl(entity), true);
	}
	
	private <S extends T> void createAcl(Iterable<S> entities) {
		for (S entity : entities) {
			createAcl(entity);
		}
	}

	@Override
	public MutableAcl findAcl(T entity) {
		Assert.notNull(entity, "Entity must be provided");
		return (MutableAcl)aclService.readAclById(new ObjectIdentityImpl(entity));
	}

	@Override
	public void insertAce(T entity, int atIndexLocation, Permission permission, Sid sid, boolean granting) {
		MutableAcl acl = this.findAcl(entity);
		acl.insertAce(atIndexLocation, permission, sid, granting);
		aclService.updateAcl(acl);
	}

	@Override
	public void appendAce(T entity, Permission permission, Sid sid, boolean granting) {
		MutableAcl acl;
		try {
			acl = this.findAcl(entity);
		}
		catch (NotFoundException e) {
			acl = this.createAcl(entity);
		}
		this.insertAce(entity, acl.getEntries().size(), permission, sid, granting);
	}

	@Override
	public void appendAces(T entity, Collection<Permission> permissions, Sid sid, boolean granting) {
		for (Permission permission : permissions) {
			this.appendAce(entity, permission, sid, granting);
		}
	}

	@Override
	public void setAclOwner(T entity, Sid owner) {
		MutableAcl acl = this.findAcl(entity);
		acl.setOwner(owner);
		aclService.updateAcl(acl);
	}

	@Override
	public void deleteAce(T entity, Permission permission, Sid sid) {
		MutableAcl acl = (MutableAcl)aclService.readAclById(new ObjectIdentityImpl(entity));
		for (int i = 0; i < acl.getEntries().size(); i++) {
			AccessControlEntry ace = acl.getEntries().get(i);
			if (ace.getPermission().equals(permission) && ace.getSid().equals(sid)) {
				acl.deleteAce(i);
				break;
			}
		}
	}
}
