/**
 * 
 */
package de.geoinfoffm.registry.api;

/**
 * @author Florian.Esser
 *
 */
public class NoRevisionAtThisPointInTimeException extends Exception
{

	/**
	 * 
	 */
	public NoRevisionAtThisPointInTimeException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NoRevisionAtThisPointInTimeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NoRevisionAtThisPointInTimeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoRevisionAtThisPointInTimeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NoRevisionAtThisPointInTimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
