/**
 * 
 */
package de.geoinfoffm.registry.core;

/**
 * Service to create secure hash values for passwords.
 * 
 * @author Florian Esser
 *
 */
public class PasswordEncodingServiceImpl implements PasswordEncodingService
{
	private HashingStrategy strategy;
	
	/**
	 * Creates a password encoding service with the default encoding strategy
	 */
	public PasswordEncodingServiceImpl() {
		this.strategy = new BCryptHashingStrategy();
	}
	
	/**
	 * Creates a password encoding service with the given encoding strategy
	 * @param strategy Hashing strategy to use
	 */
	public PasswordEncodingServiceImpl(HashingStrategy strategy) {
		this.strategy = strategy;
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.PasswordEncodingService#encodePassword(java.lang.String)
	 */
	@Override
	public String encode(CharSequence password) {
		return strategy.encode(password);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return strategy.matches(rawPassword, encodedPassword);
	}
}
