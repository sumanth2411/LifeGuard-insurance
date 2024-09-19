package com.techlabs.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.State;

public interface StateRepository extends JpaRepository<State, Long> {

}
