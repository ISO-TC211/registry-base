/**
 * 
 */
package de.geoinfoffm.registry.core.model;

import java.util.Collection;

/**
 * @author Florian.Esser
 *
 */
public interface NameBuildingStrategy
{
	public <P extends Proposal> String buildName(Collection<P> proposals);
}
