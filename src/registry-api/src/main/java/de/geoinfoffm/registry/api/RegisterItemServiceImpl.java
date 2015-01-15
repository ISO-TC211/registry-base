/**
 * 
 */
package de.geoinfoffm.registry.api;

import java.io.Writer;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import org.isotc211.iso19135.RE_RegisterItem_Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.geoinfoffm.registry.core.model.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.core.model.iso19135.ProposalManagementInformationRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.persistence.AppealRepository;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.ProposalRepository;
import de.geoinfoffm.registry.persistence.RegisterItemRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;
import de.geoinfoffm.registry.persistence.ResponsiblePartyRepository;
import de.geoinfoffm.registry.persistence.SupersessionRepository;
import de.geoinfoffm.registry.persistence.xml.StaxSerializationStrategy;
import de.geoinfoffm.registry.persistence.xml.StaxXmlSerializer;
import de.geoinfoffm.registry.persistence.xml.exceptions.XmlSerializationException;

/**
 * @author Florian.Esser
 *
 */
@Transactional
@Service
public class RegisterItemServiceImpl 
extends AbstractApplicationService<RE_RegisterItem, RegisterItemRepository>
implements RegisterItemService
{
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private RegisterRepository registerRepository;
	
	@Autowired
	private SubmittingOrganizationRepository submittingOrgRepository;
	
	@Autowired
	private ResponsiblePartyRepository partyRepository;

	@Autowired
	private ItemClassRepository itemClassRepository;

	@Autowired
	private RegisterItemRepository itemRepository;

	@Autowired
	private ProposalRepository proposalRepository;

	@Autowired
	private ProposalManagementInformationRepository pmiRepository;

	@Autowired
	private AppealRepository appealRepository;
	
	@Autowired
	private SupersessionRepository supersessionRepository;
	
	@Autowired
	public RegisterItemServiceImpl(RegisterItemRepository repository) {
		super(repository);
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#findAll(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<RE_RegisterItem> findAll(Pageable pageable) {
		return itemRepository.findAll(pageable);
	}
	
	@Override
	public List<RE_RegisterItem> findAll(String entityName, String orderBy) {
		String jpql = "select i from " + entityName + " order by " + orderBy;

//		CriteriaQuery<RE_RegisterItem> cq = entityManager.getCriteriaBuilder().createQuery(RE_RegisterItem.class);
		return entityManager.createQuery(jpql).getResultList();
	}

	@Override
	public void toXml(RE_RegisterItem item, Writer output) throws XmlSerializationException {
		StaxXmlSerializer<Writer> s = new StaxXmlSerializer<Writer>(new StaxSerializationStrategy(true));
		s.serialize(item, output);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#findAdditionInformation(de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem)
	 */
	@Override
	public RE_AdditionInformation findFinalAdditionInformation(RE_RegisterItem containedItem) {
		List<RE_ProposalManagementInformation> pmis = pmiRepository.findByItem(containedItem);
		if (pmis == null) {
			return null;
		}
		
		for (RE_ProposalManagementInformation pmi : pmis) {
			if (pmi instanceof RE_AdditionInformation && pmi.getStatus().equals(RE_DecisionStatus.FINAL)) {
				return (RE_AdditionInformation)pmi;
			}
		}
		
		return null;
	}

	@Override
	public RE_RegisterItem createRegisterItem(RE_RegisterItem_Type item) {
	    return null;
	}
	
	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#findByStatus(de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus)
	 */
	@Override
	public Set<RE_RegisterItem> findByStatus(RE_ItemStatus status) {
		return itemRepository.findByStatus(status);
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.api.RegisterItemService#findByStatus(de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<RE_RegisterItem> findByStatus(RE_ItemStatus status, Pageable pageable) {
		return itemRepository.findByStatus(status, pageable);
	}

	@Override
	public Page<RE_RegisterItem> findByRegisterAndStatus(RE_Register register, RE_ItemStatus status, Pageable pageable) {
		return itemRepository.findByRegisterAndStatus(register, status, pageable);
	}

	@Override
	public Page<RE_RegisterItem> findByRegisterAndItemClassAndStatus(RE_Register register, RE_ItemClass itemClass, RE_ItemStatus status, Pageable pageable) {
		return itemRepository.findByRegisterAndItemClassAndStatus(register, itemClass, status, pageable);
	}

	@Override
	public RE_RegisterItem saveRegisterItem(RE_RegisterItem item) {
		item = repository().save(item);
		
		// TODO Publish event
		
		return item;
	}

	@Override
	@Transactional(readOnly = true)
	public BigInteger findMaxItemIdentifier() {
		return repository().findMaxItemIdentifier();
	}

}
