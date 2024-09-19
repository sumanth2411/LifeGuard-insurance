package com.techlabs.app.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.CustomerDto;
import com.techlabs.app.dto.CustomerQueryRequestDto;
import com.techlabs.app.dto.CustomerQueryResponseDto;
import com.techlabs.app.dto.DocumentResponseDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.CustomerQuery;
import com.techlabs.app.entity.Document;
import com.techlabs.app.entity.DocumentStatus;
import com.techlabs.app.entity.DocumentType;
import com.techlabs.app.entity.Employee;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.SchemeDocument;
import com.techlabs.app.entity.SubmittedDocument;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.LifeGuardException;
import com.techlabs.app.exception.LifeGuardOtpException;
import com.techlabs.app.exception.ResourceNotFoundException;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.CustomerQueryRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.DocumentRepository;
import com.techlabs.app.repository.DocumentRepository1;
import com.techlabs.app.repository.EmployeeRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.util.PagedResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private AgentRepository agentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private JavaMailSender mailSender; // For sending emails

	@Autowired
	private CustomerQueryRepository customerQueryRepository;
	
	@Autowired
	private DocumentRepository1 documentRepository1;

	@Override
	@Transactional
	public String registerAgent(AgentRequestDto agentRequestDto) {
		// Validate that the password is not null
		if (agentRequestDto.getPassword() == null || agentRequestDto.getPassword().isEmpty()) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Password must not be null or empty");
		}

		// Create and save User
		User user = new User();
		user.setUsername(agentRequestDto.getUsername());
		user.setEmail(agentRequestDto.getEmail());
		user.setPassword(passwordEncoder.encode(agentRequestDto.getPassword()));
		// userRepository.save(user);

		Role agentRole = roleRepository.findByName("ROLE_AGENT")
				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: ROLE_AGENT"));
		Set<Role> roles = new HashSet<>();
		roles.add(agentRole);
		user.setRoles(roles);
		User savedUser = userRepository.save(user);
		// Find and set City
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
		agent.setVerified(false); 
		agentRepository.save(agent);

		return "Agent registered successfully";
	}

	@Override
	public String verifyCustomerById(Long customerId) {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Customer with ID not found"));

		// String panCard = customer.getPanCard();
		// String aadhaarCard = customer.getAadhaarCard();

//        
//        if (panCard == null || panCard.isEmpty()) {
//            throw new APIException(HttpStatus.BAD_REQUEST, "Customer cannot be verified without a PAN card");
//        }
//        if (aadhaarCard == null || aadhaarCard.isEmpty()) {
//            throw new APIException(HttpStatus.BAD_REQUEST, "Customer cannot be verified without an Aadhaar card");
//        }
//
//        
//        if (customer.isVerified()) {
//            throw new APIException(HttpStatus.BAD_REQUEST, "Customer is already verified");
//        }
//
//        
//        customer.setVerified(true);
//        customerRepository.save(customer);

		// return "Customer with PAN card " + panCard + " and Aadhaar card " +
		// aadhaarCard + " verified successfully";
		return null;
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
	@Transactional
	public AgentResponseDto updateAgent(long agentId, AgentRequestDto updatedAgentData) {
		
		Agent agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Agent not found with id: " + agentId));

		
		User user = agent.getUser();

		
		if (!user.getEmail().equals(updatedAgentData.getEmail())) {
			boolean emailExists = userRepository.existsByEmail(updatedAgentData.getEmail());
			if (emailExists) {
				throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
			}
		}

		
		agent.setFirstName(updatedAgentData.getFirstName());
		agent.setLastName(updatedAgentData.getLastName());
		agent.setPhoneNumber(updatedAgentData.getPhoneNumber());
		agent.setActive(updatedAgentData.isActive());

		
		user.setEmail(updatedAgentData.getEmail());

		

		
		agentRepository.save(agent);
		userRepository.save(user);

		
		AgentResponseDto responseDto = new AgentResponseDto();
		responseDto.setAgentId(agent.getAgentId());
		responseDto.setFirstName(agent.getFirstName());
		responseDto.setLastName(agent.getLastName());
		responseDto.setPhoneNumber(agent.getPhoneNumber());
		responseDto.setActive(agent.isActive());
		responseDto.setEmail(user.getEmail());
		responseDto.setCityName(agent.getCity().getCity_name());
		responseDto.setVerified(agent.isVerified());


		double totalCommission = agent.getCommissions().stream().mapToDouble(commission -> commission.getAmount())
				.sum();
		responseDto.setTotalCommission(totalCommission);

		return responseDto;
	}

	@Transactional
	@Override
	public void verifyPolicyDocuments(long policyId) {
	
		InsurancePolicy policy = insurancePolicyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Policy with ID: " + policyId + " not found"));

		
		Set<SchemeDocument> schemeDocuments = policy.getInsuranceScheme().getSchemeDocuments();

		
		Set<SubmittedDocument> submittedDocuments = policy.getDocuments();

		boolean allDocumentsApproved = true;

	
		
		for (SchemeDocument schemeDoc : schemeDocuments) {
			boolean documentFoundAndApproved = false;

			for (SubmittedDocument submittedDoc : submittedDocuments) {
				
				System.out.println("Checking document: " + submittedDoc.getDocumentName() + ", Status: "
						+ submittedDoc.getDocumentStatus());

				
				if (schemeDoc.getName().equalsIgnoreCase(submittedDoc.getDocumentName())) {
					if (submittedDoc.getDocumentStatus().equals(DocumentStatus.PENDING.name())) {
						
						submittedDoc.setDocumentStatus(DocumentStatus.APPROVED.name());
						documentRepository.save(submittedDoc); 
						System.out.println("Document " + submittedDoc.getDocumentName() + " has been approved.");
					}
					documentFoundAndApproved = true;
					break; 
				}
			}

			
			if (!documentFoundAndApproved) {
				allDocumentsApproved = false;
				System.out.println("Document for " + schemeDoc.getName() + " is missing or not approved.");
				break;
			}
		}


		if (allDocumentsApproved) {
			policy.setVerified(true);
			insurancePolicyRepository.save(policy);
			System.out.println("Policy verified successfully.");
		} else {
			throw new LifeGuardException("Some documents are still pending or missing. Policy cannot be verified.");
		}

	}

	@Override
	@Transactional
	public void verifyCustomer(long customerId) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		customer.setVerified(true);
		customerRepository.save(customer);

		sendVerificationEmail(customer);
	}

	private void sendVerificationEmail(Customer customer) {
		

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(customer.getUser().getEmail()); 
		message.setSubject("Your account has been verified");
		message.setText("Dear " + customer.getFirstName() + ",\n\n" + "Your account has been successfully verified.\n\n"
				+ "Best regards,\n" + "The Team");

		mailSender.send(message);
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
	@Transactional
	public AgentResponseDto getAgentById(long agentId) {
		// Fetch agent by ID
		Agent agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Agent not found with id: " + agentId));

		// Map agent entity to AgentResponseDto
		AgentResponseDto agentResponseDto = new AgentResponseDto();
		agentResponseDto.setAgentId(agent.getAgentId());
		agentResponseDto.setFirstName(agent.getFirstName());
		agentResponseDto.setLastName(agent.getLastName());
		agentResponseDto.setPhoneNumber(agent.getPhoneNumber());
		agentResponseDto.setActive(agent.isActive());
		agentResponseDto.setVerified(agent.isVerified());

		
		User user = agent.getUser();
		City city = agent.getCity();

		if (user != null) {
			agentResponseDto.setEmail(user.getEmail());
		}

		if (city != null) {
			agentResponseDto.setCityName(city.getCity_name());
		}


		double totalCommission = agent.getCommissions().stream().mapToDouble(commission -> commission.getAmount()) // Assuming
																													// 'getAmount'
																													// returns
																													// the
																													// commission
																													// value
				.sum();
		agentResponseDto.setTotalCommission(totalCommission);
	

		return agentResponseDto;
	}

	@Override
	public void changeEmployeePassword(String email, String oldPassword, String newPassword,
			String confirmNewPassword) {
		// Check for null values
		if (email == null || oldPassword == null || newPassword == null || confirmNewPassword == null) {
			throw new IllegalArgumentException(
					"Email, old password, new password, and confirm password must not be null.");
		}

		
		Employee employee = employeeRepository.findByUser_Email(email)
				.orElseThrow(() -> new UsernameNotFoundException("Employee not found with email: " + email));

		
		if (!passwordEncoder.matches(oldPassword, employee.getUser().getPassword())) {
			throw new IllegalArgumentException("Old password is incorrect.");
		}


		if (!newPassword.equals(confirmNewPassword)) {
			throw new IllegalArgumentException("New password and confirm password do not match.");
		}

		
		employee.getUser().setPassword(passwordEncoder.encode(newPassword));
		employeeRepository.save(employee);
	}

	@Override
	public CustomerDto getCustomerIdById(long customerId) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new LifeGuardOtpException.UserNotFoundException(
						"Sorry, we couldn't find a customer with ID: " + customerId));

		return convertCustomerToCustomerResponseDto(customer);
	}

	private CustomerDto convertCustomerToCustomerResponseDto(Customer customer) {
		CustomerDto customerResponseDto = new CustomerDto();
		customerResponseDto.setCustomerId(customer.getCustomerId());
		customerResponseDto.setFirstName(customer.getFirstName());
		customerResponseDto.setLastName(customer.getLastName());
		
		customerResponseDto.setActive(customer.isActive());
	
		customerResponseDto.setPhoneNumber(customer.getPhoneNumber());

		customerResponseDto.setCity(customer.getCity().getCity_name());

		return customerResponseDto;
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
			dto.setVerified(customer.isVerified());
			dto.setCity(customer.getCity().getCity_name());
			dto.setEmail(customer.getUser().getEmail()); 

			return dto;
		}).collect(Collectors.toList());

		return new PagedResponse<>(customers, customerPage.getNumber(), customerPage.getSize(),
				customerPage.getTotalElements(), customerPage.getTotalPages(), customerPage.isLast());
	}

	@Override
	public Page<CustomerQueryResponseDto> getAllCustomerQueries(Pageable pageable) {
		return customerQueryRepository.findAll(pageable).map(this::convertToResponseDto);
	}

	@Override
	public String replyToCustomerQuery(Long queryId, String response) {
		CustomerQuery customerQuery = customerQueryRepository.findById(queryId)
				.orElseThrow(() -> new RuntimeException("Query not found"));

		String extractedResponse = response.trim().replaceAll("[{}]", "");

		// Set the response and change the status to "RESOLVED"
		customerQuery.setResponse(extractedResponse);
		customerQuery.setStatus("RESOLVED");

		customerQueryRepository.save(customerQuery);

		return "Query responded and resolved successfully";
	}

	private CustomerQueryResponseDto convertToResponseDto(CustomerQuery customerQuery) {
		CustomerQueryResponseDto dto = new CustomerQueryResponseDto();
		dto.setQueryId(customerQuery.getQueryId());
		dto.setCustomerId(customerQuery.getCustomer().getCustomerId());
		dto.setCustomerName(customerQuery.getCustomer().getFirstName()); 
		dto.setSubject(customerQuery.getSubject());
		dto.setMessage(customerQuery.getMessage());
		dto.setSubmittedAt(customerQuery.getSubmittedAt());
		dto.setStatus(customerQuery.getStatus());
		return dto;
	}

	@Override
	public String updateEmployee(EmployeeRequestDto employeeRequestDto) {
		System.out.println("In update profile");
		User user = userRepository.findByEmail(getEmailFromSecurityContext()).orElse(null);
		if (user.getEmail() == null) {
			String message = "No employee found with this email";
//        logger.error(message);
			throw new ResourceNotFoundException(message);
		}

		Employee employee = employeeRepository.findByUser(user);
		if (employee == null) {
			throw new ResourceNotFoundException("Employee not found");

		}
		if (employeeRequestDto.getFirstName() != null) {
			employee.setFirstName(employeeRequestDto.getFirstName());
		}
		if (employeeRequestDto.getLastName() != null) {
			employee.setLastName(employeeRequestDto.getLastName());
		}

		
		Employee updatedEmployee = employeeRepository.save(employee);

		return "Employee Updated Suucessfully";
	}

	@Override
	public PagedResponse<DocumentResponseDto> getAllDocuments(int page, int size, String sortBy, String direction,
			String filterBy, String filterValue) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);

		Page<Document> documentPage;

		if (filterBy != null && filterValue != null) {
			if ("documentname".equalsIgnoreCase(filterBy)) {
				DocumentType documentType;
				try {
					documentType = DocumentType.valueOf(filterValue.toUpperCase());
				} catch (IllegalArgumentException e) {
					documentType = null;
				}
				documentPage = documentRepository1.findByDocumentName(documentType, pageable);
			} else {
				documentPage = documentRepository1.findAll(pageable);
			}
		} else {
			documentPage = documentRepository1.findAll(pageable);
		}

		List<DocumentResponseDto> documents = documentPage.getContent().stream().map(document -> {
			DocumentResponseDto dto = new DocumentResponseDto();
			dto.setDocumentId(document.getDocumentId());
			dto.setDocumentName(document.getDocumentName() != null ? document.getDocumentName().name() : null);
			dto.setVerified(document.isVerified());

			// Safe handling of potential null values
			if (document.getCustomer() != null) {
				dto.setCustomer_Id(document.getCustomer().getCustomerId());
			} else {
				dto.setCustomer_Id((Long) null);
			}

			if (document.getVerifyBy() != null) {
				dto.setVerified_By(document.getVerifyBy().getEmployeeId());
			} else {
				dto.setVerified_By((Long) null);
			}

			dto.setContent(document.getContent());

			return dto;
		}).collect(Collectors.toList());

		return new PagedResponse<>(documents, documentPage.getNumber(), documentPage.getSize(),
				documentPage.getTotalElements(), documentPage.getTotalPages(), documentPage.isLast());
	}

	@Override
	public String verifyDocument(int documentId, long employeeId) {
		Document document = documentRepository1.findById(documentId)
				.orElseThrow(() -> new LifeGuardOtpException.ResourceNotFoundException(
						"Sorry, we couldn't find a document with ID: " + documentId));

		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new LifeGuardOtpException.UserNotFoundException(
						"Sorry, we couldn't find an employee with ID: " + employeeId));

		document.setVerified(true);
		document.setVerifyBy(employee);
		documentRepository1.save(document);

		return "Document with ID " + documentId + " has been successfully verified by employee "
				+ employee.getFirstName() + " " + employee.getLastName() + ".";
	}

	@Override
	public void updateCustomerProfileById(long customerId, CustomerDto customerRequestDto) {
		
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));

		
		if (customerRequestDto.getFirstName() != null && !customerRequestDto.getFirstName().isEmpty()) {
			customer.setFirstName(customerRequestDto.getFirstName());
		}
		if (customerRequestDto.getLastName() != null && !customerRequestDto.getLastName().isEmpty()) {
			customer.setLastName(customerRequestDto.getLastName());
		}
		if (customerRequestDto.getPhoneNumber() != 0) {
			customer.setPhoneNumber(customerRequestDto.getPhoneNumber());
		}

	
		customerRepository.save(customer);
	}

}
