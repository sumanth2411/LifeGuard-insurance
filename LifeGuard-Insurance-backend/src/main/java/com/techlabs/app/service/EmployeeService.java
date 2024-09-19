package com.techlabs.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.CustomerDto;
import com.techlabs.app.dto.CustomerQueryResponseDto;
import com.techlabs.app.dto.DocumentResponseDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.util.PagedResponse;

public interface EmployeeService {
	String registerAgent(AgentRequestDto agentRequestDto);

	String verifyCustomerById(Long customerId);

	//String updateEmployee(EmployeeRequestDto employeeRequestDto);

	// String updateAgent(Long agentId, AgentRequestDto agentRequestDto);

	void verifyPolicyDocuments(long policyId);

	void verifyCustomer(long customerId);

	PagedResponse<AgentRequestDto> getAllAgents(int page, int size, String sortBy, String direction);

	AgentResponseDto getAgentById(long agentId);

	AgentResponseDto updateAgent(long agentId, AgentRequestDto updatedAgentData);

	void changeEmployeePassword(String email, String oldPassword, String newPassword, String confirmNewPassword);

	PagedResponse<CustomerDto> getAllCustomers(int page, int size, String sortBy, String direction);

	CustomerDto getCustomerIdById(long customerID);

	String replyToCustomerQuery(Long queryId, String response);

	Page<CustomerQueryResponseDto> getAllCustomerQueries(Pageable pageable);
	String updateEmployee(EmployeeRequestDto employeeRequestDto);

	void updateCustomerProfileById(long customerId, CustomerDto customerRequestDto);

	PagedResponse<DocumentResponseDto> getAllDocuments(int page, int size, String sortBy, String direction,
			String filterBy, String filterValue);

	String verifyDocument(int documentId, long employeeId);
}
