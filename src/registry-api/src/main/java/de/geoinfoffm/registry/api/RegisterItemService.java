/**
 * 
 */
package de.geoinfoffm.registry.api;

import java.io.Writer;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import org.isotc211.iso19135.RE_RegisterItem_Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.persistence.xml.exceptions.XmlSerializationException;

/**
 * @author Florian.Esser
 *
 */
public interface RegisterItemService extends ApplicationService<RE_RegisterItem>
{
	Page<RE_RegisterItem> findAll(Pageable pageable);
	List<RE_RegisterItem> findAll(String className, String orderBy);
	Page<RE_RegisterItem> findByStatus(RE_ItemStatus status, Pageable pageable);
	Page<RE_RegisterItem> findByRegisterAndStatus(RE_Register register, RE_ItemStatus status, Pageable pageable);
	Page<RE_RegisterItem> findByRegisterAndItemClassAndStatus(RE_Register register, RE_ItemClass itemClass, RE_ItemStatus status, Pageable pageable);
	
	Set<RE_RegisterItem> findByStatus(RE_ItemStatus status);

	RE_RegisterItem createRegisterItem(RE_RegisterItem_Type item);
		
	RE_RegisterItem saveRegisterItem(RE_RegisterItem item);
		
	RE_AdditionInformation findFinalAdditionInformation(RE_RegisterItem containedItem);
	
	BigInteger findMaxItemIdentifier();
	
	void toXml(RE_RegisterItem item, Writer output) throws XmlSerializationException;
}
