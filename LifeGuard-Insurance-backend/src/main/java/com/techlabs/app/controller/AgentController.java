package com.techlabs.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.CustomerDto;
import com.techlabs.app.dto.CustomerRegistrationDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.dto.JWTAuthResponse;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.service.AgentService;
import com.techlabs.app.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/LifeGuard/api/agent")
@PreAuthorize("hasRole('AGENT')")
public class AgentController {

  private AgentService agentService;
  
  
  
   public AgentController(AgentService agentService) {
    super();
    this.agentService = agentService;
  }




  @PostMapping("/claim")
  public ResponseEntity<String> AgentclaimPolicy(@RequestBody ClaimRequestDto claimRequestDto,
                                            @RequestParam Long agentId) {
      String response = agentService.agentclaimPolicy(claimRequestDto, agentId);
      return ResponseEntity.ok(response);
  }
  
  @GetMapping("/{agentId}/customers")
  public ResponseEntity<List<CustomerDto>> getCustomersByCity(@PathVariable long agentId) {
      List<CustomerDto> customers = agentService.getCustomersByCity(agentId);
      return ResponseEntity.ok(customers);
  }


  
  @PostMapping("/register-customer")
  public ResponseEntity<JWTAuthResponse> registerOrFetchCustomer(@RequestBody CustomerRegistrationDto customerRegistrationDto) {
      try {
          JWTAuthResponse response = agentService.registerOrFetchCustomer(customerRegistrationDto);
          return ResponseEntity.ok(response);
      } catch (APIException e) {
       
          JWTAuthResponse errorResponse = new JWTAuthResponse(null, "Bearer", "ROLE_AGENT", null, null, e.getMessage());
          return new ResponseEntity<>(errorResponse, e.getStatus());
      } catch (Exception e) {
          
          e.printStackTrace();
          JWTAuthResponse genericError = new JWTAuthResponse(null, "Bearer", "ROLE_AGENT", null, null, "An error occurred while processing the request.");
          return new ResponseEntity<>(genericError, HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
  
  
  @PutMapping("/profile")
  public ResponseEntity<String> updateProfile(@RequestBody AgentRequestDto agentRequestDto) {
      String updatedAgent = agentService.updateAgent(agentRequestDto);
      return ResponseEntity.ok(updatedAgent);
  }

  @PostMapping("/changePassword")
  public ResponseEntity<String> changePassword(@RequestBody Map<String, String> request) {
      String email = request.get("email");
      String oldPassword = request.get("oldPassword");
      String newPassword = request.get("newPassword");
      String confirmNewPassword = request.get("confirmNewPassword");

      if (newPassword == null  || confirmNewPassword == null) {
          return ResponseEntity.badRequest().body("New password and confirm password must not be null.");
      }

      agentService.changeAgentPassword(email, oldPassword, newPassword, confirmNewPassword);
      return ResponseEntity.ok("Password changed successfully.");
  }

  // New endpoint for sending email with policy registration link
  @PostMapping("/sendPolicyEmail")
  public ResponseEntity<String> sendPolicyEmail(@RequestBody Map<String, String> request) {
      String recipientEmail = request.get("recipientEmail");
      String registrationLink = request.get("registrationLink");

      if (recipientEmail == null  || recipientEmail.isEmpty()) {
          return ResponseEntity.badRequest().body("Recipient email is missing.");
      }
      if (registrationLink == null || registrationLink.isEmpty()) {
          return ResponseEntity.badRequest().body("Registration link is missing.");
      }

      agentService.sendPolicyEmail(recipientEmail, registrationLink);
      return ResponseEntity.ok("Email sent successfully to " + recipientEmail);
  }
  
  @Operation(summary = "Get All Customers")
  @GetMapping("/getallCustomers")
  public ResponseEntity<PagedResponse<CustomerDto>> getAllCustomers(
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "5") int size,
          @RequestParam(name = "sortBy", defaultValue = "customerId") String sortBy,
          @RequestParam(name = "direction", defaultValue = "asc") String direction) {
      PagedResponse<CustomerDto> customers = agentService.getAllCustomers(page, size, sortBy, direction);
      return new ResponseEntity<>(customers, HttpStatus.OK);
  }


  
}