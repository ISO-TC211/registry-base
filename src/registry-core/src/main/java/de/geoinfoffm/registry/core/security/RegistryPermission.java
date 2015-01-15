package de.geoinfoffm.registry.core.security;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

public class RegistryPermission extends BasePermission
{
    public static final Permission REGISTER_SUBMIT = new RegistryPermission(1 << 5, 'S'); // 32; Register submitting org
    public static final Permission REGISTER_MANAGE = new RegistryPermission(1 << 6, 'M'); // 64; Register manager
    public static final Permission REGISTER_CONTROL = new RegistryPermission(1 << 7, 'B'); // 128; Register Control Body

	public RegistryPermission(int mask) {
		super(mask);
	}

	public RegistryPermission(int mask, char code) {
		super(mask, code);
	}

}
