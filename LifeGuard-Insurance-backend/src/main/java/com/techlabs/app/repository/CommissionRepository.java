package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Commission;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Long> {

	List<Commission> findByAgent(Agent agent);

	//Commission findByAgentIdAndInsurancePolicyId(Long agentId, Long insurancePolicyId);

	


}
