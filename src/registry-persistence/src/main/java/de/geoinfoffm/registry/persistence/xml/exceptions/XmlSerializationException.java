package de.geoinfoffm.registry.persistence.xml.exceptions;

/**
 * An exception during XML serialization.
 * 
 * @author Florian Esser
 *
 */
public class XmlSerializationException extends SerializationException
{
	private static final long serialVersionUID = -5062124982662307430L;

	public XmlSerializationException() {
		// TODO Auto-generated constructor stub
	}

	public XmlSerializationException(String message) {
		super(message);
	}

	public XmlSerializationException(Throwable cause) {
		super(cause);
	}

	public XmlSerializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public XmlSerializationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
