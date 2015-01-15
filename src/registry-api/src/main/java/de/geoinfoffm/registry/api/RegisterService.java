/**
 * 
 */
package de.geoinfoffm.registry.api;

import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import de.geoinfoffm.registry.core.ParameterizedRunnable;
import de.geoinfoffm.registry.core.model.Actor;
import de.geoinfoffm.registry.core.model.RegisterRelatedRole;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.persistence.xml.exceptions.XmlSerializationException;

/**
 * @author Florian.Esser
 *
 */
public interface RegisterService extends ApplicationService<RE_Register>
{
	public RE_Register createRegister(String name, Actor registerOwner, Actor registerManager, Actor controlBody, RoleService roleService);
	public <R extends RE_Register> R createRegister(String name, Actor registerOwner, Actor registerManager, Actor controlBody, RoleService roleService, Class<R> registerClass, ParameterizedRunnable<R> setter);
	
	public RE_Register findByName(String name);
	
	public Map<UUID, String> getCachedRegisterNames();
//	public Map<UUID, String> getCachedItemClasses(UUID registerUuid);
	
	public Map<UUID, String> getContainedItemClasses(UUID registerUuid);
	
//	public BigInteger nextItemIdentifier();
	
	public RegisterRelatedRole getManagerRole(RE_Register register);
	public RegisterRelatedRole getOwnerRole(RE_Register register);
	public RegisterRelatedRole getSubmitterRole(RE_Register register);
	public RegisterRelatedRole getControlBodyRole(RE_Register register);
	
	public void toXml(RE_Register register, Writer output) throws XmlSerializationException;
}
