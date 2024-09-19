package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Customer;

public interface CityRepository extends JpaRepository<City, Long> {

}
