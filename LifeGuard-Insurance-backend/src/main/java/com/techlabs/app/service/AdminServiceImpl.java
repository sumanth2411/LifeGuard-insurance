package com.techlabs.app.service;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.CityRequestDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.dto.InsurancePlanDTO;

import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.InsuranceSchemeDto;
import com.techlabs.app.dto.PaymentDto;
import com.techlabs.app.dto.SchemeDocumentDto;
import com.techlabs.app.dto.StateRequestDto;
import com.techlabs.app.dto.StateResponseDto;
import com.techlabs.app.dto.CityResponseDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.ClaimResponseDto;
import com.techlabs.app.dto.CommissionDto;
import com.techlabs.app.dto.CustomerDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Employee;
import com.techlabs.app.entity.InsurancePlan;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.Nominee;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.ClaimStatus;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.entity.Payment;
import com.techlabs.app.entity.PaymentTax;
import com.techlabs.app.entity.PolicyStatus;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.SchemeDocument;
import com.techlabs.app.entity.State;
import com.techlabs.app.entity.SubmittedDocument;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.LifeGuardException;
import com.techlabs.app.exception.ResourceNotFoundException;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.ClaimRepository;
import com.techlabs.app.repository.CommissionRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.DocumentRepository;
import com.techlabs.app.repository.EmployeeRepository;
import com.techlabs.app.repository.InsurancePlanRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.techlabs.app.repository.InsuranceSchemeRepository;
import com.techlabs.app.repository.NomineeRepository;

import com.techlabs.app.repository.PaymentRepository;
import com.techlabs.app.repository.PaymentTaxRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.StateRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.util.ImageUtil;
import com.techlabs.app.util.PagedResponse;

