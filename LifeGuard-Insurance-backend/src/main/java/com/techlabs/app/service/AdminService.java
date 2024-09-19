package com.techlabs.app.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
import com.techlabs.app.util.PagedResponse;

public interface AdminService {
	String registerEmployee(EmployeeRequestDto employeeRequestDto);

	String registerAgent(AgentRequestDto agentRequestDto);

	String updateAgent(Long agentId, AgentRequestDto agentRequestDto);

	String updateEmployee(Long employeeId, EmployeeRequestDto employeeRequestDto);

	String createState(StateRequestDto stateRequest);

	String verifyAgent(Long agentId);

	PagedResponse<StateResponseDto> getAllStates(int page, int size, String sortBy, String direction);

	String deactivateStateById(long id);

	String activateStateById(long id);

	String createCity(CityRequestDto cityRequest);

	String deactivateCity(long id);

	CityResponseDto getCityById(long id);

	String activateCity(long id);

	PagedResponse<CityResponseDto> getAllCities(int page, int size, String sortBy, String direction);

	String createInsurancePlan(InsurancePlanDTO insurancePlanDto);

	// String createInsuranceScheme(InsuranceSchemeDto insuranceSchemeDto);

	// String createInsurancePolicy(InsurancePolicyDto insurancePolicyDto);

	String updateInsurancePlan(Long planId, InsurancePlanDTO insurancePlanDto);

	String approveAgentClaim(Long claimId, ClaimResponseDto claimDto);

	String approveCustomerClaim(Long claimId, ClaimResponseDto claimDto);

	PagedResponse<AgentRequestDto> getAllAgents(int page, int size, String sortBy, String direction);

	// PagedResponse<EmployeeRequestDto> getAllEmployees(int page, int size, String
	// sortBy, String direction);

	PagedResponse<CustomerDto> getAllCustomers(int page, int size, String sortBy, String direction);

	PagedResponse<PaymentDto> getAllPayments(int page, int size, String sortBy, String direction);

	PagedResponse<CommissionDto> getAllCommissions(int page, int size, String sortBy, String direction);

	String createInsuranceScheme(long insurancePlanId, InsuranceSchemeDto insuranceSchemeDto);

	// String updateInsuranceScheme(long insurancePlanId, long insuranceSchemeId,
	// InsuranceSchemeDto insuranceSchemeDto);

	PagedResponse<InsurancePlanDTO> getAllPlans(int page, int size, String sortBy, String direction);

	PagedResponse<InsuranceSchemeDto> getAllSchemes(int page, int size, String sortBy, String direction);

	PagedResponse<InsuranceSchemeDto> getAllSchemesByPlanId(Long planId, int page, int size, String sortBy,
			String direction);

	PagedResponse<EmployeeRequestDto> getAllEmployees(int page, int size, String sortBy, String direction);

	PagedResponse<InsuranceSchemeDto> getSchemeById(Long schemeId, int page, int size, String sortBy, String direction);

	String updateInsuranceScheme(long insuranceSchemeId, InsuranceSchemeDto insuranceSchemeDto);

	PaymentTax getPaymentTax();

	void setPaymentTax(Long paymentTax);

	double getInstallmentAmountByPolicyId(long policyId);

	// List<ClaimRequestDto> getPendingCustomerClaims();

	PagedResponse<ClaimRequestDto> getPendingCustomerClaims(int page, int size, String sortBy, String direction);

	String deactivateAgent(Long agentId);

	String activateAgent(Long agentId);

	String deactivateEmployee(Long employeeId);

	String activateEmployee(Long employeeId);

	String deactivateCustomer(Long customerId);

	String activateCustomer(Long customerId);
	
	PagedResponse<CustomerDto> getCustomersByFirstName(String firstName, int page, int size, String sortBy, String direction);
	PagedResponse<CustomerDto> getCustomersByLastName(String lastName, int page, int size, String sortBy, String direction);
	//PagedResponse<CustomerDto> getCustomersByActiveStatus(boolean isActive, int page, int size, String sortBy, String direction);

//	PagedResponse<CustomerDto> getCustomersByIsActive(int page, int size, String sortBy,
//			String direction);

	PagedResponse<CustomerDto> getCustomersByIsActive(boolean isActive, int page, int size, String sortBy,
			String direction);

	


}
