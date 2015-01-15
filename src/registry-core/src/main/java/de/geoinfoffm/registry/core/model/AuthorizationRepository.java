package de.geoinfoffm.registry.core.model;

import java.util.List;

import de.geoinfoffm.registry.core.AuditedRepository;

public interface AuthorizationRepository extends AuditedRepository<Authorization>
{
	List<Authorization> findByActor(Actor actor);
	List<Authorization> findByRole(Role role);
	Authorization findByActorAndRole(Actor actor, Role role);
}
