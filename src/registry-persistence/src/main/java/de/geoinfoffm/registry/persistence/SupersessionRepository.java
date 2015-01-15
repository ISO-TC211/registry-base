/**
 * 
 */
package de.geoinfoffm.registry.persistence;

import java.util.Set;

import de.geoinfoffm.registry.core.AuditedRepository;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

/**
 * @author Florian.Esser
 *
 */
public interface SupersessionRepository extends AuditedRepository<Supersession>
{
//	public Set<Supersession> findByStatusIn(Collection<RE_DecisionStatus> status);
	
//	public Set<Supersession> findByRegister(RE_Register register);
//	public Set<Supersession> findByRegisterAndStatusIn(RE_Register register, Collection<RE_DecisionStatus> status);
}
