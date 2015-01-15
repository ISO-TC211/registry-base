package de.geoinfoffm.registry.api;

/**
 * Thrown when an entity is not to be found in the backend.
 * 
 * @author Florian Esser
 *
 */
public class EntityNotFoundException extends RuntimeException
{

	/**
	 * 
	 */
	public EntityNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public EntityNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public EntityNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public EntityNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
