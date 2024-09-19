package com.techlabs.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;


import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
	 Optional<Claim> findByPolicy(InsurancePolicy policy);

	List<Claim> findByClaimedStatus(String string);

	Page<Claim> findByClaimedStatus(String string, PageRequest pageable);

}