import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;
import java.util.Collections;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private AgentRepository agentRepository;
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private InsurancePlanRepository insurancePlanRepository;
	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;
	@Autowired
	private InsuranceSchemeRepository insuranceSchemeRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private DocumentRepository documentRepository;
	@Autowired
	private NomineeRepository nomineeRepository;
	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private ClaimRepository claimRepository;

	@Autowired
	private CommissionRepository commissionRepository;

	@Autowired
	private PaymentTaxRepository paymentTaxRepository;

	public AdminServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository, AgentRepository agentRepository,
			CityRepository cityRepository, InsurancePlanRepository insurancePlanRepository,
			InsurancePolicyRepository insurancePolicyRepository, InsuranceSchemeRepository insuranceSchemeRepository,
			CustomerRepository customerRepository, DocumentRepository documentRepository,
			NomineeRepository nomineeRepository, PaymentRepository paymentRepository, StateRepository stateRepository,
			ClaimRepository claimRepository, CommissionRepository commissionRepository) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.employeeRepository = employeeRepository;
		this.agentRepository = agentRepository;
		this.cityRepository = cityRepository;
		this.insurancePlanRepository = insurancePlanRepository;
		this.insurancePolicyRepository = insurancePolicyRepository;
		this.insuranceSchemeRepository = insuranceSchemeRepository;
		this.customerRepository = customerRepository;
		this.documentRepository = documentRepository;
		this.nomineeRepository = nomineeRepository;
		this.paymentRepository = paymentRepository;
		this.stateRepository = stateRepository;
		this.claimRepository = claimRepository;
		this.commissionRepository = commissionRepository;
	}

	@Transactional
	@Override
	public String registerEmployee(EmployeeRequestDto employeeRequestDto) {

		if (userRepository.existsByUsername(employeeRequestDto.getUsername())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
		}
		if (userRepository.existsByEmail(employeeRequestDto.getEmail())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
		}

		User user = new User();
		user.setUsername(employeeRequestDto.getUsername());
		user.setEmail(employeeRequestDto.getEmail());
		user.setPassword(passwordEncoder.encode(employeeRequestDto.getPassword()));

		Role employeeRole = roleRepository.findByName("ROLE_EMPLOYEE")
				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: ROLE_EMPLOYEE"));
		Set<Role> roles = new HashSet<>();
		roles.add(employeeRole);
		user.setRoles(roles);

		User savedUser = userRepository.save(user);

		Employee employee = new Employee();
		employee.setEmployeeId(savedUser.getId());
		employee.setUser(savedUser);
		employee.setFirstName(employeeRequestDto.getFirstName());
		employee.setLastName(employeeRequestDto.getLastName());
		employee.setActive(employeeRequestDto.isActive());

		employeeRepository.save(employee);
		return "Employee registered successfully";
	}

	@Transactional
	@Override
	public String registerAgent(AgentRequestDto agentRequestDto) {

		if (userRepository.existsByUsername(agentRequestDto.getUsername())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
		}
		if (userRepository.existsByEmail(agentRequestDto.getEmail())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
		}

		User user = new User();
		user.setUsername(agentRequestDto.getUsername());
		user.setEmail(agentRequestDto.getEmail());
		user.setPassword(passwordEncoder.encode(agentRequestDto.getPassword()));

		Role agentRole = roleRepository.findByName("ROLE_AGENT")
				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: ROLE_AGENT"));
		Set<Role> roles = new HashSet<>();
		roles.add(agentRole);
		user.setRoles(roles);

		User savedUser = userRepository.save(user);

		City city = cityRepository.findById(agentRequestDto.getCity_id())
				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,
						"City not found with id: " + agentRequestDto.getCity_id()));

		Agent agent = new Agent();
		agent.setAgentId(savedUser.getId());
		agent.setUser(savedUser);
		agent.setFirstName(agentRequestDto.getFirstName());
		agent.setLastName(agentRequestDto.getLastName());
		agent.setPhoneNumber(agentRequestDto.getPhoneNumber());
		agent.setCity(city);
		agent.setActive(agentRequestDto.isActive());

		agent.setVerified(true);

		agentRepository.save(agent);
		return "Agent Registered successfully";
	}

	@Override
	public String approveAgentClaim(Long claimId, ClaimResponseDto claimDto) {
		Claim claim = claimRepository.findById(claimId).orElseThrow(() -> new RuntimeException("Claim not found"));

		if (!ClaimStatus.PENDING.name().equals(claim.getClaimedStatus())) {
			throw new RuntimeException("Claim is not in pending status.");
		}

		System.out.println("Claim status is pending. Proceeding with approval/rejection.");
		System.out.println("Claimed status from DTO: " + claimDto.getClaimedStatus());
		System.out.println(claimDto);
		System.out.println(claimId);
		if (claimDto.getClaimedStatus()) {

			Agent agent = claim.getAgent();
			double totalCommission = agent.getTotalCommission();

			System.out.println("Agent's total commission: " + totalCommission);
			System.out.println("Claim amount: " + claim.getClaimAmount());

			if (totalCommission < claim.getClaimAmount()) {
				throw new LifeGuardException("Insufficient total commission amount.");
			}

			totalCommission -= claim.getClaimAmount();
			agent.setTotalCommission(totalCommission);
			agentRepository.save(agent);

			claim.setClaimedStatus(ClaimStatus.APPROVED.name());
			System.out.println("Claim approved. New commission: " + totalCommission);
		}

		else {
			claim.setClaimedStatus(ClaimStatus.REJECT.name());
			System.out.println("Claim rejected.");
		}

		claimRepository.save(claim);

		return "Claim ID " + claimId + " has been " + (claimDto.getClaimedStatus() ? "approved" : "rejected")
				+ " and the commission has been " + (claimDto.getClaimedStatus() ? "deducted." : "not deducted.");
	}

	@Override
	public PagedResponse<ClaimRequestDto> getPendingCustomerClaims(int page, int size, String sortBy,
			String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);
		Page<Claim> pendingClaimsPage = claimRepository.findByClaimedStatus("PENDING", pageable);

		List<ClaimRequestDto> claimRequestDtos = pendingClaimsPage.getContent().stream().map(this::convertToDto)
				.collect(Collectors.toList());

		return new PagedResponse<>(claimRequestDtos, pendingClaimsPage.getNumber(), pendingClaimsPage.getSize(),
				pendingClaimsPage.getTotalElements(), pendingClaimsPage.getTotalPages(), pendingClaimsPage.isLast());
	}

	private ClaimRequestDto convertToDto(Claim claim) {
		ClaimRequestDto dto = new ClaimRequestDto();
		dto.setPolicyId(claim.getPolicy().getInsuranceId());
		dto.setId(claim.getId());
		dto.setClaimAmount(claim.getClaimAmount());
		dto.setBankName(claim.getBankName());
		dto.setBranchName(claim.getBranchName());
		dto.setBankAccountId(claim.getBankAccountId());
		dto.setIfscCode(claim.getIfscCode());
		dto.setDate(claim.getDate());
		dto.setClaimedStatus(claim.getClaimedStatus());
		// Add more fields as needed

		return dto;
	}

	@Override
	@Transactional
	public String approveCustomerClaim(Long claimId, ClaimResponseDto claimDto) {

		Claim claim = claimRepository.findById(claimId).orElseThrow(() -> new LifeGuardException("Claim not found"));

		if (!claim.getClaimedStatus().equals("PENDING")) {
			throw new LifeGuardException("Claim is not in pending status.");
		}

		InsurancePolicy policy = claim.getPolicy();

		if (claimDto.getClaimedStatus()) {

			double claimAmount = claim.getClaimAmount();
			double totalAmountPaid = policy.getTotalAmountPaid();

			if (totalAmountPaid >= claimAmount) {
				totalAmountPaid -= claimAmount;
				policy.setTotalAmountPaid(0.0);
			} else {
				throw new LifeGuardException("Insufficient total amount paid for deduction.");
			}

			claim.setClaimedStatus("APPROVED");

			// Set policy status to CLAIMED
			policy.setPolicyStatus(PolicyStatus.CLAIMED.name());

		} else {

			claim.setClaimedStatus("REJECTED");
		}

		claimRepository.save(claim);
		insurancePolicyRepository.save(policy);

		return "Claim has been processed and the amount has been deducted from the total amount paid.";
	}

	@Override
	@Transactional
	public String verifyAgent(Long agentId) {
		Agent agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Agent not found with id: " + agentId));

		agent.setVerified(true);
		agentRepository.save(agent);

		return "Agent verified successfully";
	}

	@Override
	@Transactional
	public String updateEmployee(Long employeeId, EmployeeRequestDto employeeRequestDto) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Employee not found with id: " + employeeId));

		User user = employee.getUser();

		user.setUsername(employeeRequestDto.getUsername());
		user.setEmail(employeeRequestDto.getEmail());
		if (employeeRequestDto.getPassword() != null && !employeeRequestDto.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(employeeRequestDto.getPassword()));
		}

		employee.setFirstName(employeeRequestDto.getFirstName());
		employee.setLastName(employeeRequestDto.getLastName());
		employee.setActive(employeeRequestDto.isActive());

		userRepository.save(user);
		employeeRepository.save(employee);

		return "Employee updated successfully";
	}

	@Override
	@Transactional
	public String updateAgent(Long agentId, AgentRequestDto agentRequestDto) {

		Agent agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Agent not found with id: " + agentId));

		User user = agent.getUser();

		user.setUsername(agentRequestDto.getUsername());
		user.setEmail(agentRequestDto.getEmail());
		if (agentRequestDto.getPassword() != null && !agentRequestDto.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(agentRequestDto.getPassword()));
		}

		City city = cityRepository.findById(agentRequestDto.getCity_id())
				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,
						"City not found with id: " + agentRequestDto.getCity_id()));

		agent.setFirstName(agentRequestDto.getFirstName());
		agent.setLastName(agentRequestDto.getLastName());
		agent.setPhoneNumber(agentRequestDto.getPhoneNumber());
		agent.setCity(city);
		agent.setActive(agentRequestDto.isActive());

		if (agentRequestDto.getVerified() != null) {
			agent.setVerified(agentRequestDto.getVerified());
		}

		userRepository.save(user);
		agentRepository.save(agent);

		return "Agent updated successfully";
	}

	@Override
	public String createState(StateRequestDto stateRequest) {
		State state = new State();
		state.setName(stateRequest.getName());
		state.setActive(true); // Default value
		stateRepository.save(state);
		return "State Added Successfully";
	}

	@Override
	public PagedResponse<StateResponseDto> getAllStates(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);
		Page<State> statePage = stateRepository.findAll(pageable);

		List<StateResponseDto> stateResponseList = statePage.getContent().stream().map(state -> {
			StateResponseDto response = new StateResponseDto();
			response.setStateId(state.getStateId());
			response.setName(state.getName());
			response.setActive(state.isActive());
			response.setCities(state.getCities().stream().map(city -> {
				CityResponseDto cityResponse = new CityResponseDto();
				cityResponse.setCityId(city.getId());
				cityResponse.setName(city.getCity_name());
				cityResponse.setActive(city.isActive());
				return cityResponse;
			}).collect(Collectors.toList()));
			return response;
		}).collect(Collectors.toList());

		return new PagedResponse<>(stateResponseList, statePage.getNumber(), statePage.getSize(),
				statePage.getTotalElements(), statePage.getTotalPages(), statePage.isLast());
	}

	@Override
	@Transactional
	public String deactivateStateById(long id) {
		State state = stateRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("State not found"));

		if (state.isActive()) {

			state.setActive(false);

			state.getCities().forEach(city -> {
				if (city.isActive()) {
					city.setActive(false);
					cityRepository.save(city);
				}
			});

			stateRepository.save(state);
		} else {
			throw new IllegalStateException("State is already deactivated");
		}

		return "State and its related cities deactivated successfully";
	}

	@Override
	public String activateStateById(long id) {

		State state = stateRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("State not found"));
		if (!state.isActive()) {
			state.setActive(true);
			stateRepository.save(state);
		} else {
			throw new IllegalStateException("State is already activated");
		}
		return "State activated successfully";

	}

	@Override
	public String createCity(CityRequestDto cityRequest) {
		try {
			State state = stateRepository.findById(cityRequest.getState_id())
					.orElseThrow(() -> new IllegalArgumentException("Invalid state ID"));

			City city = new City();
			city.setCity_name(cityRequest.getName());
			city.setState(state);
			city.setActive(true);
			cityRepository.save(city);
			return "City Added Successfully";
		} catch (Exception e) {
			throw new RuntimeException("Error creating city: " + e.getMessage());
		}
	}

	@Override
	public String deactivateCity(long id) {
		City city = cityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("City not found"));
		if (city.isActive()) {
			city.setActive(false);
			cityRepository.save(city);

		}
		return "City deactivated successfully";
	}

	@Override
	public CityResponseDto getCityById(long id) {
		City city = cityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("City not found"));
		CityResponseDto cityResponse = new CityResponseDto();
		cityResponse.setCityId(city.getId());
		cityResponse.setName(city.getCity_name());
		cityResponse.setActive(city.isActive());
		return cityResponse;
	}

	@Override
	public String activateCity(long id) {
		City city = cityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("City not found"));
		if (!city.isActive()) {
			city.setActive(true);
			cityRepository.save(city);
		}
		return "City activated successfully";
	}

	@Override
	public PagedResponse<CityResponseDto> getAllCities(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);
		Page<City> cityPage = cityRepository.findAll(pageable);

		List<CityResponseDto> cityResponseList = cityPage.getContent().stream().map(city -> {
			CityResponseDto response = new CityResponseDto();
			response.setCityId(city.getId());
			response.setName(city.getCity_name());
			response.setActive(city.isActive());
			return response;
		}).collect(Collectors.toList());

		return new PagedResponse<>(cityResponseList, cityPage.getNumber(), cityPage.getSize(),
				cityPage.getTotalElements(), cityPage.getTotalPages(), cityPage.isLast());
	}

	@Override
	public String createInsurancePlan(InsurancePlanDTO insurancePlanDto) {
		InsurancePlan plan = new InsurancePlan();
		plan.setName(insurancePlanDto.getName());
		plan.setActive(insurancePlanDto.isActive());
		insurancePlanRepository.save(plan);
		return "Insurance Plan created successfully.";
	}

	@Override
	public String createInsuranceScheme(long insurancePlanId, InsuranceSchemeDto insuranceSchemeDto) {

		InsurancePlan insurancePlan = insurancePlanRepository.findById(insurancePlanId)
				.orElseThrow(() -> new LifeGuardException("Insurance Plan with ID: " + insurancePlanId + " not found"));

		InsuranceScheme insuranceScheme = new InsuranceScheme();
		insuranceScheme.setInsuranceScheme(insuranceSchemeDto.getInsuranceScheme());
		insuranceScheme.setDescription(insuranceSchemeDto.getDescription());
		insuranceScheme.setMinimumPolicyTerm(insuranceSchemeDto.getMinimumPolicyTerm());
		insuranceScheme.setMaximumPolicyTerm(insuranceSchemeDto.getMaximumPolicyTerm());
		insuranceScheme.setMinimumAge(insuranceSchemeDto.getMinimumAge());
		insuranceScheme.setMaximumAge(insuranceSchemeDto.getMaximumAge());
		insuranceScheme.setMinimumInvestmentAmount(insuranceSchemeDto.getMinimumInvestmentAmount());
		insuranceScheme.setMaximumInvestmentAmount(insuranceSchemeDto.getMaximumInvestmentAmount());
		insuranceScheme.setProfitRatio(insuranceSchemeDto.getProfitRatio());
		insuranceScheme.setNewRegistrationCommission(insuranceSchemeDto.getNewRegistrationCommission());
		insuranceScheme.setInstallmentPaymentCommission(insuranceSchemeDto.getInstallmentPaymentCommission());
		insuranceScheme.setActive(insuranceSchemeDto.isActive());

		insuranceScheme.setSchemeImage(insuranceSchemeDto.getSchemeImage());

		Set<SchemeDocument> schemeDocuments = insuranceSchemeDto.getSchemeDocument().stream().map(docDto -> {
			SchemeDocument doc = new SchemeDocument();
			doc.setId(docDto.getId());
			doc.setName(docDto.getName());
			return doc;
		}).collect(Collectors.toSet());
		insuranceScheme.setSchemeDocuments(schemeDocuments);

		insuranceScheme.setInsurancePlan(insurancePlan);

		insuranceSchemeRepository.save(insuranceScheme);

		return "Insurance Scheme created successfully!";
	}

	@Override
	public String updateInsuranceScheme(long insuranceSchemeId, InsuranceSchemeDto insuranceSchemeDto) {

		InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(insuranceSchemeId).orElseThrow(
				() -> new LifeGuardException("Insurance Scheme with ID: " + insuranceSchemeId + " not found"));

		insuranceScheme.setInsuranceScheme(insuranceSchemeDto.getInsuranceScheme());
		insuranceScheme.setDescription(insuranceSchemeDto.getDescription());
		insuranceScheme.setMinimumPolicyTerm(insuranceSchemeDto.getMinimumPolicyTerm());
		insuranceScheme.setMaximumPolicyTerm(insuranceSchemeDto.getMaximumPolicyTerm());
		insuranceScheme.setMinimumAge(insuranceSchemeDto.getMinimumAge());
		insuranceScheme.setMaximumAge(insuranceSchemeDto.getMaximumAge());
		insuranceScheme.setMinimumInvestmentAmount(insuranceSchemeDto.getMinimumInvestmentAmount());
		insuranceScheme.setMaximumInvestmentAmount(insuranceSchemeDto.getMaximumInvestmentAmount());
		insuranceScheme.setProfitRatio(insuranceSchemeDto.getProfitRatio());
		insuranceScheme.setNewRegistrationCommission(insuranceSchemeDto.getNewRegistrationCommission());
		insuranceScheme.setInstallmentPaymentCommission(insuranceSchemeDto.getInstallmentPaymentCommission());
		insuranceScheme.setActive(insuranceSchemeDto.isActive());

		insuranceScheme.setSchemeImage(insuranceSchemeDto.getSchemeImage());

		Set<SchemeDocument> schemeDocuments = insuranceSchemeDto.getSchemeDocument().stream().map(docDto -> {
			SchemeDocument doc = new SchemeDocument();
			doc.setId(docDto.getId());
			doc.setName(docDto.getName());
			return doc;
		}).collect(Collectors.toSet());
		insuranceScheme.setSchemeDocuments(schemeDocuments);

		insuranceSchemeRepository.save(insuranceScheme);

		return "Insurance Scheme updated successfully!";
	}

	@Override
	@Transactional
	public String updateInsurancePlan(Long planId, InsurancePlanDTO insurancePlanDto) {
		InsurancePlan plan = insurancePlanRepository.findById(planId).orElseThrow(
				() -> new APIException(HttpStatus.NOT_FOUND, "Insurance Plan not found for ID: " + planId));

		plan.setName(insurancePlanDto.getName());
		plan.setActive(insurancePlanDto.isActive());
		insurancePlanRepository.save(plan);

		return "Insurance Plan updated successfully.";
	}

	@Override
	public PagedResponse<InsurancePlanDTO> getAllPlans(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);
		Page<InsurancePlan> planPage = insurancePlanRepository.findAll(pageable);

		List<InsurancePlanDTO> planResponseList = planPage.getContent().stream().map(plan -> {
			List<InsuranceSchemeDto> schemes = plan.getInsuranceSchemes().stream().map(scheme -> {
				InsuranceSchemeDto schemeDto = new InsuranceSchemeDto();
				schemeDto.setInsuranceSchemeId(scheme.getInsuranceSchemeId());
				schemeDto.setInsuranceScheme(scheme.getInsuranceScheme());
				schemeDto.setDescription(scheme.getDescription());
				schemeDto.setMinimumPolicyTerm(scheme.getMinimumPolicyTerm());
				schemeDto.setMaximumPolicyTerm(scheme.getMaximumPolicyTerm());
				schemeDto.setMinimumAge(scheme.getMinimumAge());
				schemeDto.setMaximumAge(scheme.getMaximumAge());
				schemeDto.setMinimumInvestmentAmount(scheme.getMinimumInvestmentAmount());
				schemeDto.setMaximumInvestmentAmount(scheme.getMaximumInvestmentAmount());
				schemeDto.setProfitRatio(scheme.getProfitRatio());
				schemeDto.setNewRegistrationCommission(scheme.getNewRegistrationCommission());
				schemeDto.setInstallmentPaymentCommission(scheme.getInstallmentPaymentCommission());
				schemeDto.setActive(scheme.isActive());
				schemeDto.setSchemeImage(scheme.getSchemeImage());

				// Map scheme documents
				Set<SchemeDocumentDto> schemeDocuments = scheme.getSchemeDocuments().stream().map(doc -> {
					SchemeDocumentDto docDto = new SchemeDocumentDto();
					docDto.setId(doc.getId());
					docDto.setName(doc.getName());
					return docDto;
				}).collect(Collectors.toSet());

				schemeDto.setSchemeDocument(schemeDocuments); // Set documents to scheme DTO

				return schemeDto;
			}).collect(Collectors.toList());

			// Map plan
			InsurancePlanDTO planDto = new InsurancePlanDTO();
			planDto.setInsurancePlanId(plan.getInsurancePlanId());
			planDto.setName(plan.getName());
			planDto.setActive(plan.isActive());
			planDto.setInsuranceSchemes(schemes);

			return planDto;
		}).collect(Collectors.toList());

		return new PagedResponse<>(planResponseList, planPage.getNumber(), planPage.getSize(),
				planPage.getTotalElements(), planPage.getTotalPages(), planPage.isLast());
	}

	@Override
	public PagedResponse<InsuranceSchemeDto> getAllSchemes(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);
		Page<InsuranceScheme> schemePage = insuranceSchemeRepository.findAll(pageable);

		List<InsuranceSchemeDto> schemeResponseList = schemePage.getContent().stream().map(scheme -> {
			InsuranceSchemeDto schemeDto = new InsuranceSchemeDto();
			schemeDto.setInsuranceSchemeId(scheme.getInsuranceSchemeId());
			schemeDto.setInsuranceScheme(scheme.getInsuranceScheme());
			schemeDto.setDescription(scheme.getDescription());
			schemeDto.setMinimumPolicyTerm(scheme.getMinimumPolicyTerm());
			schemeDto.setMaximumPolicyTerm(scheme.getMaximumPolicyTerm());
			schemeDto.setMinimumAge(scheme.getMinimumAge());
			schemeDto.setMaximumAge(scheme.getMaximumAge());
			schemeDto.setMinimumInvestmentAmount(scheme.getMinimumInvestmentAmount());
			schemeDto.setMaximumInvestmentAmount(scheme.getMaximumInvestmentAmount());
			schemeDto.setProfitRatio(scheme.getProfitRatio());
			schemeDto.setNewRegistrationCommission(scheme.getNewRegistrationCommission());
			schemeDto.setInstallmentPaymentCommission(scheme.getInstallmentPaymentCommission());
			schemeDto.setActive(scheme.isActive());
			schemeDto.setSchemeImage(scheme.getSchemeImage());
			schemeDto.setInsurancePlanId(scheme.getInsurancePlan().getInsurancePlanId());

			// Set scheme documents (converting entity to DTO)
			Set<SchemeDocumentDto> schemeDocumentsDto = scheme.getSchemeDocuments().stream().map(doc -> {
				SchemeDocumentDto docDto = new SchemeDocumentDto();
				docDto.setId(doc.getId());
				docDto.setName(doc.getName());
				return docDto;
			}).collect(Collectors.toSet());
			schemeDto.setSchemeDocument(schemeDocumentsDto);

			return schemeDto;
		}).collect(Collectors.toList());

		return new PagedResponse<>(schemeResponseList, schemePage.getNumber(), schemePage.getSize(),
				schemePage.getTotalElements(), schemePage.getTotalPages(), schemePage.isLast());
	}

	@Override
	public PagedResponse<InsuranceSchemeDto> getAllSchemesByPlanId(Long planId, int page, int size, String sortBy,
			String direction) {
		InsurancePlan plan = insurancePlanRepository.findById(planId)
				.orElseThrow(() -> new LifeGuardException("Insurance Plan with ID: " + planId + " not found"));

		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);
		Page<InsuranceScheme> schemePage = insuranceSchemeRepository.findAllByInsurancePlan(plan, pageable);

		List<InsuranceSchemeDto> schemeResponseList = schemePage.getContent().stream().map(scheme -> {
			InsuranceSchemeDto schemeDto = new InsuranceSchemeDto();
			schemeDto.setInsuranceSchemeId(scheme.getInsuranceSchemeId());
			schemeDto.setInsuranceScheme(scheme.getInsuranceScheme());
			schemeDto.setDescription(scheme.getDescription());
			schemeDto.setMinimumPolicyTerm(scheme.getMinimumPolicyTerm());
			schemeDto.setMaximumPolicyTerm(scheme.getMaximumPolicyTerm());
			schemeDto.setMinimumAge(scheme.getMinimumAge());
			schemeDto.setMaximumAge(scheme.getMaximumAge());
			schemeDto.setMinimumInvestmentAmount(scheme.getMinimumInvestmentAmount());
			schemeDto.setMaximumInvestmentAmount(scheme.getMaximumInvestmentAmount());
			schemeDto.setProfitRatio(scheme.getProfitRatio());
			schemeDto.setNewRegistrationCommission(scheme.getNewRegistrationCommission());
			schemeDto.setInstallmentPaymentCommission(scheme.getInstallmentPaymentCommission());
			schemeDto.setActive(scheme.isActive());
			schemeDto.setSchemeImage(scheme.getSchemeImage());
			schemeDto.setInsurancePlanId(scheme.getInsurancePlan().getInsurancePlanId());

			// Set scheme documents (converting entity to DTO)
			Set<SchemeDocumentDto> schemeDocumentsDto = scheme.getSchemeDocuments().stream().map(doc -> {
				SchemeDocumentDto docDto = new SchemeDocumentDto();
				docDto.setId(doc.getId());
				docDto.setName(doc.getName());
				return docDto;
			}).collect(Collectors.toSet());
			schemeDto.setSchemeDocument(schemeDocumentsDto);

			return schemeDto;
		}).collect(Collectors.toList());

		return new PagedResponse<>(schemeResponseList, schemePage.getNumber(), schemePage.getSize(),
				schemePage.getTotalElements(), schemePage.getTotalPages(), schemePage.isLast());
	}

	@Override
	public PagedResponse<InsuranceSchemeDto> getSchemeById(Long schemeId, int page, int size, String sortBy,
			String direction) {
		InsuranceScheme scheme = insuranceSchemeRepository.findById(schemeId)
				.orElseThrow(() -> new LifeGuardException("Insurance Scheme with ID: " + schemeId + " not found"));

		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);

		// For the sake of pagination, we create a single-item page
		List<InsuranceScheme> singleSchemeList = Collections.singletonList(scheme);
		Page<InsuranceScheme> schemePage = new PageImpl<>(singleSchemeList, pageable, 1);

		List<InsuranceSchemeDto> schemeResponseList = schemePage.getContent().stream().map(s -> {
			InsuranceSchemeDto schemeDto = new InsuranceSchemeDto();
			schemeDto.setInsuranceSchemeId(s.getInsuranceSchemeId());
			schemeDto.setInsuranceScheme(s.getInsuranceScheme());
			schemeDto.setDescription(s.getDescription());
			schemeDto.setMinimumPolicyTerm(s.getMinimumPolicyTerm());
			schemeDto.setMaximumPolicyTerm(s.getMaximumPolicyTerm());
			schemeDto.setMinimumAge(s.getMinimumAge());
			schemeDto.setMaximumAge(s.getMaximumAge());
			schemeDto.setMinimumInvestmentAmount(s.getMinimumInvestmentAmount());
			schemeDto.setMaximumInvestmentAmount(s.getMaximumInvestmentAmount());
			schemeDto.setProfitRatio(s.getProfitRatio());
			schemeDto.setNewRegistrationCommission(s.getNewRegistrationCommission());
			schemeDto.setInstallmentPaymentCommission(s.getInstallmentPaymentCommission());
			schemeDto.setActive(s.isActive());
			schemeDto.setSchemeImage(s.getSchemeImage());
			schemeDto.setInsurancePlanId(s.getInsurancePlan().getInsurancePlanId());

			// Set scheme documents (converting entity to DTO)
			Set<SchemeDocumentDto> schemeDocumentsDto = s.getSchemeDocuments().stream().map(doc -> {
				SchemeDocumentDto docDto = new SchemeDocumentDto();
				docDto.setId(doc.getId());
				docDto.setName(doc.getName());
				return docDto;
			}).collect(Collectors.toSet());
			schemeDto.setSchemeDocument(schemeDocumentsDto);

			return schemeDto;
		}).collect(Collectors.toList());

		return new PagedResponse<>(schemeResponseList, schemePage.getNumber(), schemePage.getSize(),
				schemePage.getTotalElements(), schemePage.getTotalPages(), schemePage.isLast());
	}

	@Override
	public PagedResponse<AgentRequestDto> getAllAgents(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);
		Page<Agent> agentPage = agentRepository.findAll(pageable);

		List<AgentRequestDto> agents = agentPage.getContent().stream().map(agent -> {
			AgentRequestDto dto = new AgentRequestDto();
			dto.setAgentId(agent.getAgentId());
			dto.setFirstName(agent.getFirstName());
			dto.setLastName(agent.getLastName());
			dto.setEmail(agent.getUser().getEmail());
			dto.setPhoneNumber(agent.getPhoneNumber());
			dto.setUsername(agent.getUser().getUsername());
			dto.setCity_id(agent.getCity().getId());
			dto.setState_id(agent.getCity().getState().getStateId());
			dto.setVerified(agent.isVerified());
			return dto;
		}).collect(Collectors.toList());

		return new PagedResponse<>(agents, agentPage.getNumber(), agentPage.getSize(), agentPage.getTotalElements(),
				agentPage.getTotalPages(), agentPage.isLast());
	}

	@Override
	public PagedResponse<EmployeeRequestDto> getAllEmployees(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);
		Page<Employee> employeePage = employeeRepository.findAll(pageable);

		List<EmployeeRequestDto> employees = employeePage.getContent().stream().map(employee -> {
			EmployeeRequestDto dto = new EmployeeRequestDto();
			dto.setEmployeeId(employee.getEmployeeId());
			dto.setFirstName(employee.getFirstName());
			dto.setLastName(employee.getLastName());
			dto.setEmail(employee.getUser().getEmail());
			dto.setUsername(employee.getUser().getUsername());
			return dto;
		}).collect(Collectors.toList());

		return new PagedResponse<>(employees, employeePage.getNumber(), employeePage.getSize(),
				employeePage.getTotalElements(), employeePage.getTotalPages(), employeePage.isLast());
	}

	@Override
	public PagedResponse<CustomerDto> getAllCustomers(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);
		Page<Customer> customerPage = customerRepository.findAll(pageable);

		List<CustomerDto> customers = customerPage.getContent().stream().map(customer -> {
			CustomerDto dto = new CustomerDto();
			dto.setCustomerId(customer.getCustomerId());
			dto.setFirstName(customer.getFirstName());
			dto.setLastName(customer.getLastName());
			dto.setPhoneNumber(customer.getPhoneNumber());
			dto.setDob(customer.getDob()); // Ensure DOB is mapped
			// dto.setPanCard(customer.getPanCard()); // Map PAN card
			// dto.setAadhaarCard(customer.getAadhaarCard()); // Map Aadhaar card
			dto.setCity(customer.getCity().getCity_name());
			dto.setActive(customer.isActive()); // Correctly set boolean values
			// dto.setVerified(customer.isVerified()); // Correctly set boolean values
			return dto;
		}).collect(Collectors.toList());

		return new PagedResponse<>(customers, customerPage.getNumber(), customerPage.getSize(),
				customerPage.getTotalElements(), customerPage.getTotalPages(), customerPage.isLast());
	}

	@Override
	public PagedResponse<PaymentDto> getAllPayments(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);
		Page<Payment> paymentPage = paymentRepository.findAll(pageable);

		List<PaymentDto> payments = paymentPage.getContent().stream().map(payment -> {
			PaymentDto dto = new PaymentDto();
			dto.setId(payment.getId());
			if (payment.getPolicy() != null) {
				dto.setPolicyId(payment.getPolicy().getInsuranceId());
			}
			dto.setPaymentType(payment.getPaymentType());
			dto.setAmount(payment.getAmount());
			dto.setPaymentDate(payment.getPaymentDate());
			dto.setTax(payment.getTax());
			dto.setTotalPayment(payment.getTotalPayment());
			return dto;
		}).collect(Collectors.toList());

		return new PagedResponse<>(payments, paymentPage.getNumber(), paymentPage.getSize(),
				paymentPage.getTotalElements(), paymentPage.getTotalPages(), paymentPage.isLast());
	}

	@Override
	public PagedResponse<CommissionDto> getAllCommissions(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);
		Page<Commission> commissionPage = commissionRepository.findAll(pageable);

		List<CommissionDto> commissions = commissionPage.getContent().stream().map(commission -> {
			CommissionDto dto = new CommissionDto();
			dto.setCommissionId(commission.getCommissionId());
			dto.setAgentId(commission.getAgent().getAgentId());
			if (commission.getInsurancePolicy() != null) {
				dto.setPolicyId(commission.getInsurancePolicy().getInsuranceId());
			}
			dto.setAmount(commission.getAmount());
			dto.setDate(commission.getDate());
			dto.setCommissionType(commission.getCommissionType());
			return dto;
		}).collect(Collectors.toList());

		return new PagedResponse<>(commissions, commissionPage.getNumber(), commissionPage.getSize(),
				commissionPage.getTotalElements(), commissionPage.getTotalPages(), commissionPage.isLast());
	}

	@Override
	public PaymentTax getPaymentTax() {
		return paymentTaxRepository.findFirstByOrderByIdAsc().orElse(new PaymentTax());
	}

	// Set or update the Payment Tax
	@Override
	public void setPaymentTax(Long paymentTaxValue) {
		Optional<PaymentTax> existingTax = paymentTaxRepository.findFirstByOrderByIdAsc();

		if (existingTax.isPresent()) {
			// Update the existing tax
			PaymentTax paymentTax = existingTax.get();
			paymentTax.setPaymentTax(paymentTaxValue);
			paymentTaxRepository.save(paymentTax);
		} else {
			// Create a new Payment Tax entry
			PaymentTax newPaymentTax = new PaymentTax();
			newPaymentTax.setPaymentTax(paymentTaxValue);
			paymentTaxRepository.save(newPaymentTax);
		}
	}

	@Override
	public double getInstallmentAmountByPolicyId(long policyId) {
		InsurancePolicy policy = insurancePolicyRepository.findById(policyId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Policy not found with ID: " + policyId));

		int totalInstallments = (policy.getPolicyTerm() * 12) / policy.getInstallmentPeriod();
		return policy.getPremiumAmount() / totalInstallments; // Calculate the installment amount
	}

	@Override
	@Transactional
	public String deactivateAgent(Long agentId) {
		Agent agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Agent not found with id: " + agentId));
		if (!agent.isActive()) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Agent is already deactivated.");
		}
		agent.setActive(false);
		agentRepository.save(agent);
		return "Agent deactivated successfully.";
	}

	
	@Override
	@Transactional
	public String activateAgent(Long agentId) {
		Agent agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Agent not found with id: " + agentId));
		if (agent.isActive()) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Agent is already active.");
		}
		agent.setActive(true);
		agentRepository.save(agent);
		return "Agent activated successfully.";
	}

	// Employee Deactivation
	@Override
	@Transactional
	public String deactivateEmployee(Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Employee not found with id: " + employeeId));
		if (!employee.isActive()) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Employee is already deactivated.");
		}
		employee.setActive(false);
		employeeRepository.save(employee);
		return "Employee deactivated successfully.";
	}

	
	@Override
	@Transactional
	public String activateEmployee(Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Employee not found with id: " + employeeId));
		if (employee.isActive()) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Employee is already active.");
		}
		employee.setActive(true);
		employeeRepository.save(employee);
		return "Employee activated successfully.";
	}

	
	@Override
	@Transactional
	public String deactivateCustomer(Long customerId) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Customer not found with id: " + customerId));
		if (!customer.isActive()) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Customer is already deactivated.");
		}
		customer.setActive(false);
		customerRepository.save(customer);
		return "Customer deactivated successfully.";
	}

	
	@Override
	@Transactional
	public String activateCustomer(Long customerId) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Customer not found with id: " + customerId));
		if (customer.isActive()) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Customer is already active.");
		}
		customer.setActive(true);
		customerRepository.save(customer);
		return "Customer activated successfully.";
	}

	@Override
	public PagedResponse<CustomerDto> getCustomersByFirstName(String firstName, int page, int size, String sortBy, String direction) {
	    Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
	    PageRequest pageable = PageRequest.of(page, size, sort);
	    Page<Customer> customerPage = customerRepository.findByFirstNameContainingIgnoreCase(firstName, pageable);

	    List<CustomerDto> customers = customerPage.getContent().stream().map(this::convertCustomerToDto).collect(Collectors.toList());

	    return new PagedResponse<>(customers, customerPage.getNumber(), customerPage.getSize(), customerPage.getTotalElements(), customerPage.getTotalPages(), customerPage.isLast());
	}

	@Override
	public PagedResponse<CustomerDto> getCustomersByLastName(String lastName, int page, int size, String sortBy, String direction) {
	    Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
	    PageRequest pageable = PageRequest.of(page, size, sort);
	    Page<Customer> customerPage = customerRepository.findByLastNameContainingIgnoreCase(lastName, pageable);

	    List<CustomerDto> customers = customerPage.getContent().stream().map(this::convertCustomerToDto).collect(Collectors.toList());

	    return new PagedResponse<>(customers, customerPage.getNumber(), customerPage.getSize(), customerPage.getTotalElements(), customerPage.getTotalPages(), customerPage.isLast());
	}

	@Override
	public PagedResponse<CustomerDto> getCustomersByIsActive(boolean isActive, int page, int size, String sortBy, String direction) {
	    Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();
	    PageRequest pageable = PageRequest.of(page, size, sort);
	    
	    Page<Customer> customerPage = customerRepository.findByIsActive(isActive, pageable);

	    List<CustomerDto> customers = customerPage.getContent().stream().map(customer -> {
	        CustomerDto dto = new CustomerDto();
	        dto.setCustomerId(customer.getCustomerId());
	        dto.setFirstName(customer.getFirstName());
	        dto.setLastName(customer.getLastName());
	        dto.setPhoneNumber(customer.getPhoneNumber());
	        dto.setDob(customer.getDob());
	        dto.setCity(customer.getCity().getCity_name());
	        dto.setActive(customer.isActive());
	        dto.setVerified(customer.isVerified());
	        return dto;
	    }).collect(Collectors.toList());

	    return new PagedResponse<>(customers, customerPage.getNumber(), customerPage.getSize(),
	            customerPage.getTotalElements(), customerPage.getTotalPages(), customerPage.isLast());
	}

	private CustomerDto convertCustomerToDto(Customer customer) {
	    CustomerDto dto = new CustomerDto();
	    dto.setCustomerId(customer.getCustomerId());
	    dto.setFirstName(customer.getFirstName());
	    dto.setLastName(customer.getLastName());
	    dto.setPhoneNumber(customer.getPhoneNumber());
	    dto.setDob(customer.getDob());
	    dto.setCity(customer.getCity().getCity_name());
	    dto.setActive(customer.isActive());
	    dto.setVerified(customer.isVerified());
	    return dto;
	}


}
