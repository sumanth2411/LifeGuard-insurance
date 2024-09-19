package com.techlabs.app.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.CustomerDto;
import com.techlabs.app.dto.CustomerRegistrationDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.JWTAuthResponse;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.ClaimStatus;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Employee;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.LifeGuardException;
import com.techlabs.app.exception.ResourceNotFoundException;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.ClaimRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.EmployeeRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.util.PagedResponse;

import jakarta.transaction.Transactional;

@Service
public class AgentServiceImpl implements AgentService {

	@Autowired
	private AgentRepository agentRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;

	@Autowired
	private ClaimRepository claimRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;
	private final JavaMailSender mailSender;

	public AgentServiceImpl(AgentRepository agentRepository, UserRepository userRepository,
			InsurancePolicyRepository insurancePolicyRepository, ClaimRepository claimRepository,
			CustomerRepository customerRepository, CityRepository cityRepository, PasswordEncoder passwordEncoder,
			RoleRepository roleRepository, JavaMailSender mailSender) {
		super();
		this.agentRepository = agentRepository;
		this.userRepository = userRepository;
		this.insurancePolicyRepository = insurancePolicyRepository;
		this.claimRepository = claimRepository;
		this.customerRepository = customerRepository;
		this.cityRepository = cityRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.mailSender = mailSender;
	}

