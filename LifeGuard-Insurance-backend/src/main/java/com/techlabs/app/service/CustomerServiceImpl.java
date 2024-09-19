package com.techlabs.app.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.CustomerDto;
import com.techlabs.app.dto.CustomerQueryRequestDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.NomineeDto;
import com.techlabs.app.dto.PaymentDto;
import com.techlabs.app.dto.SchemeDocumentDto;
import com.techlabs.app.dto.SubmittedDocumentDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.CustomerQuery;
import com.techlabs.app.entity.Document;
import com.techlabs.app.entity.DocumentStatus;
import com.techlabs.app.entity.DocumentType;
import com.techlabs.app.entity.InsurancePlan;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.Nominee;
import com.techlabs.app.entity.Payment;
import com.techlabs.app.entity.PolicyStatus;
import com.techlabs.app.entity.RelationStatus;
import com.techlabs.app.entity.SchemeDocument;
import com.techlabs.app.entity.SubmittedDocument;
import com.techlabs.app.entity.User;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.ClaimStatus;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.LifeGuardException;
import com.techlabs.app.exception.LifeGuardOtpException;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.ClaimRepository;
import com.techlabs.app.repository.CommissionRepository;
import com.techlabs.app.repository.CustomerQueryRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.DocumentRepository;
import com.techlabs.app.repository.DocumentRepository1;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.techlabs.app.repository.InsuranceSchemeRepository;
import com.techlabs.app.repository.KeyValueRepository;
import com.techlabs.app.repository.NomineeRepository;
import com.techlabs.app.repository.PaymentRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.util.ImageUtil;
import com.techlabs.app.util.PagedResponse;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerQueryRepository customerQueryRepository;

	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;

	@Autowired
	private AgentRepository agentRepository;

	@Autowired
	private InsuranceSchemeRepository insuranceSchemeRepository;

	@Autowired
	private NomineeRepository nomineeRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private ClaimRepository claimRepository;

	@Autowired
	private CommissionRepository commissionRepository;

	@Autowired
	private KeyValueRepository keyValueRepository;

	@Autowired
	private DocumentRepository1 documentRepository1;

	@Autowired
	private PaymentRepository paymentRepository;

	@Override
	@Transactional
	public InsurancePolicyDto buyPolicy(InsurancePolicyDto accountRequestDto, long customerId) {
		
		InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(accountRequestDto.getInsuranceSchemeId())
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
						"Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));

		
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
						"Sorry, we couldn't find a customer with ID: " + customerId));

		
		Agent agent = agentRepository.findById(accountRequestDto.getAgentId())
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
						"Sorry, we couldn't find an agent with ID: " + accountRequestDto.getAgentId()));

		
		InsurancePolicy insurancePolicy = new InsurancePolicy();
		insurancePolicy.getCustomers().add(customer);
		insurancePolicy.setAgent(agent); // Set the agent for commission purposes
		insurancePolicy.setPolicyTerm(accountRequestDto.getPolicyTerm());
		insurancePolicy.setPremiumAmount(accountRequestDto.getPremiumAmount());
		insurancePolicy.setIssuedDate(LocalDate.now());
		insurancePolicy.setMaturityDate(insurancePolicy.getIssuedDate().plusYears(accountRequestDto.getPolicyTerm()));
		insurancePolicy.setInstallmentPeriod(accountRequestDto.getInstallmentPeriod());
		insurancePolicy.setRegisteredCommission(insuranceScheme.getNewRegistrationCommission());
		insurancePolicy.setInsuranceScheme(insuranceScheme);
		insurancePolicy.setPolicyStatus(PolicyStatus.PENDING.name());

		double sumAssured = (insurancePolicy.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
				+ insurancePolicy.getPremiumAmount();
		insurancePolicy.setClaimAmount(sumAssured);

		long months = accountRequestDto.getInstallmentPeriod();
		long totalMonths = insurancePolicy.getPolicyTerm() * 12 / months;
		double installmentAmount = insurancePolicy.getPremiumAmount() / totalMonths;
		insurancePolicy.setInstallmentPayment(installmentAmount);
		insurancePolicy.setTotalAmountPaid(0.0);

		
		if (accountRequestDto.getNominees() != null && !accountRequestDto.getNominees().isEmpty()) {
			List<Nominee> nominees = new ArrayList<>();
			for (NomineeDto nomineeDto : accountRequestDto.getNominees()) {
				Nominee nominee = new Nominee();
				nominee.setNomineeName(nomineeDto.getNomineeName());
				nominee.setRelationStatus(nomineeDto.getRelationStatus());
				nomineeRepository.save(nominee);
				nominees.add(nominee);
			}
			insurancePolicy.setNominees(nominees);
		} else {
			throw new APIException(HttpStatus.NOT_FOUND, "No nominees provided in the request");
		}

		// Handle documents using DTO
		Set<SchemeDocument> schemeDocuments = insuranceScheme.getSchemeDocuments();
		if (schemeDocuments != null && !schemeDocuments.isEmpty()) {
			Set<SubmittedDocument> submittedDocuments = new HashSet<>();
			for (SchemeDocument schemeDoc : schemeDocuments) {
				boolean documentFound = false;

				for (SubmittedDocumentDto submittedDto : accountRequestDto.getDocuments()) {
					if (schemeDoc.getName().equalsIgnoreCase(submittedDto.getDocumentName())) {
						SubmittedDocument submittedDoc = new SubmittedDocument();
						submittedDoc.setDocumentName(schemeDoc.getName());
						submittedDoc.setDocumentStatus(DocumentStatus.PENDING.name());
						submittedDoc.setDocumentImage(submittedDto.getDocumentImage());

						documentRepository.save(submittedDoc);
						submittedDocuments.add(submittedDoc);
						documentFound = true;
						break;
					}
				}

				if (!documentFound) {
					throw new APIException(HttpStatus.BAD_REQUEST,
							"Document for " + schemeDoc.getName() + " is missing.");
				}
			}
			insurancePolicy.setDocuments(submittedDocuments);
		} else {
			throw new APIException(HttpStatus.NOT_FOUND, "No scheme documents available for the selected scheme.");
		}

		
		InsurancePolicy savedPolicy = insurancePolicyRepository.save(insurancePolicy);

		
		customer.getInsurancePolicies().add(insurancePolicy);
		customerRepository.save(customer);

		
		double currentCommission = agent.getTotalCommission();
		agent.setTotalCommission(currentCommission + insuranceScheme.getNewRegistrationCommission());
		agentRepository.save(agent);

		
		InsurancePolicyDto policyDto = convertToDto(savedPolicy);

		return policyDto;
	}

	@Override
	@Transactional
	public InsurancePolicyDto buyPolicyWithoutAgent(InsurancePolicyDto accountRequestDto, long customerId) {

		InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(accountRequestDto.getInsuranceSchemeId())
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
						"Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
						"Sorry, we couldn't find a customer with ID: " + customerId));

		InsurancePolicy insurancePolicy = new InsurancePolicy();
		insurancePolicy.getCustomers().add(customer);
		insurancePolicy.setPolicyTerm(accountRequestDto.getPolicyTerm());
		insurancePolicy.setPremiumAmount(accountRequestDto.getPremiumAmount());
		insurancePolicy.setIssuedDate(LocalDate.now());
		insurancePolicy.setMaturityDate(insurancePolicy.getIssuedDate().plusYears(accountRequestDto.getPolicyTerm()));
		insurancePolicy.setInstallmentPeriod(accountRequestDto.getInstallmentPeriod());
		insurancePolicy.setRegisteredCommission(insuranceScheme.getNewRegistrationCommission());
		insurancePolicy.setInsuranceScheme(insuranceScheme);
		insurancePolicy.setPolicyStatus(PolicyStatus.PENDING.name());

		double sumAssured = (insurancePolicy.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
				+ insurancePolicy.getPremiumAmount();
		insurancePolicy.setClaimAmount(sumAssured);

		long months = accountRequestDto.getInstallmentPeriod();
		long totalMonths = insurancePolicy.getPolicyTerm() * 12 / months;
		double installmentAmount = insurancePolicy.getPremiumAmount() / totalMonths;
		insurancePolicy.setInstallmentPayment(installmentAmount);
		insurancePolicy.setTotalAmountPaid(0.0);

		if (accountRequestDto.getNominees() != null && !accountRequestDto.getNominees().isEmpty()) {
			List<Nominee> nominees = new ArrayList<>();
			for (NomineeDto nomineeDto : accountRequestDto.getNominees()) {
				Nominee nominee = new Nominee();
				nominee.setNomineeName(nomineeDto.getNomineeName());
				nominee.setRelationStatus(nomineeDto.getRelationStatus());
				nomineeRepository.save(nominee);
				nominees.add(nominee);
			}
			insurancePolicy.setNominees(nominees);
		}

		Set<SchemeDocument> schemeDocuments = insuranceScheme.getSchemeDocuments();
		if (schemeDocuments != null && !schemeDocuments.isEmpty()) {
			Set<SubmittedDocument> submittedDocuments = new HashSet<>();
			for (SchemeDocument schemeDoc : schemeDocuments) {
				boolean documentFound = false;

				for (SubmittedDocumentDto submittedDto : accountRequestDto.getDocuments()) {
					if (schemeDoc.getName().equalsIgnoreCase(submittedDto.getDocumentName())) {
						SubmittedDocument submittedDoc = new SubmittedDocument();
						submittedDoc.setDocumentName(schemeDoc.getName());
						submittedDoc.setDocumentStatus(DocumentStatus.PENDING.name());
						submittedDoc.setDocumentImage(submittedDto.getDocumentImage());

						documentRepository.save(submittedDoc);
						submittedDocuments.add(submittedDoc);
						documentFound = true;
						break;
					}
				}

				if (!documentFound) {
					throw new APIException(HttpStatus.BAD_REQUEST,
							"Document for " + schemeDoc.getName() + " is missing.");
				}
			}
			insurancePolicy.setDocuments(submittedDocuments);
		} else {
			throw new APIException(HttpStatus.NOT_FOUND, "No scheme documents available for the selected scheme.");
		}

		InsurancePolicy savedPolicy = insurancePolicyRepository.save(insurancePolicy);

		customer.getInsurancePolicies().add(insurancePolicy);
		customerRepository.save(customer);

		InsurancePolicyDto policyDto = convertToDto(savedPolicy);

		return policyDto;
	}

	@Override
	public String customerCancelPolicy(ClaimRequestDto claimRequestDto, Long customerId) {
		
		InsurancePolicy policy = insurancePolicyRepository.findById(claimRequestDto.getPolicyId())
				.orElseThrow(() -> new LifeGuardException("Policy not found"));

		
		boolean customerExistsInPolicy = policy.getCustomers().stream()
				.anyMatch(customer -> customer.getCustomerId() == customerId);

		if (!customerExistsInPolicy) {
			throw new LifeGuardException("Customer is not associated with this policy.");
		}

		
		Optional<Claim> existingClaim = claimRepository.findByPolicy(policy);

		if (existingClaim.isPresent()) {
			System.out.println("Claim already exists for the policy, updating it...");
		} else {
			System.out.println("No existing claim, creating a new one...");
		}

		Claim claim;
		if (existingClaim.isPresent()) {
			
			claim = existingClaim.get();
		} else {
			
			claim = new Claim();
			claim.setPolicy(policy);
		}

		double policyAmount = policy.getTotalAmountPaid();
		
		double claimAmount;
		if (policy.getMaturityDate().isAfter(LocalDate.now())) {
			// Apply 20% deduction if canceled before maturity
			double deductionPercentage = Double.parseDouble(keyValueRepository.getValueByKey("deduction_percentage"));
			double deductionAmount = policyAmount * (deductionPercentage / 100);
			claimAmount = policyAmount - deductionAmount;

		
			claim.setCancel(true);
			System.out.println("Policy canceled before maturity, applying 20% deduction.");
		} else {
			
			claimAmount = policyAmount;
			claim.setCancel(false);
			System.out.println("Policy claimed after maturity, no deduction applied.");
		}

		
		claim.setClaimAmount(claimAmount);
		claim.setBankName(claimRequestDto.getBankName());
		claim.setBranchName(claimRequestDto.getBranchName());
		claim.setBankAccountId(claimRequestDto.getBankAccountId());
		claim.setIfscCode(claimRequestDto.getIfscCode());
		claim.setClaimedStatus("PENDING");

		
		claim = claimRepository.save(claim);
		System.out.println("Claim has been saved: " + claim);

		
		policy.setClaim(claim);
		insurancePolicyRepository.save(policy);

		return "Policy cancellation or claim has been created for the customer.";
	}

	@Override
	public String uploadDocument(MultipartFile file, DocumentType documentName, long customerId) throws IOException {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new LifeGuardOtpException.UserNotFoundException(
						"Sorry, we couldn't find a customer with ID: " + customerId));

		Document document = new Document();
		document.setCustomer(customer);
		document.setContent(ImageUtil.compressFile(file.getBytes()));
		document.setDocumentName(documentName);
		documentRepository1.save(document);
		return "Document '" + documentName + "' has been successfully uploaded for customer ID " + customerId + ".";
	}

	@Override
	public String createCustomerQuery(CustomerQueryRequestDto customerQueryRequestDto) {

		System.out.println("In Customer query");
		User user = userRepository.findByEmail(getEmailFromSecurityContext()).orElse(null);
		if (user.getEmail() == null) {
			String message = "No Customer found with this email";
			throw new APIException(HttpStatus.BAD_REQUEST, message);
		}

		Customer customer = customerRepository.findByUser(user).orElse(null);

		if (customer == null) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Customer not found");
		}

		CustomerQuery customerQuery = new CustomerQuery();
		customerQuery.setSubject(customerQueryRequestDto.getSubject());
		customerQuery.setMessage(customerQueryRequestDto.getMessage());
		customerQuery.setSubmittedAt(LocalDateTime.now());
		customerQuery.setStatus("PENDING");

		customerQuery.setCustomer(customer);

		CustomerQuery savedQuery = customerQueryRepository.save(customerQuery);

		return "Query created successfully";
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

	public InsurancePolicyDto getPolicyById(long customerId, long policyId) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
						"Sorry, we couldn't find a customer with ID: " + customerId));

		InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(policyId).orElseThrow(
				() -> new APIException(HttpStatus.NOT_FOUND, "Sorry, we couldn't find a policy with ID: " + policyId));

		if (!insurancePolicy.getCustomers().contains(customer)) {
			throw new APIException(HttpStatus.FORBIDDEN,
					"This policy does not belong to the customer with ID: " + customerId);
		}

		return convertToDto(insurancePolicy);
	}

	private InsurancePolicyDto convertToDto(InsurancePolicy insurancePolicy) {
		InsurancePolicyDto dto = new InsurancePolicyDto();
		dto.setInsuranceId(insurancePolicy.getInsuranceId());
		dto.setInsuranceSchemeId(insurancePolicy.getInsuranceScheme().getInsuranceSchemeId());
		dto.setPremiumAmount(insurancePolicy.getPremiumAmount());
		dto.setPolicyTerm(insurancePolicy.getPolicyTerm());
		dto.setInstallmentPeriod(insurancePolicy.getInstallmentPeriod());
		dto.setNominees(insurancePolicy.getNominees().stream().map(nominee -> {
			NomineeDto nomineeDto = new NomineeDto();
			nomineeDto.setNomineeName(nominee.getNomineeName());
			nomineeDto.setRelationStatus(nominee.getRelationStatus());
			return nomineeDto;
		}).collect(Collectors.toList()));
		dto.setDocuments(insurancePolicy.getDocuments().stream().map(document -> {
			SubmittedDocumentDto documentDto = new SubmittedDocumentDto();
			documentDto.setDocumentName(document.getDocumentName());
			documentDto.setDocumentStatus(document.getDocumentStatus());
			return documentDto;
		}).collect(Collectors.toSet()));
		return dto;
	}


	
	
	@Transactional
	public PagedResponse<InsurancePolicyDto> getPolicyDetails(long customerId, int page, int size, String sortBy,
	        String direction) {

	    Customer customer = customerRepository.findById(customerId)
	            .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Customer not found with ID: " + customerId));

	    Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();

	    PageRequest pageable = PageRequest.of(page, size, sort);

	    // Fetch policies that do not have a status of "CLAIMED"
	    Page<InsurancePolicy> policyPage = insurancePolicyRepository.findByCustomersContainingAndPolicyStatusNot(customer, 
	            PolicyStatus.CLAIMED.name(), pageable);

	    List<InsurancePolicyDto> policyDtos = policyPage.getContent().stream().map(this::convertToDto1)
	            .collect(Collectors.toList());

	    return new PagedResponse<>(policyDtos, policyPage.getNumber(), policyPage.getSize(),
	            policyPage.getTotalElements(), policyPage.getTotalPages(), policyPage.isLast());
	}
	
	
	private InsurancePolicyDto convertToDto1(InsurancePolicy insurancePolicy) {
	    InsurancePolicyDto dto = new InsurancePolicyDto();

	  
	    if (insurancePolicy.getPolicyStatus().equals(PolicyStatus.CLAIMED.name())) {
	        
	        return null; 
	    }

	    dto.setInsuranceId(insurancePolicy.getInsuranceId());
	    dto.setIssuedDate(insurancePolicy.getIssuedDate());
	    dto.setMaturityDate(insurancePolicy.getMaturityDate());
	    dto.setPolicyTerm(insurancePolicy.getPolicyTerm());
	    dto.setPremiumAmount(insurancePolicy.getPremiumAmount());
	    dto.setClaimAmount(insurancePolicy.getClaimAmount());
	    dto.setInstallmentPeriod(insurancePolicy.getInstallmentPeriod());

	   
	    Agent agent = insurancePolicy.getAgent();
	    if (agent != null) {
	        dto.setAgentId(agent.getAgentId());
	    } else {
	        dto.setAgentId(0); 
	    }

	    
	    InsuranceScheme insuranceScheme = insurancePolicy.getInsuranceScheme();
	    if (insuranceScheme != null) {
	        dto.setInsuranceSchemeId(insuranceScheme.getInsuranceSchemeId());
	        dto.setInsuranceSchemeName(insuranceScheme.getInsuranceScheme());
	        dto.setProfitRatio(insuranceScheme.getProfitRatio());

	        InsurancePlan insurancePlan = insuranceScheme.getInsurancePlan();
	        if (insurancePlan != null) {
	            dto.setInsurancePlanId(insurancePlan.getInsurancePlanId());
	            dto.setInsurancePlanName(insurancePlan.getName());
	        }
	    }

	    
	    Claim claim = insurancePolicy.getClaim();
	    if (claim != null) {
	        dto.setClaimId(claim.getClaimId());
	    } else {
	        dto.setClaimId(0); 
	    }

	    return dto;
	}



	@Override
	@Transactional
	public CustomerDto getCustomerDetailsByPolicyId(long policyId) {
		// Fetch the policy with payments and other details
		InsurancePolicy policy = insurancePolicyRepository.findByIdWithPayments(policyId)
				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Policy not found with ID: " + policyId));

		List<Payment> fpayment = paymentRepository.findByPolicy(policyId);
		List<Customer> customers = policy.getCustomers();
		if (customers == null || customers.isEmpty()) {
			throw new APIException(HttpStatus.NOT_FOUND, "No customers found for policy ID: " + policyId);
		}

		
		Customer customer = customers.get(0);

		
		CustomerDto customerDto = new CustomerDto();
		customerDto.setCustomerId(customer.getCustomerId());
		customerDto.setFirstName(customer.getFirstName());
		customerDto.setLastName(customer.getLastName());
		customerDto.setEmail(customer.getUser().getEmail());
		customerDto.setCity(customer.getCity().getCity_name());
		customerDto.setPhoneNumber(customer.getPhoneNumber());
		customerDto.setActive(customer.isActive());

		
		InsurancePolicyDto policyDto = new InsurancePolicyDto();
		policyDto.setInsuranceId(policy.getInsuranceId());
		policyDto.setInsuranceSchemeId(policy.getInsuranceScheme().getInsuranceSchemeId());
		policyDto.setInsuranceSchemeName(policy.getInsuranceScheme().getInsuranceScheme());
		policyDto.setInsurancePlanName(policy.getInsuranceScheme().getInsurancePlan().getName());
		policyDto.setIssuedDate(policy.getIssuedDate());
		policyDto.setMaturityDate(policy.getMaturityDate());
		policyDto.setPremiumAmount(policy.getPremiumAmount());
		policyDto.setProfitRatio(policy.getInsuranceScheme().getProfitRatio());
		policyDto.setClaimAmount(policy.getClaimAmount());
		policyDto.setPolicyTerm(policy.getPolicyTerm());
		policyDto.setInstallmentPeriod(policy.getInstallmentPeriod());
		policyDto.setAgentId(policy.getAgent() != null ? policy.getAgent().getAgentId() : 0);

	
		List<PaymentDto> paymentDtos = new ArrayList<>();
		int totalInstallments = policy.getPolicyTerm() * 12 / policy.getInstallmentPeriod();
		LocalDate installmentStartDate = policy.getIssuedDate();
		double installmentAmount = policy.getPremiumAmount() / totalInstallments; // Calculate installment amount

		// Create a map of payments made for this policy based on installment date
		Map<LocalDate, Payment> paymentMap = fpayment.stream()
				.collect(Collectors.toMap(payment -> payment.getPaymentDate().toLocalDate(), payment -> payment,
						(existing, replacement) -> replacement));



		int n = fpayment.size();
		for (int i = 1; i <= totalInstallments; i++) {
			PaymentDto paymentDto = new PaymentDto();
			paymentDto.setInstallmentNumber(i);
			LocalDate installmentDate = installmentStartDate.plusMonths((long) policy.getInstallmentPeriod() * (i - 1));
			paymentDto.setInstallmentDate(installmentDate);
			paymentDto.setInstallmentAmount(installmentAmount); 
			paymentDto.setPaymentStatus("Unpaid"); 

			// Check if the payment has been made
			
			if (i - 1 < n) {
				paymentDto.setPaymentStatus(fpayment.get(i - 1).getPaymentStatus());
				paymentDto.setPaidDate(fpayment.get(i - 1).getPaymentDate());
			}

			paymentDtos.add(paymentDto);
		}

		policyDto.setPayments(paymentDtos);

		
		customerDto.setInsurancePolicies(Collections.singletonList(policyDto));

		return customerDto;
	}
	
	@Override
	public CustomerDto getCustomerProfile(long customerId) {
	    // Fetch the customer using the customer repository
	    Customer customer = customerRepository.findById(customerId)
	            .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Customer not found with ID: " + customerId));

	    // Map the customer entity to CustomerDto
	    CustomerDto customerDto = new CustomerDto();
	    customerDto.setCustomerId(customer.getCustomerId());
	    customerDto.setFirstName(customer.getFirstName());
	    customerDto.setLastName(customer.getLastName());
	    customerDto.setEmail(customer.getUser().getEmail()); // Get email from associated User entity
	    customerDto.setDob(customer.getDob());
	    customerDto.setPhoneNumber(customer.getPhoneNumber());
	    //customerDto.setPassword(customer.getUser().getPassword()); // Get password from associated User entity

	    return customerDto;
	}



}
