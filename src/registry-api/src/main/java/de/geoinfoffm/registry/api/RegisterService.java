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
 * @author Florian Esser
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
	public boolean containsItemClass(RE_Register register, String itemClassName);
	
//	public BigInteger nextItemIdentifier();
	
	public RegisterRelatedRole getManagerRole(RE_Register register);
	public RegisterRelatedRole getOwnerRole(RE_Register register);
	public RegisterRelatedRole getSubmitterRole(RE_Register register);
	public RegisterRelatedRole getControlBodyRole(RE_Register register);
	
	public void toXml(RE_Register register, Writer output) throws XmlSerializationException;
}
