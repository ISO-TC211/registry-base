package de.geoinfoffm.registry.api.converter;

import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public interface ItemVisitor
{
	public boolean visit(RE_RegisterItem item);
}
