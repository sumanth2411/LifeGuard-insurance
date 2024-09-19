package com.techlabs.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Administrator;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.User;


@Repository
public interface AgentRepository extends JpaRepository<Agent, Long>{

	//Agent findByUser(User user);
	Optional<Agent> findByUser(User user);

	Optional<Agent> findByUser_Email(String email);
	
     
}
