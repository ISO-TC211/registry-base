/**
 * 
 */
package de.geoinfoffm.registry.api;

import de.geoinfoffm.registry.core.model.RegistryUser;

/**
 * Exception class that indicates an exception during the registration
 * of a {@link RegistryUser}.
 * 
 * @author Florian Esser
 *
 */
public class UserRegistrationException extends Exception
{

	/**
	 * 
	 */
	public UserRegistrationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UserRegistrationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UserRegistrationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UserRegistrationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UserRegistrationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
