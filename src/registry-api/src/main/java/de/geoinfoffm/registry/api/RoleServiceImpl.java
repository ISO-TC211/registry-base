package de.geoinfoffm.registry.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.stereotype.Service;

import de.geoinfoffm.registry.core.model.RegisterRelatedRole;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.RoleRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@Service
public class RoleServiceImpl extends AbstractApplicationService<Role, RoleRepository> implements RoleService 
{
	@Autowired
	private MutableAclService mutableAclService;

	@Autowired
	public RoleServiceImpl(RoleRepository repository) {
		super(repository);
	}

	@Override
	public Role createRole(String name) {
		Role role = new Role(name);
		role = repository().save(role);
		
		return role;
	}

	@Override
	public Role findByName(String name) {
		return repository().findByName(name);
	}

	@Override
	public RegisterRelatedRole createRole(String name, RE_Register register) {
		RegisterRelatedRole role = new RegisterRelatedRole(name, register);
		role = repository().save(role);
		
		return role;
	}
}
