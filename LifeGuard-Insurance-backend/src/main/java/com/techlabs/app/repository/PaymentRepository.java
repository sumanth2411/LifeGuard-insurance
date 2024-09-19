package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	//Payment findByPolicyAndInstallmentNumber(InsurancePolicy policy, int installmentNumber);

	@Query("Select p from Payment p " + "join p.policy x " + "where (:policyId is NULL OR x.insuranceId= :policyId)")
	List<Payment> findByPolicy(@Param("policyId") long policyId);

}