	private String getEmailFromSecurityContext() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			return userDetails.getUsername();
		}
		return null;
	}

	@Override
	public String agentclaimPolicy(ClaimRequestDto claimRequestDto, Long agentId) {
		InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(claimRequestDto.getPolicyId())
				.orElseThrow(() -> new RuntimeException("Policy not found"));

		// Agent's Commission Claim
		Agent agent = agentRepository.findById(agentId).orElseThrow(() -> new LifeGuardException("Agent not found"));

		Claim agentClaim = new Claim();
		agentClaim.setClaimAmount(claimRequestDto.getClaimAmount());
		agentClaim.setBankName(claimRequestDto.getBankName());
		agentClaim.setBranchName(claimRequestDto.getBranchName());
		agentClaim.setBankAccountId(claimRequestDto.getBankAccountId());
		agentClaim.setIfscCode(claimRequestDto.getIfscCode());
		agentClaim.setClaimedStatus(ClaimStatus.PENDING.name());
		agentClaim.setPolicy(insurancePolicy);
		agentClaim.setAgent(agent);

		claimRepository.save(agentClaim);

		return "Claim of " + claimRequestDto.getClaimAmount() + " has been successfully created for policy ID "
				+ claimRequestDto.getPolicyId() + ". The claim is pending approval.";
	}

	@Override

	public List<CustomerDto> getCustomersByCity(long agentId) {
		Agent agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Agent not found with ID: " + agentId));

		List<Customer> customers = customerRepository.findByCity(agent.getCity());
		return customers.stream().map(this::convertToCustomerDto).collect(Collectors.toList());
	}

	private CustomerDto convertToCustomerDto(Customer customer) {
		CustomerDto dto = new CustomerDto();
		dto.setCustomerId(customer.getCustomerId());
		dto.setFirstName(customer.getFirstName());
		dto.setLastName(customer.getLastName());
		dto.setDob(customer.getDob());
		dto.setPhoneNumber(customer.getPhoneNumber());
		dto.setCity(customer.getCity().getCity_name());
		dto.setEmail(customer.getUser().getEmail());
		dto.setVerified(customer.isVerified());
		dto.setActive(customer.isActive());

		if (customer.getInsurancePolicies() != null) {
			List<InsurancePolicyDto> policyDtos = customer.getInsurancePolicies().stream().map(policy -> {
				InsurancePolicyDto policyDto = new InsurancePolicyDto();
				policyDto.setInsuranceId(policy.getInsuranceId());
				// policyDto.setInsuranceSchemeName(policy.getInsuranceScheme().getSchemeName());
				policyDto.setPolicyTerm(policy.getPolicyTerm());
				policyDto.setPremiumAmount(policy.getPremiumAmount());
				policyDto.setMaturityDate(policy.getMaturityDate());
				// Set other fields as needed
				return policyDto;
			}).collect(Collectors.toList());
			dto.setInsurancePolicies(policyDtos);
		}

		return dto;
	}

	@Override
	@Transactional
	public JWTAuthResponse registerOrFetchCustomer(CustomerRegistrationDto customerRegistrationDto) {

		Long agentId = getLoggedInAgentId();
		Agent agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "Agent not found"));

		City agentCity = agent.getCity();

		Optional<User> existingUserOpt = userRepository.findByUsername(customerRegistrationDto.getUsername());

		if (existingUserOpt.isPresent()) {

			User existingUser = existingUserOpt.get();
			if (passwordEncoder.matches(customerRegistrationDto.getPassword(), existingUser.getPassword())) {
				Customer existingCustomer = customerRepository.findByUser(existingUser).orElseThrow(
						() -> new APIException(HttpStatus.NOT_FOUND, "Customer associated with the user not found"));

				// Check if the customer's city matches the agent's city
				if (!existingCustomer.getCity().getId().equals(agentCity.getId())) {
					return new JWTAuthResponse(null, "Bearer", "ROLE_AGENT", null, agentId,
							"Cannot buy policy as the customer belongs to a different city.");
				}

				return new JWTAuthResponse(null, "Bearer", "ROLE_AGENT", existingCustomer.getCustomerId(), agentId,
						"Customer is already registered.");
			} else {
				throw new APIException(HttpStatus.UNAUTHORIZED, "Invalid password for existing customer");
			}
		}

		if (userRepository.existsByUsername(customerRegistrationDto.getUsername())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
		}

		if (userRepository.existsByEmail(customerRegistrationDto.getEmail())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
		}

		User user = new User();
		user.setUsername(customerRegistrationDto.getUsername());
		user.setEmail(customerRegistrationDto.getEmail());
		user.setPassword(passwordEncoder.encode(customerRegistrationDto.getPassword()));

		Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: ROLE_CUSTOMER"));
		user.setRoles(Set.of(customerRole));
		userRepository.save(user);

		Customer customer = new Customer();
		customer.setUser(user);
		customer.setFirstName(customerRegistrationDto.getFirstName());
		customer.setLastName(customerRegistrationDto.getLastName());
		customer.setDob(customerRegistrationDto.getDob());
		customer.setPhoneNumber(customerRegistrationDto.getPhoneNumber());
		customer.setCity(agentCity); // Set the city to the agent's city
		customer.setActive(customerRegistrationDto.getIsActive());
		customer.setVerified(true); // Set verification status as true since the agent added this customer

		customerRepository.save(customer);

		return new JWTAuthResponse(null, "Bearer", "ROLE_AGENT", customer.getCustomerId(), agentId,
				"Customer registered successfully!");
	}

	private Long getLoggedInAgentId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new APIException(HttpStatus.UNAUTHORIZED, "User not authenticated");
		}

		String usernameOrEmail = authentication.getName();

		User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED,
						"User not found with username or email: " + usernameOrEmail));

		Agent agent = agentRepository.findByUser(user)
				.orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "Agent not found for the user"));

		return agent.getAgentId();
	}

	@Override
	public String updateAgent(AgentRequestDto agentRequestDto) {
		System.out.println("In update profile");

		// Retrieve the User object using the email from the security context
		User user = userRepository.findByEmail(getEmailFromSecurityContext())
				.orElseThrow(() -> new ResourceNotFoundException("No Agent found with this email"));

		// Retrieve the Agent object using the User object
		Optional<Agent> agentOptional = agentRepository.findByUser(user);
		if (!agentOptional.isPresent()) {
			throw new ResourceNotFoundException("Agent not found");
		}

		// Get the Agent object from the Optional
		Agent agent = agentOptional.get();

		// Update fields in the Agent object
		if (agentRequestDto.getFirstName() != null) {
			agent.setFirstName(agentRequestDto.getFirstName());
		}
		if (agentRequestDto.getLastName() != null) {
			agent.setLastName(agentRequestDto.getLastName());
		}
		if (agentRequestDto.getPhoneNumber() != null) {
			agent.setPhoneNumber(agentRequestDto.getPhoneNumber());
		}
		if (agentRequestDto.getVerified() != null) {
			agent.setVerified(agentRequestDto.getVerified());
		}

		// Save the updated Agent entity
		agentRepository.save(agent);

		return "Agent Updated Successfully";
	}

	@Override
	public void changeAgentPassword(String email, String oldPassword, String newPassword, String confirmNewPassword) {
		// Check for null values
		if (email == null || oldPassword == null || newPassword == null || confirmNewPassword == null) {
			throw new IllegalArgumentException(
					"Email, old password, new password, and confirm password must not be null.");
		}

		// Retrieve the agent by email
		Agent agent = agentRepository.findByUser_Email(email)
				.orElseThrow(() -> new UsernameNotFoundException("Agent not found with email: " + email));

		// Check if the old password matches
		if (!passwordEncoder.matches(oldPassword, agent.getUser().getPassword())) {
			throw new IllegalArgumentException("Old password is incorrect.");
		}

		// Check if new password and confirm password match
		if (!newPassword.equals(confirmNewPassword)) {
			throw new IllegalArgumentException("New password and confirm password do not match.");
		}

		// Update the password
		agent.getUser().setPassword(passwordEncoder.encode(newPassword));
		agentRepository.save(agent);
	}

	@Override
	public void sendPolicyEmail(String recipientEmail, String registrationLink) {
		// Check if recipientEmail or registrationLink is null
		if (recipientEmail == null || recipientEmail.isEmpty()) {
			throw new IllegalArgumentException("Recipient email must not be null or empty.");
		}
		if (registrationLink == null || registrationLink.isEmpty()) {
			throw new IllegalArgumentException("Registration link must not be null or empty.");
		}

		// Create a simple mail message
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(recipientEmail);
		message.setSubject("Exclusive Offer: Buy Policy Now!");

		// Email body with registration link
		String emailBody = "Dear Customer,\n\nWe are excited to offer you a special opportunity to purchase one of our best insurance policies."
				+ "\nClick the link below to register and explore our policies:\n\n" + registrationLink
				+ "\n\nThank you for choosing LifeGuard Insurance.\n\nBest regards,\nLifeGuard-Insurance Team";

		message.setText(emailBody);

		// Send the email
		mailSender.send(message);
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
			dto.setCity(customer.getCity().getCity_name());
			return dto;
		}).collect(Collectors.toList());

		return new PagedResponse<>(customers, customerPage.getNumber(), customerPage.getSize(),
				customerPage.getTotalElements(), customerPage.getTotalPages(), customerPage.isLast());
	}

}
