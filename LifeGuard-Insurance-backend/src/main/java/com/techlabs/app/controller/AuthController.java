package com.techlabs.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.ForgetPasswordRequestDto;
import com.techlabs.app.dto.JWTAuthResponse;
import com.techlabs.app.dto.LoginDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.dto.ResetPasswordRequestDto;
import com.techlabs.app.service.AuthService;
import com.techlabs.app.service.EmailService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/LifeGuard/api/auth")
public class AuthController {
	@Autowired
	private EmailService emailService;

	private AuthService authService;
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto) {
		logger.debug("Login attempt for user: " + loginDto.getUsernameOrEmail());
		try {

			JWTAuthResponse jwtAuthResponse = authService.login(loginDto);

			logger.info("User logged in successfully with role: {}", jwtAuthResponse.getRole());
			return ResponseEntity.ok(jwtAuthResponse);
		} catch (Exception e) {
			logger.error("Login failed", e);
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = { "/register" })
	public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {

		logger.trace("A TRACE Message" + registerDto);
		logger.debug("A DEBUG Message");
		logger.info("An INFO Message");
		logger.warn("A WARN Message");
		logger.error("An ERROR Message");
		String response = authService.register(registerDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/send-otp")
	public ResponseEntity<String> sendOtpForForgetPassword(
			@RequestBody ForgetPasswordRequestDto otpForgetPasswordRequest) {
		String response = emailService.sendOtpForForgetPassword(otpForgetPasswordRequest.getUsernameOrEmail());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<String> verifyOtpAndSetNewPassword(
			@RequestBody @Validated ResetPasswordRequestDto forgetPasswordRequest) {
		String response = emailService.verifyOtpAndSetNewPassword(forgetPasswordRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
