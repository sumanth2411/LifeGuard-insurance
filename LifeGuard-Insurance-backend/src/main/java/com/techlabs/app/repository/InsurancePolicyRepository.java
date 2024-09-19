package com.techlabs.app.repository;

import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {

	 @Query("SELECT ip.insuranceScheme FROM InsurancePolicy ip WHERE ip.insuranceId = :insuranceId")
	    InsuranceScheme findInsuranceSchemeByPolicyId(@Param("insuranceId") Long insuranceId);

	Page<InsurancePolicy> findByCustomersContaining(Customer customer, PageRequest pageable);
	
	 @Query("SELECT p FROM InsurancePolicy p "
	         + "LEFT JOIN FETCH p.payments "
	         + "LEFT JOIN FETCH p.insuranceScheme s "
	         + "LEFT JOIN FETCH s.insurancePlan "
	         + "LEFT JOIN FETCH p.agent "
	         + "LEFT JOIN FETCH p.documents "
	         // Removed LEFT JOIN FETCH p.customers
	         + "WHERE p.insuranceId = :policyId")
	    Optional<InsurancePolicy> findByIdWithPayments(@Param("policyId") long policyId);

	Page<InsurancePolicy> findByCustomersContainingAndPolicyStatusNot(Customer customer, String name,
			PageRequest pageable);

}