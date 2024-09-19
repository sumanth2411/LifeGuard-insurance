package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.CustomerDto;
import com.techlabs.app.dto.CustomerRegistrationDto;
import com.techlabs.app.dto.JWTAuthResponse;
import com.techlabs.app.util.PagedResponse;

public interface AgentService {

  //String updateAgent(AgentRequestDto agentRequestDto);

String agentclaimPolicy(ClaimRequestDto claimRequestDto, Long agentId);

List<CustomerDto> getCustomersByCity(long agentId);

JWTAuthResponse registerOrFetchCustomer(CustomerRegistrationDto customerRegistrationDto);

String updateAgent(AgentRequestDto agentRequestDto);



void changeAgentPassword(String email, String oldPassword, String newPassword, String confirmNewPassword);

//String updateCustomerProfile(@Valid CustomerRequestDto customerRequestDto);

void sendPolicyEmail(String recipientEmail, String registrationLink);

PagedResponse<CustomerDto> getAllCustomers(int page, int size, String sortBy, String direction);

}