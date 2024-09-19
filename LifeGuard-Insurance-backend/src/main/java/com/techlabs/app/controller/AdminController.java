package com.techlabs.app.controller;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.CityRequestDto;
import com.techlabs.app.dto.CityResponseDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.ClaimResponseDto;
import com.techlabs.app.dto.CommissionDto;
import com.techlabs.app.dto.CustomerDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.dto.InsurancePlanDTO;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.InsuranceSchemeDto;
import com.techlabs.app.dto.PaymentDto;
import com.techlabs.app.dto.StateRequestDto;
import com.techlabs.app.dto.StateResponseDto;
import com.techlabs.app.entity.PaymentTax;
import com.techlabs.app.service.AdminService;
import com.techlabs.app.service.FileService;
import com.techlabs.app.util.ImageUtil;
import com.techlabs.app.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/LifeGuard/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private AdminService adminService;

	@Autowired
	private FileService fileService;

	public AdminController(AdminService adminService, FileService fileService) {
		super();
		this.adminService = adminService;
		this.fileService = fileService;
	}

	@Operation(summary = "Add Employee")
	@PostMapping("addEmployee")
	public ResponseEntity<String> registerEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {
		logger.info("Registering employee: {}", employeeRequestDto);
		try {
			String response = adminService.registerEmployee(employeeRequestDto);
			logger.info("Employee registered successfully.");
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			logger.error("Error registering employee: {}", e.getMessage());
			throw e;
		}
	}

	@Operation(summary = "Add Agent")
	@PostMapping("addAgent")
	public ResponseEntity<String> registerAgent(@RequestBody AgentRequestDto agentRequestDto) {
		System.out.println(agentRequestDto);
		logger.info("Registering agent: {}", agentRequestDto);
		try {
			String response = adminService.registerAgent(agentRequestDto);
			logger.info("Agent registered successfully.");
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			logger.error("Error registering agent: {}", e.getMessage());
			throw e;
		}
	}

	@PostMapping("/approveClaim/{claimId}")
	public ResponseEntity<String> approveClaim(@PathVariable Long claimId, @RequestBody ClaimResponseDto claimDto) {
		String result = adminService.approveAgentClaim(claimId, claimDto);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/approveCustomerClaim/{claimId}")
	public ResponseEntity<String> approveCustomerClaim(@PathVariable Long claimId,
			@RequestBody ClaimResponseDto claimDto) {
		String result = adminService.approveCustomerClaim(claimId, claimDto);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	


	
	@GetMapping("/getPendingCustomerClaims")
	public ResponseEntity<PagedResponse<ClaimRequestDto>> getPendingCustomerClaims(
	        @RequestParam(name = "page", defaultValue = "0") int page,
	        @RequestParam(name = "size", defaultValue = "5") int size,
	        @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
	        @RequestParam(name = "direction", defaultValue = "asc") String direction) {
	    PagedResponse<ClaimRequestDto> response = adminService.getPendingCustomerClaims(page, size, sortBy, direction);
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}



	@PutMapping("/verifyAgent/{agentId}")
	public ResponseEntity<String> verifyAgent(@PathVariable Long agentId) {
		String response = adminService.verifyAgent(agentId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Update Employee")
	@PutMapping("updateEmployee/{employeeId}")
	public ResponseEntity<String> updateEmployee(@PathVariable Long employeeId,
			@RequestBody EmployeeRequestDto employeeRequestDto) {
		return new ResponseEntity<>(adminService.updateEmployee(employeeId, employeeRequestDto), HttpStatus.OK);
	}

	@Operation(summary = "Update Agent")
	@PutMapping("updateAgent/{agentId}")
	public ResponseEntity<String> updateAgent(@PathVariable Long agentId,
			@RequestBody AgentRequestDto agentRequestDto) {
		return new ResponseEntity<String>(adminService.updateAgent(agentId, agentRequestDto), HttpStatus.OK);
	}

	@PostMapping("/addState")
	public ResponseEntity<String> createState(@RequestBody StateRequestDto stateRequest) {
		String response = adminService.createState(stateRequest);
		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	@GetMapping("/listAllStates")
	public ResponseEntity<PagedResponse<StateResponseDto>> getAllStates(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "stateId") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		return new ResponseEntity<PagedResponse<StateResponseDto>>(
				adminService.getAllStates(page, size, sortBy, direction), HttpStatus.OK);

	}

	@DeleteMapping("/deleteState/{id}")
	public ResponseEntity<String> deactivateState(@PathVariable(name = "id") long id) {
		return new ResponseEntity<String>(adminService.deactivateStateById(id), HttpStatus.OK);
	}

	@PutMapping("/ActivateState/{id}")
	public ResponseEntity<String> activateState(@PathVariable(name = "id") long id) {
		return new ResponseEntity<String>(adminService.activateStateById(id), HttpStatus.OK);
	}

	@PostMapping("/addCity")
	public ResponseEntity<String> createCity(@RequestBody CityRequestDto cityRequest) {

		try {
			String response = adminService.createCity(cityRequest);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (Exception e) {
			// Log and handle the exception appropriately
			return new ResponseEntity<>("Error creating city: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@DeleteMapping("/deleteCity/{id}")
	public ResponseEntity<String> deactivateCity(@PathVariable(name = "id") long id) {
		return new ResponseEntity<String>(adminService.deactivateCity(id), HttpStatus.OK);

	}

	@GetMapping("/city/{id}")
	public ResponseEntity<CityResponseDto> getCityById(@PathVariable(name = "id") long id) {
		return new ResponseEntity<CityResponseDto>(adminService.getCityById(id), HttpStatus.OK);
	}

	@PutMapping("/activateCity/{id}")
	public ResponseEntity<String> activateCity(@PathVariable(name = "id") long id) {
		return new ResponseEntity<String>(adminService.activateCity(id), HttpStatus.OK);
	}

	@GetMapping("/listAllCities")
	public ResponseEntity<PagedResponse<CityResponseDto>> getAllCities(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		System.out.println("To get all cities");
		return new ResponseEntity<PagedResponse<CityResponseDto>>(
				adminService.getAllCities(page, size, sortBy, direction), HttpStatus.OK);

	}

	@Operation(summary = "Create Insurance Plan")
	@PostMapping("/createPlan")
	public ResponseEntity<String> createInsurancePlan(@RequestBody InsurancePlanDTO insurancePlanDto) {
		logger.info("Creating insurance plan: {}", insurancePlanDto);
		String response = adminService.createInsurancePlan(insurancePlanDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping(value = "/createScheme")
	public ResponseEntity<String> createInsuranceScheme(@RequestParam("insurancePlanId") long insurancePlanId,

			@Valid @RequestBody InsuranceSchemeDto insuranceSchemeDto) {

		String response = adminService.createInsuranceScheme(insurancePlanId, insuranceSchemeDto);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	  @PutMapping(value = "/updateScheme/{insuranceSchemeId}")
	    public ResponseEntity<String> updateInsuranceScheme(
	            @PathVariable("insuranceSchemeId") long insuranceSchemeId,
	            @Valid @RequestBody InsuranceSchemeDto insuranceSchemeDto) {

	        String response = adminService.updateInsuranceScheme(insuranceSchemeId, insuranceSchemeDto);

	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }

	@Operation(summary = "Get All Insurance Plans with Pagination")
	@GetMapping("/listAllPlans")
	public ResponseEntity<PagedResponse<InsurancePlanDTO>> getAllPlans(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "insurancePlanId") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		PagedResponse<InsurancePlanDTO> plans = adminService.getAllPlans(page, size, sortBy, direction);
		return new ResponseEntity<>(plans, HttpStatus.OK);
	}

	@Operation(summary = "Get All Insurance Schemes with Pagination")
	@GetMapping("/listAllSchemes")
	public ResponseEntity<PagedResponse<InsuranceSchemeDto>> getAllSchemes(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "insuranceSchemeId") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		PagedResponse<InsuranceSchemeDto> schemes = adminService.getAllSchemes(page, size, sortBy, direction);
		return new ResponseEntity<>(schemes, HttpStatus.OK);
	}

	@PutMapping("updateInsurancePlan/{planId}")
	@Operation(summary = "Update Insurance Plan")
	public ResponseEntity<String> updateInsurancePlan(@PathVariable Long planId,
			@RequestBody InsurancePlanDTO insurancePlanDto) {
		logger.info("Updating insurance plan with ID {}: {}", planId, insurancePlanDto);
		try {
			String response = adminService.updateInsurancePlan(planId, insurancePlanDto);
			logger.info("Insurance plan updated successfully.");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error updating insurance plan: {}", e.getMessage());
			throw e;
		}
	}

	@Operation(summary = "Get All Schemes by Plan ID with Pagination")
	@GetMapping("/plans/{planId}/schemes")
	public ResponseEntity<PagedResponse<InsuranceSchemeDto>> getAllSchemesByPlanId(@PathVariable Long planId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "insuranceSchemeId") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {

		PagedResponse<InsuranceSchemeDto> schemes = adminService.getAllSchemesByPlanId(planId, page, size, sortBy,
				direction);
		return new ResponseEntity<>(schemes, HttpStatus.OK);
	}
	
	
	@Operation(summary = "Get Scheme by Scheme ID with Pagination")
	@GetMapping("/schemes/{schemeId}")
	public ResponseEntity<PagedResponse<InsuranceSchemeDto>> getSchemeById(
	    @PathVariable Long schemeId,
	    @RequestParam(name = "page", defaultValue = "0") int page,
	    @RequestParam(name = "size", defaultValue = "5") int size,
	    @RequestParam(name = "sortBy", defaultValue = "insuranceSchemeId") String sortBy,
	    @RequestParam(name = "direction", defaultValue = "asc") String direction) {

	    PagedResponse<InsuranceSchemeDto> schemeResponse = adminService.getSchemeById(schemeId, page, size, sortBy, direction);
	    return new ResponseEntity<>(schemeResponse, HttpStatus.OK);
	}


	@Operation(summary = "Get All Agents")
	@GetMapping("/getallAgents")
	public ResponseEntity<PagedResponse<AgentRequestDto>> getAllAgents(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "agentId") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		PagedResponse<AgentRequestDto> agents = adminService.getAllAgents(page, size, sortBy, direction);
		return new ResponseEntity<>(agents, HttpStatus.OK);
	}

	@Operation(summary = "Get All Employees")
	@GetMapping("/getallEmployees")
	public ResponseEntity<PagedResponse<EmployeeRequestDto>> getAllEmployees(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "employeeId") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		PagedResponse<EmployeeRequestDto> employees = adminService.getAllEmployees(page, size, sortBy, direction);
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	@Operation(summary = "Get All Customers")
	@GetMapping("/getallCustomers")
	public ResponseEntity<PagedResponse<CustomerDto>> getAllCustomers(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "customerId") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		PagedResponse<CustomerDto> customers = adminService.getAllCustomers(page, size, sortBy, direction);
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}

	@Operation(summary = "Get All Payments")
	@GetMapping("/getallPayments")
	public ResponseEntity<PagedResponse<PaymentDto>> getAllPayments(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		PagedResponse<PaymentDto> payments = adminService.getAllPayments(page, size, sortBy, direction);
		return new ResponseEntity<>(payments, HttpStatus.OK);
	}

	@Operation(summary = "Get All Commissions")
	@GetMapping("/getallCommissions")
	public ResponseEntity<PagedResponse<CommissionDto>> getAllCommissions(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "commissionId") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		PagedResponse<CommissionDto> commissions = adminService.getAllCommissions(page, size, sortBy, direction);
		return new ResponseEntity<>(commissions, HttpStatus.OK);
	}
	
	  @GetMapping("/payment-tax")
	    public ResponseEntity<PaymentTax> getPaymentTax() {
	        PaymentTax paymentTax = adminService.getPaymentTax();
	        return ResponseEntity.ok(paymentTax);
	    }

	    
	    @PostMapping("/payment-tax")
	    public ResponseEntity<String> setPaymentTax(@RequestBody PaymentTax paymentTax) {
	        adminService.setPaymentTax(paymentTax.getPaymentTax());
	        return ResponseEntity.ok("Payment tax updated successfully");
	    }
	    
	    
	    
	    @GetMapping("/customer/policies/{policyId}/installment-amount")
	    public ResponseEntity<Double> getInstallmentAmount(@PathVariable long policyId) {
	        double installmentAmount = adminService.getInstallmentAmountByPolicyId(policyId);
	        return ResponseEntity.ok(installmentAmount);
	    }
	    
	    
	    @Operation(summary = "Deactivate Agent")
	    @DeleteMapping("/deactivateAgent/{agentId}")
	    public ResponseEntity<String> deactivateAgent(@PathVariable Long agentId) {
	        String response = adminService.deactivateAgent(agentId);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }

	    @Operation(summary = "Activate Agent")
	    @PutMapping("/activateAgent/{agentId}")
	    public ResponseEntity<String> activateAgent(@PathVariable Long agentId) {
	        String response = adminService.activateAgent(agentId);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }
	    
	    @Operation(summary = "Deactivate Employee")
	    @DeleteMapping("/deactivateEmployee/{employeeId}")
	    public ResponseEntity<String> deactivateEmployee(@PathVariable Long employeeId) {
	        String response = adminService.deactivateEmployee(employeeId);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }

	    @Operation(summary = "Activate Employee")
	    @PutMapping("/activateEmployee/{employeeId}")
	    public ResponseEntity<String> activateEmployee(@PathVariable Long employeeId) {
	        String response = adminService.activateEmployee(employeeId);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }
	    
	    @Operation(summary = "Deactivate Customer")
	    @DeleteMapping("/deactivateCustomer/{customerId}")
	    public ResponseEntity<String> deactivateCustomer(@PathVariable Long customerId) {
	        String response = adminService.deactivateCustomer(customerId);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }

	    @Operation(summary = "Activate Customer")
	    @PutMapping("/activateCustomer/{customerId}")
	    public ResponseEntity<String> activateCustomer(@PathVariable Long customerId) {
	        String response = adminService.activateCustomer(customerId);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }
	    
	    @GetMapping("/getCustomersByFirstName")
	    public ResponseEntity<PagedResponse<CustomerDto>> getCustomersByFirstName(
	            @RequestParam(name = "firstName") String firstName,
	            @RequestParam(name = "page", defaultValue = "0") int page,
	            @RequestParam(name = "size", defaultValue = "5") int size,
	            @RequestParam(name = "sortBy", defaultValue = "customerId") String sortBy,
	            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
	        PagedResponse<CustomerDto> response = adminService.getCustomersByFirstName(firstName, page, size, sortBy, direction);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }

	    @GetMapping("/getCustomersByLastName")
	    public ResponseEntity<PagedResponse<CustomerDto>> getCustomersByLastName(
	            @RequestParam(name = "lastName") String lastName,
	            @RequestParam(name = "page", defaultValue = "0") int page,
	            @RequestParam(name = "size", defaultValue = "5") int size,
	            @RequestParam(name = "sortBy", defaultValue = "customerId") String sortBy,
	            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
	        PagedResponse<CustomerDto> response = adminService.getCustomersByLastName(lastName, page, size, sortBy, direction);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }

	    @Operation(summary = "Get Active Customers")
	    @GetMapping("/getActiveCustomers")
	    public ResponseEntity<PagedResponse<CustomerDto>> getActiveCustomers(
	            @RequestParam(name = "page", defaultValue = "0") int page,
	            @RequestParam(name = "size", defaultValue = "5") int size,
	            @RequestParam(name = "sortBy", defaultValue = "customerId") String sortBy,
	            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
	        PagedResponse<CustomerDto> customers = adminService.getCustomersByIsActive(true, page, size, sortBy, direction);
	        return new ResponseEntity<>(customers, HttpStatus.OK);
	    }





}
