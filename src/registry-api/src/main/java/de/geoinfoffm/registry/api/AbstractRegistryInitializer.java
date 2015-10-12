package de.geoinfoffm.registry.api;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.isotc211.iso19135.RE_SubmittingOrganization_PropertyType;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import de.bespire.LoggerFactory;
import de.geoinfoffm.registry.api.OrganizationService;
import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.RoleService;
import de.geoinfoffm.registry.api.UserRegistrationException;
import de.geoinfoffm.registry.api.soap.CreateOrganizationRequest;
import de.geoinfoffm.registry.api.soap.CreateRegistryUserRequest;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.ParameterizedRunnable;
import de.geoinfoffm.registry.core.RegistersChangedEvent;
import de.geoinfoffm.registry.core.RegistryInitializer;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.Delegation;
import de.geoinfoffm.registry.core.model.DelegationRepository;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.OrganizationRepository;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.RegistryUserGroupRepository;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.iso19115.CI_ResponsibleParty;
import de.geoinfoffm.registry.core.model.iso19115.CI_RoleCode;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.model.iso19135.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;

public abstract class AbstractRegistryInitializer implements RegistryInitializer, ApplicationEventPublisherAware
{
	private static final Logger logger = LoggerFactory.make();

	@Autowired
	protected RegistryUserGroupRepository userGroupRepository;

	@Autowired
	private ItemClassRepository itemClassRepository;

	@Autowired
	private RegisterRepository registerRepository;

	@Autowired
	private RegistryUserRepository userRepository;

	@Autowired
	private RegistryUserService userService;

	@Autowired
	protected OrganizationService orgService;

	@Autowired
	private OrganizationRepository orgRepository;

	@Autowired
	private SubmittingOrganizationRepository suborgRepository;

	@Autowired
	protected RegisterService registerService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private RegisterItemService itemService;

	@Autowired
	private ProposalService proposalService;

	@Autowired
	private MutableAclService mutableAclService;

	@Autowired
	private DelegationRepository delegationRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private String status;
	private StringBuilder initLog;
	private ApplicationEventPublisher eventPublisher;

	public AbstractRegistryInitializer() {
		this.status = RegistryInitializer.STATUS_NOT_INITIALIZING;
	}

