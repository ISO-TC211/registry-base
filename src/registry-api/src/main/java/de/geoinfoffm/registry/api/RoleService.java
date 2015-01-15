package de.geoinfoffm.registry.api;

import org.springframework.stereotype.Service;

import de.geoinfoffm.registry.core.model.RegisterRelatedRole;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@Service
public interface RoleService extends ApplicationService<Role>
{
	Role createRole(String name);
	RegisterRelatedRole createRole(String name, RE_Register register);

	Role findByName(String name);
}
