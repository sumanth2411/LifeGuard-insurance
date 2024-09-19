
package com.techlabs.app.config;

import com.techlabs.app.security.JwtAuthenticationEntryPoint;
import com.techlabs.app.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthenticationEntryPoint authenticationEntryPoint;
	private final JwtAuthenticationFilter authenticationFilter;

	public SecurityConfig(JwtAuthenticationEntryPoint authenticationEntryPoint,
			JwtAuthenticationFilter authenticationFilter) {
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.authenticationFilter = authenticationFilter;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(withDefaults()).authorizeHttpRequests(authorize -> authorize

				// Admin Endpoints
				.requestMatchers(HttpMethod.POST, "/LifeGuard/api/admin/plans").hasRole("ADMIN")
				.requestMatchers(HttpMethod.POST, "/LifeGuard/api/admin/policies").hasRole("ADMIN")
				.requestMatchers(HttpMethod.POST, "/LifeGuard/api/admin/**").hasRole("ADMIN")

				// Employee Endpoints
				.requestMatchers(HttpMethod.POST, "/LifeGuard/api/employee/**").hasRole("EMPLOYEE")

				// Agent Endpoints
				.requestMatchers(HttpMethod.POST, "/LifeGuard/api/agent/**").hasRole("AGENT")
				.requestMatchers(HttpMethod.GET, "/LifeGuard/api/agent/{agentId}/customers").permitAll()

				// Payment Endpoints
				.requestMatchers(HttpMethod.POST, "/LifeGuard/payments/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/LifeGuard/api/main/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/LifeGuard/file/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/LifeGuard/file/**").permitAll()
				// Authentication Endpoints
				.requestMatchers("/LifeGuard/api/auth/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/LifeGuard/api/admin/payment-tax").permitAll()
				.requestMatchers(HttpMethod.GET, "/LifeGuard/api/admin/payment-tax").permitAll()
				// Any other request must be authenticated
				.anyRequest().authenticated())
				.exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/swagger-ui/**", "/v3/api-docs/**");
	}
}
