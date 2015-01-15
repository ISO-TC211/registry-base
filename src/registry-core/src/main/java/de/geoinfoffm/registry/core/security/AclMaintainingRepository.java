package de.geoinfoffm.registry.core.security;

import java.util.Collection;

import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import de.geoinfoffm.registry.core.Entity;

public interface AclMaintainingRepository<T extends Entity>
{
	MutableAcl findAcl(T entity);
	
	void insertAce(T entity, int atIndexLocation, Permission permission, Sid sid, boolean granting);
	void appendAce(T entity, Permission permission, Sid sid, boolean granting);
	void appendAces(T entity, Collection<Permission> permission, Sid sid, boolean granting);
	void deleteAce(T entity, Permission permission, Sid sid);
	
	void setAclOwner(T entity, Sid owner);
}
