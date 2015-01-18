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
package de.geoinfoffm.registry.api;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import javax.jws.WebService;

import org.springframework.stereotype.Service;

import de.geoinfoffm.registry.api.soap.CreateOrganizationRequest;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Delegation;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.OrganizationRelatedRole;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.Role;

/**
 * Service operations for {@link Organization}s in the GDI-DE Registry.
 * 
 * @author Florian Esser
 */
@Service
@WebService
public interface OrganizationService extends ApplicationService<Organization>
{
	public Organization findByName(String name);
	
	public Organization createOrganization(CreateOrganizationRequest organization) throws UnauthorizedException;
	public Organization updateOrganization(OrganizationUpdateDTO dto) throws UnauthorizedException;

	public Delegation delegate(RegistryUser user, Role role, Organization organization) throws UnauthorizedException;
	public Delegation requestDelegation(Role role, Organization organization) throws UnauthorizedException;
	public Delegation getOrCreateDelegationRequest(RegistryUser user, Role role, Organization organization) throws UnauthorizedException;
	public Delegation findDelegation(RegistryUser user, Role role, Organization org);
	public void revokeDelegation(Delegation delegation);
	
	public OrganizationRelatedRole createOrganizationRelatedRole(String name, Organization organization);
	
	public Map<UUID, String> getAllNames();

	Collection<RegistryUser> findRoleUsers(Organization organization, Role role);

	OrganizationRelatedRole getPointOfContactRole(Organization organization);
	Collection<RegistryUser> findPointsOfContact(Organization organization);
	
	OrganizationRelatedRole getMembershipRole(Organization organization);
}
