package com.techlabs.app.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.InsurancePlanDTO;
import com.techlabs.app.dto.InsuranceSchemeDto;
import com.techlabs.app.service.AdminService;
import com.techlabs.app.util.ImageUtil;
import com.techlabs.app.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/LifeGuard/api/main")
public class MainController {
	@Autowired
	private AdminService adminService;

	public MainController(AdminService adminService) {
		super();
		this.adminService = adminService;
	}

	@Operation(summary = "Get All Insurance Plans with Pagination")
	@GetMapping("/listAllPlans")
	public ResponseEntity<PagedResponse<InsurancePlanDTO>> getAllPlans(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "insurancePlanId") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		PagedResponse<InsurancePlanDTO> plans = adminService.getAllPlans(page, size, sortBy, direction);
		return new ResponseEntity<>(plans, HttpStatus.OK);
	}

	@Operation(summary = "Get All Insurance Schemes with Pagination")
	@GetMapping("/listAllSchemes")
	public ResponseEntity<PagedResponse<InsuranceSchemeDto>> getAllSchemes(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "insuranceSchemeId") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {
		PagedResponse<InsuranceSchemeDto> schemes = adminService.getAllSchemes(page, size, sortBy, direction);
		return new ResponseEntity<>(schemes, HttpStatus.OK);
	}

	@Operation(summary = "Get All Schemes by Plan ID with Pagination")
	@GetMapping("/plans/{planId}/schemes")
	public ResponseEntity<PagedResponse<InsuranceSchemeDto>> getAllSchemesByPlanId(@PathVariable Long planId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "insuranceSchemeId") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction) {

		PagedResponse<InsuranceSchemeDto> schemes = adminService.getAllSchemesByPlanId(planId, page, size, sortBy,
				direction);
		return new ResponseEntity<>(schemes, HttpStatus.OK);
	}
	
	@Operation(summary = "Get Scheme by Scheme ID with Pagination")
	@GetMapping("/schemes/{schemeId}")
	public ResponseEntity<PagedResponse<InsuranceSchemeDto>> getSchemeById(
	    @PathVariable Long schemeId,
	    @RequestParam(name = "page", defaultValue = "0") int page,
	    @RequestParam(name = "size", defaultValue = "5") int size,
	    @RequestParam(name = "sortBy", defaultValue = "insuranceSchemeId") String sortBy,
	    @RequestParam(name = "direction", defaultValue = "asc") String direction) {

	    PagedResponse<InsuranceSchemeDto> schemeResponse = adminService.getSchemeById(schemeId, page, size, sortBy, direction);
	    return new ResponseEntity<>(schemeResponse, HttpStatus.OK);
	}


}
