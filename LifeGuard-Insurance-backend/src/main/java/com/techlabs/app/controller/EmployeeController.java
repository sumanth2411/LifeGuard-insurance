package com.techlabs.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.CustomerDto;
import com.techlabs.app.dto.CustomerQueryResponseDto;
import com.techlabs.app.dto.DocumentResponseDto;
import com.techlabs.app.dto.EmployeeRequestDto;
import com.techlabs.app.service.EmployeeService;
import com.techlabs.app.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/LifeGuard/api/employee")
@PreAuthorize("hasRole('EMPLOYEE')")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/registerAgent")
	public ResponseEntity<String> registerAgent(@RequestBody @Valid AgentRequestDto agentRequestDto) {
		System.out.println(agentRequestDto);
		String response = employeeService.registerAgent(agentRequestDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/verifyCustomer/{customerId}")
	public ResponseEntity<String> verifyCustomer(@PathVariable Long customerId) {
		String response = employeeService.verifyCustomerById(customerId);
		return ResponseEntity.ok(response);
	}




	@PutMapping("/verifyPolicy/{policyId}")
	public ResponseEntity<String> verifyPolicyDocuments(@PathVariable long policyId) {
		employeeService.verifyPolicyDocuments(policyId);
		return ResponseEntity.ok("Policy verified successfully");
	}
	
	@Operation(summary = "new verifivation") 
    @PutMapping("/verify/{customerId}") 
    public ResponseEntity<String> verifyCustomer(@PathVariable long customerId) { 
        try { 
            employeeService.verifyCustomer(customerId); 
            return ResponseEntity.ok("Customer verified successfully."); 
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); 
        } 
    }
	
	@Operation(summary = "Get All Agents")
    @GetMapping("/getallAgents")
    public ResponseEntity<PagedResponse<AgentRequestDto>> getAllAgents(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "sortBy", defaultValue = "agentId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        PagedResponse<AgentRequestDto> agents = employeeService.getAllAgents(page, size, sortBy, direction);
        return new ResponseEntity<>(agents, HttpStatus.OK);
    }
    
    @GetMapping("/agents/{agentId}")
    @Operation(summary = "get agent by id", description = " agent")
    public ResponseEntity<AgentResponseDto> getAgentById(@PathVariable long agentId) {
      return new ResponseEntity<AgentResponseDto>(employeeService.getAgentById(agentId), HttpStatus.OK);
    }
    
    @Operation(summary = "Update Agent")
    @PutMapping("updateAgent/{agentId}")
    public ResponseEntity<AgentResponseDto> updateAgent(
            @PathVariable Long agentId,
            @RequestBody AgentRequestDto agentRequestDto) {
        return new ResponseEntity<AgentResponseDto>(employeeService.updateAgent(agentId, agentRequestDto), HttpStatus.OK);
    }
    
    @PostMapping("/changePassword")
	  public ResponseEntity<String> changePassword(@RequestBody Map<String, String> request) {
	      String email = request.get("email");
	      String oldPassword = request.get("oldPassword");
	      String newPassword = request.get("newPassword");
	      String confirmNewPassword = request.get("confirmNewPassword");

	      if (newPassword == null || confirmNewPassword == null) {
	          return ResponseEntity.badRequest().body("New password and confirm password must not be null.");
	      }

	      employeeService.changeEmployeePassword(email, oldPassword, newPassword, confirmNewPassword);
	      return ResponseEntity.ok("Password changed successfully.");
	  }
    
    @GetMapping("/{customerID}")
    @Operation(summary = "Get customer details by ID", description = "Fetch customer details using customer ID")
    public ResponseEntity<CustomerDto> getCustomerIdById(@PathVariable long customerID) {
      return new ResponseEntity<CustomerDto>(employeeService.getCustomerIdById(customerID),
          HttpStatus.OK);
    }
      
      @Operation(summary = "Get All Customers")
      @GetMapping("/getallCustomers")
      public ResponseEntity<PagedResponse<CustomerDto>> getAllCustomers(
              @RequestParam(name = "page", defaultValue = "0") int page,
              @RequestParam(name = "size", defaultValue = "5") int size,
              @RequestParam(name = "sortBy", defaultValue = "customerId") String sortBy,
              @RequestParam(name = "direction", defaultValue = "asc") String direction) {
          PagedResponse<CustomerDto> customers = employeeService.getAllCustomers(page, size, sortBy, direction);
          return new ResponseEntity<>(customers, HttpStatus.OK);
      }
      
      @GetMapping("/getAllQueries")
      public ResponseEntity<Page<CustomerQueryResponseDto>> getAllQueries(Pageable pageable) {
          Page<CustomerQueryResponseDto> queries = employeeService.getAllCustomerQueries(pageable);
          return ResponseEntity.ok(queries);
      }

      @PutMapping("/replyToQuery/{queryId}")
      public ResponseEntity<String> replyToQuery(@PathVariable Long queryId, @RequestBody String response) {
          String result = employeeService.replyToCustomerQuery(queryId, response);
          return ResponseEntity.ok(result);
      }
      
      @PutMapping("/profile")
	    public ResponseEntity<String> updateProfile(@RequestBody EmployeeRequestDto EmployeeRequestDto) {

	        String updatedEmployee = employeeService.updateEmployee(EmployeeRequestDto);
	        return ResponseEntity.ok(updatedEmployee);
	    }
      
      
      @PutMapping("/updateCustomer/{customerId}")
      public ResponseEntity<String> updateCustomerProfile(
              @PathVariable long customerId,
              @RequestBody CustomerDto customerRequestDto) {

          
          employeeService.updateCustomerProfileById(customerId, customerRequestDto);

         
          return ResponseEntity.ok("Customer updated successfully");
      }
  @GetMapping("/getAllDocuments")
      public ResponseEntity<PagedResponse<DocumentResponseDto>> getAllDocuments(
              @RequestParam(name = "page", defaultValue = "0") int page,
              @RequestParam(name = "size", defaultValue = "5") int size,
              @RequestParam(name = "sortBy", defaultValue = "documentId") String sortBy,
              @RequestParam(name = "direction", defaultValue = "asc") String direction,
              @RequestParam(name = "filterBy", required = false) String filterBy,
              @RequestParam(name = "filterValue", required = false) String filterValue) {
          PagedResponse<DocumentResponseDto> documents = employeeService.getAllDocuments(page, size, sortBy, direction, filterBy, filterValue);
          return new ResponseEntity<>(documents, HttpStatus.OK);
      } 
  
  
  @PostMapping("/customer/{documentId}/verify/{employeeId}")
  public ResponseEntity<String> verifyDocument(@PathVariable(name = "documentId") int documentId,
      @PathVariable(name = "employeeId") long employeeId) {

    String response = employeeService.verifyDocument(documentId, employeeId);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
  
  

}