package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.techlabs.app.entity.CustomerQuery;

public interface CustomerQueryRepository extends JpaRepository<CustomerQuery, Long>{

}
