package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.techlabs.app.entity.Nominee;

public interface NomineeRepository extends JpaRepository<Nominee, Long> {

}
