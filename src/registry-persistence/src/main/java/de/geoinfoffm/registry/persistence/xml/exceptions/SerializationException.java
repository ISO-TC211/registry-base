package de.geoinfoffm.registry.persistence.xml.exceptions;

/**
 * An exception during the serialization process.
 * 
 * @author Florian Esser
 *
 */
public class SerializationException extends Exception {
	private static final long serialVersionUID = -8238829944445680597L;

	public SerializationException() {
		// TODO Auto-generated constructor stub
	}

	public SerializationException(String message) {
		super(message);
	}

	public SerializationException(Throwable cause) {
		super(cause);
	}

	public SerializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SerializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
