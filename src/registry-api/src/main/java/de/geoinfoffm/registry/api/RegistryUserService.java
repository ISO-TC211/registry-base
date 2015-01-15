/**
 * 
 */
package de.geoinfoffm.registry.api;

import java.util.Locale;
import java.util.UUID;

import javax.jws.WebService;

import org.springframework.stereotype.Service;

import de.geoinfoffm.registry.api.ApplicationService;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.soap.CreateOrganizationRequest;
import de.geoinfoffm.registry.soap.CreateRegistryUserRequest;

/**
 * Service operations for {@link RegistryUser}s in the GDI-DE Registry.
 * 
 * @author Florian Esser
 */
@Service
@WebService
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
	
	RegistryUser findByEmailAddress(String emailAddress);
	boolean isEmailAddressAvailable(String emailAddress);

	Locale getLocaleForLanguageTag(String languageTag);
	
	void deactivateConfirmationMails();
	void activateConfirmationMails();
}
