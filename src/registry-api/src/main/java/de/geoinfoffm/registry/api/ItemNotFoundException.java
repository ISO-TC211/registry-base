/**
 * 
 */
package de.geoinfoffm.registry.api;

import java.util.UUID;

/**
 * @author Florian.Esser
 *
 */
public class ItemNotFoundException extends EntityNotFoundException
{

	/**
	 * 
	 */
	public ItemNotFoundException() {
		// TODO Auto-generated constructor stub
	}
	
	public ItemNotFoundException(UUID itemId) {
		super(String.format("An item with ID '%s' does not exist.", itemId));
	}

	/**
	 * @param message
	 */
	public ItemNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ItemNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ItemNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ItemNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
