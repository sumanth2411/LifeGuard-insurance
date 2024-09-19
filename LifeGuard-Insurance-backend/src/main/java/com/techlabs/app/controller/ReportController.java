package com.techlabs.app.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.techlabs.app.service.ReportService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/LifeGuard/api/report")

public class ReportController {
	
	@Autowired
	private ReportService reportService;
	@GetMapping("/customer/{customerId}")
    public void getCustomerReport(@PathVariable Long customerId, HttpServletResponse response) throws DocumentException, IOException {
        reportService.generateCustomerDetailReport(customerId, response);
    }
	
	@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customers")
    public void generateCustomerReport(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=customers_report.pdf");
        
        reportService.generateCustomerReport(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/agents")
    public void generateAgentReport(HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=agent_report.pdf");
        reportService.generateAgentReport(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/agents/{agentId}")
    public void generateAgentDetailReport(@PathVariable Long agentId, HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=agent_report_" + agentId + ".pdf");
        reportService.generateAgentDetailReport(agentId, response);
    }
    
   
    
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/commissions")
    public void generateAllCommissionsReport(HttpServletResponse response) throws IOException, DocumentException {
        reportService.generateAllCommissionsReport(response);
    }
    
    @PreAuthorize("hasRole('AGENT')")
    @GetMapping("/agent/commissions")
    public void generateAgentCommissionsReport(HttpServletResponse response) throws IOException, DocumentException {
        reportService.generateAgentCommissionsReport(response);
    }
}
