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

import java.util.Locale;
import java.util.UUID;

import javax.jws.WebService;

import org.springframework.stereotype.Service;

import de.geoinfoffm.registry.api.soap.CreateOrganizationRequest;
import de.geoinfoffm.registry.api.soap.CreateRegistryUserRequest;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.RegistryUser;

/**
 * Service operations for {@link RegistryUser}s in the GDI-DE Registry.
 * 
 * @author Florian Esser
 */
@Service
public interface RegistryUserService extends ApplicationService<RegistryUser>
{
	public static final String SEND_CONFIRMATION_MAIL = "SEND_CONFIRMATION_MAIL";

	/**
	 * Registers a new user.
	 * 
	 * @param registrationData User details
	 * @return The created {@link RegistryUser}
	 * @throws UnauthorizedException 
	 */
	RegistryUser registerUser(CreateRegistryUserRequest registrationData) throws UserRegistrationException, UnauthorizedException;
	RegistryUser registerUser(CreateRegistryUserRequest request, CreateOrganizationRequest newOrganizationDetails) throws UserRegistrationException, UnauthorizedException;
	RegistryUser updateUser(RegistryUserUpdateDTO userData) throws UpdateUserException;
	boolean confirmUser(String emailAddress, UUID token);
	public void requestPasswordReset(String emailAddress);
	public abstract void resetPassword(String emailAddress, String token, String newPassword);

	RegistryUser findByEmailAddress(String emailAddress);
	boolean isEmailAddressAvailable(String emailAddress);

	Locale getLocaleForLanguageTag(String languageTag);
	
	void deactivateConfirmationMails();
	void activateConfirmationMails();
}
