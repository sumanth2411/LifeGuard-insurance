package com.techlabs.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Employee;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.User;

public interface EmployeeRepository extends JpaRepository <Employee, Long>{

	Employee findByUser(User user);

	Optional<Employee> findByUser_Email(String email);

}
