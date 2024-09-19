package com.techlabs.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.OtpStore;


@Repository
public interface OtpRepository extends JpaRepository<OtpStore, Long>{

  void deleteByUsername(String username);

  Optional<OtpStore> findByUsernameAndOtp(String username, String otp);

}