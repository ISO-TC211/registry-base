/**
 * 
 */
package de.geoinfoffm.registry.api;

import de.geoinfoffm.registry.core.model.RegistryUser;

/**
 * Exception class that indicates an exception during the update
 * of a {@link RegistryUser}.
 * 
 * @author Florian Esser
 *
 */
public class UpdateUserException extends Exception
{

	/**
	 * 
	 */
	public UpdateUserException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UpdateUserException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UpdateUserException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UpdateUserException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UpdateUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
