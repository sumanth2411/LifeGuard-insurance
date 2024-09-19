package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long>  {

  
}
