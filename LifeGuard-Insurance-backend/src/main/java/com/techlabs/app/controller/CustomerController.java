package com.techlabs.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.CustomerDto;
import com.techlabs.app.dto.CustomerQueryRequestDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.entity.DocumentType;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.PaymentTax;
import com.techlabs.app.service.CustomerService;
import com.techlabs.app.util.PagedResponse;

@RestController
@RequestMapping("/LifeGuard/api/customer")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

   


    
    @PostMapping("/customers/{customerId}/buy-policy")
    public ResponseEntity<InsurancePolicyDto> buyPolicy(@RequestBody InsurancePolicyDto insurancePolicyDto,
                                                        @PathVariable(name = "customerId") long customerId) {
        InsurancePolicyDto response = customerService.buyPolicy(insurancePolicyDto, customerId);
        return ResponseEntity.ok(response);
    }

    
    @PostMapping("/customers/{customerId}/buy-policy-without-agent")
    public ResponseEntity<InsurancePolicyDto> buyPolicyWithoutAgent(@RequestBody InsurancePolicyDto insurancePolicyDto,
                                                        @PathVariable(name = "customerId") long customerId) {
    	InsurancePolicyDto  response = customerService.buyPolicyWithoutAgent(insurancePolicyDto, customerId);
        return ResponseEntity.ok(response);
    }



    
    @PostMapping("/cancelPolicy")
    public ResponseEntity<String> customerCancelPolicy(@RequestBody ClaimRequestDto claimRequestDto,
                                                       @RequestParam Long customerId) {
        String response = customerService.customerCancelPolicy(claimRequestDto, customerId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{customerId}/documents")
    public ResponseEntity<String> uploadDocument(@RequestParam(name = "document") MultipartFile file,
        @RequestParam(name = "documentName") DocumentType documentName,
        @PathVariable(name = "customerId") long customerId) throws IOException {
      return new ResponseEntity<String>(customerService.uploadDocument(file, documentName, customerId),
          HttpStatus.OK);
    }
    
    @PostMapping("/createQuery")
    public ResponseEntity<String> createQuery(@RequestBody CustomerQueryRequestDto customerQueryRequestDto) {
      String createdQuery = customerService.createCustomerQuery(customerQueryRequestDto);
      return ResponseEntity.ok(createdQuery);
    }
    
    @GetMapping("/customers/{customerId}/policy/{policyId}")
    public ResponseEntity<InsurancePolicyDto> getPolicyById(@PathVariable(name = "customerId") long customerId,
                                                            @PathVariable(name = "policyId") long policyId) {
        InsurancePolicyDto insurancePolicyDto = customerService.getPolicyById(customerId, policyId);
        return ResponseEntity.ok(insurancePolicyDto);
    }
 
    @GetMapping("/customers/{customerId}/policies")
    public ResponseEntity<PagedResponse<InsurancePolicyDto>> getPolicyDetails(
            @PathVariable(name = "customerId") long customerId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "sortBy", defaultValue = "insuranceId") String sortBy, // Change here
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        return ResponseEntity.ok(customerService.getPolicyDetails(customerId, page, size, sortBy, direction));
    }

    
    @GetMapping("/policies/{policyId}/customer-details")
    public ResponseEntity<CustomerDto> getCustomerDetailsByPolicyId(@PathVariable long policyId) {
        CustomerDto customerDto = customerService.getCustomerDetailsByPolicyId(policyId);
        return ResponseEntity.ok(customerDto);
    }

    @GetMapping("/customers/{customerId}/profile")
    public ResponseEntity<CustomerDto> getCustomerProfile(@PathVariable long customerId) {
        CustomerDto customerProfile = customerService.getCustomerProfile(customerId);
        return ResponseEntity.ok(customerProfile);
    }

    

    
   
}
