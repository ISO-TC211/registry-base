package de.geoinfoffm.registry.api;

import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public interface RegisterItemFactory<I extends RE_RegisterItem, P extends RegisterItemProposalDTO>
{
	I createRegisterItem(P proposal);
}
