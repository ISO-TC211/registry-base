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

import java.util.ArrayList;
import java.util.List;

import de.geoinfoffm.registry.core.model.Authorization;
import de.geoinfoffm.registry.core.model.AuthorizationRepository;
import de.geoinfoffm.registry.core.model.Delegation;
import de.geoinfoffm.registry.core.model.DelegationRepository;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.RoleRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

/**
 * The class ControlBodyDiscoveryStrategyImpl.
 *
 * @author Florian Esser
 */
public class ControlBodyDiscoveryStrategyImpl implements ControlBodyDiscoveryStrategy
{
	private RegisterService registerService;
	private DelegationRepository delegationRepository;
	
	public ControlBodyDiscoveryStrategyImpl(RegisterService registerService, DelegationRepository delegationRepository) {
		this.registerService = registerService;
		this.delegationRepository = delegationRepository;
	}

	@Override
	public List<Role> findControlBodyRoles(Proposal proposal) {
		List<Role> result = new ArrayList<>();
		for (RE_Register register : proposal.getAffectedRegisters()) {
			result.addAll(this.findControlBodyRoles(register));
		}

		return result;
	}

	@Override
	public List<Authorization> findControlBodyAuthorizations(Proposal proposal) {
		List<Authorization> result = new ArrayList<>();
		
		List<Role> roles = this.findControlBodyRoles(proposal);
		result.addAll(findControlBodyAuthorizations(roles));
			
		return result;
	}

	private List<Authorization> findControlBodyAuthorizations(List<Role> roles) {
		List<Authorization> result = new ArrayList<Authorization>();
		
		for (Role role : roles) {
			List<Delegation> delegations = delegationRepository.findByRole(role);
			if (delegations != null && !delegations.isEmpty()) {
				for (Delegation delegation : delegations) {
					result.add(delegation);
				}
			}
		}
		
		return result;
	}

	@Override
	public List<Role> findControlBodyRoles(RE_Register register) {
		List<Role> result = new ArrayList<Role>();
		
		Role registerCbRole = registerService.getControlBodyRole(register);
		result.add(registerCbRole);
		
		return result;
	}

	@Override
	public List<Authorization> findControlBodyAuthorizations(RE_Register register) {
		List<Authorization> result = new ArrayList<>();
		
		List<Role> roles = this.findControlBodyRoles(register);
		result.addAll(findControlBodyAuthorizations(roles));
			
		return result;
	}
		
}
