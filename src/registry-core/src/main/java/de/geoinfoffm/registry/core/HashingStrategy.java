/**
 * 
 */
package de.geoinfoffm.registry.core;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Interface for hashing strategies to be used e.g. by a {@link PasswordEncodingService}.
 * 
 * @author Florian Esser
 *
 */
public interface HashingStrategy extends PasswordEncoder
{
}
