/**
 * 
 */
package de.geoinfoffm.registry.core;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Hashing strategy to be used by a {@link PasswordEncodingService}.<br>
 * <br>
 * This strategy uses the {@link BCryptPasswordEncoder} to create secure
 * password hashes.
 * 
 * @author Florian Esser
 *
 */
public class BCryptHashingStrategy implements HashingStrategy
{
	private BCryptPasswordEncoder passwordEncoder;

	public BCryptHashingStrategy() {
		passwordEncoder = new BCryptPasswordEncoder(12);
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.HashingStrategy#hash(java.lang.String)
	 */
	@Override
	public String encode(CharSequence value) {
		return passwordEncoder.encode(value); 
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
}
