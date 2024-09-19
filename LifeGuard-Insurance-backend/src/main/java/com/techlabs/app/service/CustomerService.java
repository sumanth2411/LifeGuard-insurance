package com.techlabs.app.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.CustomerDto;
import com.techlabs.app.dto.CustomerQueryRequestDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.entity.DocumentType;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.util.PagedResponse;

public interface CustomerService {

	//String createCustomerQuery(CustomerQueryRequestDto customerQueryRequestDto);

	InsurancePolicyDto buyPolicy(InsurancePolicyDto accountRequestDto, long customerId);

	String uploadDocument(MultipartFile file, DocumentType documentName, long customerId) throws IOException;

	String customerCancelPolicy(ClaimRequestDto claimRequestDto, Long customerId);
	
	InsurancePolicyDto buyPolicyWithoutAgent(InsurancePolicyDto insurancePolicyDto, long customerId);

	String createCustomerQuery(CustomerQueryRequestDto customerQueryRequestDto);
	
	InsurancePolicyDto getPolicyById(long customerId, long policyId);
	
	PagedResponse<InsurancePolicyDto> getPolicyDetails(long customerId, int page, int size, String sortBy, String direction);
   
	CustomerDto getCustomerDetailsByPolicyId(long policyId);
	
	public CustomerDto getCustomerProfile(long customerId);



	

}
