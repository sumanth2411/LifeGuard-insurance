package com.techlabs.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Administrator;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.User;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	//Optional<Customer> findByPanCard(String panCard);
	Optional<Customer> findByUser(User user);
	//Customer findByUser1(User user);

	List<Customer> findByCity(City city);

	

	 @Query("SELECT c FROM Customer c WHERE c.isActive = :isActive")
	    Page<Customer> findByIsActive(@Param("isActive") boolean isActive, Pageable pageable);

	Page<Customer> findByLastNameContainingIgnoreCase(String lastName, PageRequest pageable);

	Page<Customer> findByFirstNameContainingIgnoreCase(String firstName, PageRequest pageable);
}