	@Override
	@Transactional
	public void initializeRegistry() throws Exception {
		status = STATUS_INITIALIZING;
		initLog = new StringBuilder();
		log("Initializing registry...");

		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		try {
			Authentication authentication = new RunAsUserToken("SYSTEM", "SYSTEM", "N/A", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")), currentAuth.getClass());
			SecurityContextHolder.getContext().setAuthentication(authentication);

			initialize();
			loadExampleData();
			log("");

			log("Initialization complete.");
		}
		catch (Throwable t) {
			t.printStackTrace();
			log("\n\n");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			log(sw.toString());
		}
		finally {
			SecurityContextHolder.getContext().setAuthentication(currentAuth);
			status = STATUS_DONE;
		}
	}

	protected abstract void initialize() throws Exception;

	protected abstract void loadExampleData() throws Exception;

	@Override
	public String log() {
		if (this.initLog == null) {
			this.initLog = new StringBuilder();
		}
		return this.initLog.toString();
	}

	@Override
	public String status() {
		return this.status;
	}

	protected Organization createOrganization(String name, String shortName, Collection<Organization> organizations)
			throws UnauthorizedException {
		Organization result = orgRepository.findByName(name);
		if (result != null) {
			return result;
		}

		CI_ResponsibleParty respExample = new CI_ResponsibleParty("John Doe", null, null, CI_RoleCode.USER);
		entityManager.persist(respExample);
		RE_SubmittingOrganization orgExample = new RE_SubmittingOrganization(name, respExample);
		orgExample = suborgRepository.save(orgExample);

		RE_SubmittingOrganization_PropertyType pt = new RE_SubmittingOrganization_PropertyType();
		pt.setUuidref(orgExample.getUuid().toString());

		CreateOrganizationRequest cor = new CreateOrganizationRequest();
		cor.setName(name);
		cor.setShortName(shortName);
		cor.setSubmittingOrganization(pt);

		Organization org = orgService.createOrganization(cor);
		organizations.add(org);

		return org;
	}

	protected RE_ItemClass addItemClassToRegister(String name, RE_Register r) {
		RE_ItemClass ic = null;
		for (RE_ItemClass itemClass : r.getContainedItemClasses()) {
			if (itemClass.getName().equals(name)) {
				ic = itemClass;
				break;
			}
		}

		if (ic == null) {
			ic = itemClassRepository.findByName(name);
			if (ic == null) {
				ic = new RE_ItemClass();
				ic.setName(name);
			}
			log(String.format("> Adding item class '%s' to register '%s'...", name, r.getName()));
			r.getContainedItemClasses().add(ic);
			ic = itemClassRepository.save(ic);
			r = registerRepository.save(r);

			log(String.format(">>> %s", ic.getName()));
		}

		return ic;
	}

	public <P extends RegisterItemProposalDTO> RE_RegisterItem registerItem(RE_Register register, RE_ItemClass itemClass, String name,
			BigInteger itemIdentifier, Organization sponsor, Class<P> dtoClass, ParameterizedRunnable<P> paramSetter)
			throws InvalidProposalException, InstantiationException, IllegalAccessException, UnauthorizedException {
		P proposal;
		proposal = BeanUtils.instantiateClass(dtoClass);
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getSubmittingOrganization().getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		proposal.setName(name);
		proposal.setDefinition("Definition");
		proposal.setJustification("Justification");

		paramSetter.run(proposal);

		log(String.format("> Adding item '%s' of class '%s' to register '%s'...", proposal.getName(), itemClass.getName(),
				register.getName()));

		Addition ai = proposalService.createAdditionProposal(proposal);
		proposalService.submitProposal(ai);

		String decisionEvent = "Decision event";
		acceptProposal(ai, decisionEvent, itemIdentifier);

		return ai.getItem();
	}

	protected void acceptProposal(Addition ai, String decisionEvent, BigInteger itemIdentifier) throws InvalidProposalException,
			UnauthorizedException {
		try {
			if (itemIdentifier != null) {
				ai.getItem().setItemIdentifier(itemIdentifier);
				itemService.saveRegisterItem(ai.getItem());
			}
			proposalService.reviewProposal(ai);
			proposalService.acceptProposal(ai, decisionEvent);
		}
		catch (IllegalOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected RE_Register createRegister(final String name, final Organization organization) {
		RE_Register r = registerService.findByName(name);
		if (r == null) {
			log("> Creating register...");
			r = registerService.createRegister(
					name,
					organization, organization, organization,
					roleService, RE_Register.class,
					new ParameterizedRunnable<RE_Register>() {
						@Override
						public void run(RE_Register parameter) {
						}
					}
					);

			eventPublisher.publishEvent(new RegistersChangedEvent(this));

			log(String.format(">>> '%s' (owner = %s; manager = %s)", r.getName(), r.getOwner().getName(), r.getManager().getName()));
		}
		return r;
	}

	protected RegistryUser createUser(String name, String password, String mail, Organization organization, Role... roles)
			throws UserRegistrationException, UnauthorizedException {

		RegistryUser existingUser = userRepository.findByEmailAddressIgnoreCase(mail);

		if (existingUser != null) {
			return existingUser;
		}

		CreateRegistryUserRequest req = new CreateRegistryUserRequest();
		req.setName(name);
		req.setPassword(password);
		req.setOrganizationUuid(organization.getUuid().toString());
		req.setEmailAddress(mail);
		req.setPreferredLanguage("en");
		req.setActive(true);
		for (Role role : roles) {
			req.getRole().add(role.getName());
		}

		log(String.format(">>> %s", mail));

		RegistryUser result = userService.registerUser(req);
		for (Delegation delegation : delegationRepository.findByActorAndDelegatingOrganizationAndIsApprovedFalse(result, organization)) {
			delegation.setApproved(true);
			delegationRepository.save(delegation);
		}
		
		return result;
	}

	protected void delegateRegisterRole(RE_Register register, Organization organization, Role role, RegistryUser user)
			throws UnauthorizedException {
		orgService.delegate(user, role, organization);
		log(String.format(">>>  %s delegates '%s' for register '%s' to '%s'", organization.getShortName(), role.getName(),
				register.getName(), user.getEmailAddress()));
	}

	protected void delegateOrganizationRole(Organization organization, Role role, RegistryUser user) throws UnauthorizedException {
		orgService.delegate(user, role, organization);
		log(String.format(">>>  %s delegates '%s' to '%s'", organization.getShortName(), role.getName(), user.getEmailAddress()));
	}

	private static String leadingZero(int i) {
		if (i < 10) {
			return "0" + Integer.toString(i);
		}
		else {
			return Integer.toString(i);
		}
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}

	protected void log(String value) {
		initLog.append(value + "\n");
		logger.debug(value);
	}

}
