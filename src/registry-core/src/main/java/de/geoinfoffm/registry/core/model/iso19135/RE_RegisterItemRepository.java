/**
 * 
 */
package de.geoinfoffm.registry.core.model.iso19135;

import java.math.BigInteger;

import org.springframework.data.repository.NoRepositoryBean;

import de.geoinfoffm.registry.core.EntityRepository;

/**
 * @author Florian.Esser
 *
 */
@NoRepositoryBean
public interface RE_RegisterItemRepository extends EntityRepository<RE_RegisterItem>
{
	public BigInteger findMaxItemIdentifier();
}
