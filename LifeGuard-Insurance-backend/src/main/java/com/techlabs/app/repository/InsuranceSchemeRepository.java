package com.techlabs.app.repository;

import com.techlabs.app.entity.InsurancePlan;
import com.techlabs.app.entity.InsuranceScheme;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InsuranceSchemeRepository extends JpaRepository<InsuranceScheme, Long> {

//
//	@Query("SELECT isch FROM InsuranceScheme isch JOIN isch.insurancePolicies ip WHERE ip.insuranceId = :insuranceId")
//    InsuranceScheme findByInsurancePolicyId(@Param("insuranceId") long insuranceId);
	
	Page<InsuranceScheme> findAllByInsurancePlan(InsurancePlan insurancePlan, Pageable pageable);
}