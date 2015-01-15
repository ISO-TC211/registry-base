package de.geoinfoffm.registry.core.model;

import java.util.List;

import de.geoinfoffm.registry.core.EntityRepository;

public interface DelegationRepository extends EntityRepository<Delegation>
{
	List<Delegation> findByActor(Actor actor);
	List<Delegation> findByRole(Role role);
	List<Delegation> findByDelegatingOrganization(Organization organization);
	List<Delegation> findByActorAndDelegatingOrganization(Actor actor, Organization organization);
	List<Delegation> findByDelegatingOrganizationAndIsApprovedTrue(Organization organization);
	List<Delegation> findByDelegatingOrganizationAndIsApprovedFalse(Organization organization);
	List<Delegation> findByDelegatingOrganizationAndRole(Organization organization, Role role);
	List<Delegation> findByActorAndDelegatingOrganizationAndIsApprovedFalse(Actor actor, Organization organization);
	List<Delegation> findByActorAndDelegatingOrganizationAndIsApprovedTrue(Actor actor, Organization organization);
//	List<Delegation> findByApprovedFalse();
}
