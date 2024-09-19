package com.techlabs.app.service;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

public interface ReportService {

	void generateCustomerDetailReport(Long customerId, HttpServletResponse response)
			throws IOException, DocumentException;

	void generateCustomerReport(HttpServletResponse response) throws IOException;

	void generateAgentReport(HttpServletResponse response) throws IOException;

	void generateAllCommissionsReport(HttpServletResponse response) throws IOException, DocumentException;

	void generateAgentDetailReport(Long agentId, HttpServletResponse response) throws IOException, DocumentException;

	void generateAgentCommissionsReport(HttpServletResponse response) throws IOException, DocumentException;

}
