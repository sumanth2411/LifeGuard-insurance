package com.techlabs.app.repository;

import com.techlabs.app.entity.InsurancePlan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InsurancePlanRepository extends JpaRepository<InsurancePlan, Long> {
	
	 
}