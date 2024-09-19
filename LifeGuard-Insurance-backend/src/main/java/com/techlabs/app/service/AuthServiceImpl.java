package com.techlabs.app.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.JWTAuthResponse;
import com.techlabs.app.dto.LoginDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.entity.Administrator;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Employee;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.repository.AdministratorRepository;
import com.techlabs.app.repository.AgentRepository; // Add the AgentRepository
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.CustomerRepository; // Add the CustomerRepository
import com.techlabs.app.repository.EmployeeRepository; // Add the EmployeeRepository
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.security.JwtTokenProvider;

import jakarta.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private AdministratorRepository adminRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private AgentRepository agentRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CityRepository cityRepository;

	public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
			RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
			AdministratorRepository adminRepository, EmployeeRepository employeeRepository,
			AgentRepository agentRepository, CustomerRepository customerRepository) {
		super();
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
		this.adminRepository = adminRepository;
		this.employeeRepository = employeeRepository;
		this.agentRepository = agentRepository;
		this.customerRepository = customerRepository;
	}

	@Override
	public JWTAuthResponse login(LoginDto loginDto) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtTokenProvider.generateToken(authentication);

		User user;
		if (loginDto.getUsernameOrEmail().contains("@")) {
			user = userRepository.findByEmail(loginDto.getUsernameOrEmail())
					.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found"));
		} else {
			user = userRepository.findByUsername(loginDto.getUsernameOrEmail())
					.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found"));
		}

		JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
		jwtAuthResponse.setAccessToken(token);

		jwtAuthResponse.setUsername(user.getUsername());

		customerRepository.findByUser(user).ifPresent(customer -> {
			jwtAuthResponse.setCustomerId(customer.getCustomerId());
		});

		agentRepository.findByUser(user).ifPresent(agent -> {
			jwtAuthResponse.setAgentId(agent.getAgentId());
		});

		for (Role role : user.getRoles()) {
			jwtAuthResponse.setRole(role.getName());
			break;
		}

		return jwtAuthResponse;
	}

	@Transactional
	@Override
	public String register(RegisterDto registerDto) {
		if (userRepository.existsByUsername(registerDto.getUsername())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
		}

		if (userRepository.existsByEmail(registerDto.getEmail())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
		}

		User user = new User();
		user.setUsername(registerDto.getUsername());
		user.setEmail(registerDto.getEmail());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		Set<Role> roles = new HashSet<>();
		for (String roleName : registerDto.getRoles()) {
			Role role = roleRepository.findByName(roleName)
					.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: " + roleName));
			if (!roleName.equals("ROLE_ADMIN") && !roleName.equals("ROLE_CUSTOMER")) {
				throw new APIException(HttpStatus.BAD_REQUEST, "Only Admins and Customers can self-register");
			}
			roles.add(role);
		}
		user.setRoles(roles);

		userRepository.save(user);

		for (Role role : roles) {
			if (role.getName().equals("ROLE_ADMIN")) {
				registerAdministrator(user, registerDto);
			} else if (role.getName().equals("ROLE_CUSTOMER")) {
				registerCustomer(user, registerDto);
			}
		}

		return "User registered successfully!";
	}

	private void registerCustomer(User user, RegisterDto registerDto) {

		City city = cityRepository.findById(registerDto.getCityId())
				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "City not found"));

		Customer customer = new Customer();
		customer.setCustomerId(user.getId());
		customer.setUser(user);
		customer.setFirstName(registerDto.getFirstName());
		customer.setLastName(registerDto.getLastName());
		customer.setPhoneNumber(registerDto.getPhone_number());
		customer.setDob(registerDto.getDob());
		customer.setCity(city);

		customer.setActive(true);
		customer.setVerified(false);

		customerRepository.save(customer);
	}

	private void registerAdministrator(User user, RegisterDto registerDto) {
		Administrator administrator = new Administrator();
		administrator.setAdminId(user.getId());
		administrator.setUser(user);
		administrator.setFirstName(registerDto.getFirstName());
		administrator.setLastName(registerDto.getLastName());
		administrator.setPhoneNumber(registerDto.getPhone_number());
		System.out.println("Phone number being processed: " + registerDto.getPhone_number());

		adminRepository.save(administrator);
	}
}