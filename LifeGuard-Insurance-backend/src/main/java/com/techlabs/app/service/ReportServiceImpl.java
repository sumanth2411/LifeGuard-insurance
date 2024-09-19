package com.techlabs.app.service;

import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.CityRequestDto;
import com.techlabs.app.dto.CommissionDto;
import com.techlabs.app.dto.CustomerDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.exception.ResourceNotFoundException;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CommissionRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.service.ReportService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private AgentRepository agentRepository;
    
    @Autowired
    private CommissionRepository commissionRepository;

    @Override
    public void generateCustomerReport(HttpServletResponse response) throws IOException {
        List<CustomerDto> customers = fetchCustomerDtos();  // Fetch the customers as DTOs

        // Create PDF document
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

            // Add watermark
            writer.setPageEvent(new WatermarkPageEvent());

            document.open();

            // Add title
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            fontTitle.setSize(18);
            Paragraph title = new Paragraph("Customer Report", fontTitle);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            // Add table with customer data
            PdfPTable table = new PdfPTable(9); // Updated column count to 9 to include active and verified
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Table headers
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            table.addCell(new PdfPCell(new Phrase("ID", fontHeader)));
            table.addCell(new PdfPCell(new Phrase("First Name", fontHeader)));
            table.addCell(new PdfPCell(new Phrase("Last Name", fontHeader)));
            table.addCell(new PdfPCell(new Phrase("DOB", fontHeader)));
            table.addCell(new PdfPCell(new Phrase("Phone Number", fontHeader)));
            table.addCell(new PdfPCell(new Phrase("PAN Card", fontHeader)));
            table.addCell(new PdfPCell(new Phrase("Aadhaar Card", fontHeader)));
            table.addCell(new PdfPCell(new Phrase("Active", fontHeader)));  // New column for Active status
            table.addCell(new PdfPCell(new Phrase("Verified", fontHeader))); // New column for Verified status

            // Table rows
            for (CustomerDto customer : customers) {
                table.addCell(String.valueOf(customer.getCustomerId()));
                table.addCell(customer.getFirstName() != null ? customer.getFirstName() : "");
                table.addCell(customer.getLastName() != null ? customer.getLastName() : "");
                table.addCell(customer.getDob() != null ? customer.getDob().toString() : "");
                table.addCell(String.valueOf(customer.getPhoneNumber()));
               // table.addCell(customer.getPanCard() != null ? customer.getPanCard() : "");
              //  table.addCell(customer.getAadhaarCard() != null ? customer.getAadhaarCard() : "");
                table.addCell(customer.isActive() ? "Yes" : "No");  // Display "Yes" if active, "No" otherwise
                table.addCell(customer.isVerified() ? "Yes" : "No"); // Display "Yes" if verified, "No" otherwise
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new IOException("Error generating PDF document", e);
        }
    }

    // Fetch customers and map to CustomerResponseDto (you should replace this with actual mapping logic)
    private List<CustomerDto> fetchCustomerDtos() {
        // Use a mapping method to convert entities to DTOs
        return customerRepository.findAll().stream()
                .map(this::mapToCustomerResponseDto)  // Assuming you have a method to map entities to DTOs
                .toList();
    }

    // Assuming this method maps the Customer entity to CustomerResponseDto
    private CustomerDto mapToCustomerResponseDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(customer.getCustomerId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setDob(customer.getDob());
        dto.setPhoneNumber(customer.getPhoneNumber());
       // dto.setPanCard(customer.getPanCard());
      //  dto.setAadhaarCard(customer.getAadhaarCard());
       // dto.setCity(new CityRequestDto(customer.getCity().getId(), customer.getCity().getName())); // Assuming you have a CityRequestDto
        dto.setActive(customer.isActive());
        dto.setVerified(customer.isVerified());
        return dto;
    }

    // Add watermark
    class WatermarkPageEvent extends PdfPageEventHelper {
        Font FONT = new Font(Font.FontFamily.HELVETICA, 72, Font.BOLD, new GrayColor(0.85f));  // Increase watermark size and adjust opacity

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(writer.getDirectContentUnder(),
                    Element.ALIGN_CENTER, new Phrase("Lifeguard", FONT),
                    297.5f, 421, writer.getPageNumber() % 2 == 1 ? 45 : -45);
        }
    }
    
    @Override
    public void generateAgentReport(HttpServletResponse response) throws IOException {
        List<AgentResponseDto> agents = fetchAgentDtos();  // Fetch the agents as DTOs

        // Create PDF document
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

            // Add watermark
            writer.setPageEvent(new WatermarkPageEvent());

            document.open();

            // Add title
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            fontTitle.setSize(18);
            Paragraph title = new Paragraph("Agent Report", fontTitle);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            // Add table with agent data
            PdfPTable table = new PdfPTable(8); // 8 columns to include agent data
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Table headers
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            table.addCell(new PdfPCell(new Phrase("ID", fontHeader)));
            table.addCell(new PdfPCell(new Phrase("First Name", fontHeader)));
            table.addCell(new PdfPCell(new Phrase("Last Name", fontHeader)));
            table.addCell(new PdfPCell(new Phrase("Email", fontHeader)));
            table.addCell(new PdfPCell(new Phrase("Phone Number", fontHeader)));
            table.addCell(new PdfPCell(new Phrase("City Name", fontHeader)));
            table.addCell(new PdfPCell(new Phrase("Active", fontHeader)));
            table.addCell(new PdfPCell(new Phrase("Verified", fontHeader)));

            // Table rows
            for (AgentResponseDto agent : agents) {
                table.addCell(String.valueOf(agent.getAgentId()));
                table.addCell(agent.getFirstName() != null ? agent.getFirstName() : "");
                table.addCell(agent.getLastName() != null ? agent.getLastName() : "");
                table.addCell(agent.getEmail() != null ? agent.getEmail() : "");
                table.addCell(agent.getPhoneNumber() != null ? agent.getPhoneNumber() : "");
                table.addCell(agent.getCityName() != null ? agent.getCityName() : "");
                table.addCell(agent.isActive() ? "Yes" : "No");
                table.addCell(agent.isVerified() ? "Yes" : "No");
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new IOException("Error generating PDF document", e);
        }
    }

    // Fetch agents and map to AgentResponseDto (replace with actual mapping logic)
    private List<AgentResponseDto> fetchAgentDtos() {
        // Use a mapping method to convert entities to DTOs
        return agentRepository.findAll().stream()
                .map(this::mapToAgentResponseDto)  // Assuming you have a method to map entities to DTOs
                .toList();
    }

    // Assuming this method maps the Agent entity to AgentResponseDto
    private AgentResponseDto mapToAgentResponseDto(Agent agent) {
        AgentResponseDto dto = new AgentResponseDto();
        dto.setAgentId(agent.getAgentId());
        dto.setFirstName(agent.getFirstName());
        dto.setLastName(agent.getLastName());
        dto.setEmail(agent.getUser().getEmail());
        dto.setPhoneNumber(agent.getPhoneNumber());
        dto.setCityName(agent.getCity().getCity_name());
        dto.setActive(agent.isActive());
        dto.setVerified(agent.isVerified());
        return dto;
    }
    
    @Override
    public void generateAgentDetailReport(Long agentId, HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=agent_report.pdf");

        // Fetch the agent details from the repository
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        // Fetch the commissions for the agent and map to CommissionDto
        List<CommissionDto> commissionDtos = agent.getCommissions().stream()
                .map(this::mapToCommissionDto)
                .collect(Collectors.toList());

        // Populate the AgentResponseDto
        AgentResponseDto agentResponseDto = new AgentResponseDto();
        agentResponseDto.setAgentId(agent.getAgentId());
        agentResponseDto.setFirstName(agent.getFirstName());
        agentResponseDto.setLastName(agent.getLastName());
        agentResponseDto.setEmail(agent.getUser().getEmail());
        agentResponseDto.setPhoneNumber(agent.getPhoneNumber());
        agentResponseDto.setCityName(agent.getCity().getCity_name());
        agentResponseDto.setActive(agent.isActive());
        agentResponseDto.setVerified(agent.isVerified());
        agentResponseDto.setTotalCommission(agent.getTotalCommission());
        agentResponseDto.setCommissions(commissionDtos);

        // Create a Document object
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        // Open the document
        document.open();

        // Add a watermark (optional)
        Phrase watermark = new Phrase("LifeGuard");
        PdfContentByte canvas = writer.getDirectContentUnder();
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, 297.5f, 421, 45);

        // Add a title for the report
        Paragraph title = new Paragraph("Agent Detailed Report");
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Add agent details to the document
        document.add(new Paragraph("\nAgent ID: " + agentResponseDto.getAgentId()));
        document.add(new Paragraph("First Name: " + agentResponseDto.getFirstName()));
        document.add(new Paragraph("Last Name: " + agentResponseDto.getLastName()));
        document.add(new Paragraph("Email: " + agentResponseDto.getEmail()));
        document.add(new Paragraph("Phone Number: " + agentResponseDto.getPhoneNumber()));
        document.add(new Paragraph("City: " + agentResponseDto.getCityName()));
        document.add(new Paragraph("Is Active: " + (agentResponseDto.isActive() ? "Yes" : "No")));
        document.add(new Paragraph("Is Verified: " + (agentResponseDto.isVerified() ? "Yes" : "No")));
        document.add(new Paragraph("Total Commission: " + agentResponseDto.getTotalCommission()));

        // Add commission details
        document.add(new Paragraph("\nCommissions:"));
        for (CommissionDto commission : agentResponseDto.getCommissions()) {
            document.add(new Paragraph("Commission ID: " + commission.getCommissionId()));
            document.add(new Paragraph("Policy Name: " + commission.getPolicyName()));  // Corrected to print policy name
            document.add(new Paragraph("Amount: " + commission.getAmount()));
            document.add(new Paragraph("Date: " + commission.getDate()));
            document.add(new Paragraph("Commission Type: " + commission.getCommissionType()));
            document.add(new Paragraph("\n"));
        }

        // Close the document
        document.close();
    }
    
    
    private CommissionDto mapToCommissionDto(Commission commission) {
        CommissionDto dto = new CommissionDto();
        dto.setCommissionId(commission.getCommissionId());
        dto.setAgentId(commission.getAgent().getAgentId());

        // Set policyId with Long
        dto.setPolicyId(commission.getInsurancePolicy().getInsuranceId()); // Ensure this method exists

        // Set policyName with String
        dto.setPolicyName(commission.getInsurancePolicy().getInsuranceScheme().getInsuranceScheme());

        dto.setAmount(commission.getAmount());
        dto.setDate(commission.getDate());
        dto.setCommissionType(commission.getCommissionType());
        return dto;
    }



    @Override
    public void generateCustomerDetailReport(Long customerId, HttpServletResponse response) throws IOException, DocumentException {
        // Set the content type for the response to PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=customer_report.pdf");

        // Fetch the customer details from the repository
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Create a Document object
        Document document = new Document(PageSize.A4);

        // Create a PdfWriter object to write to the response output stream
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        // Open the document
        document.open();

        // Add a watermark (optional)
        Phrase watermark = new Phrase("LifeGuard");
        PdfContentByte canvas = writer.getDirectContentUnder();
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, 297.5f, 421, 45);

        // Add a title for the report
        Paragraph title = new Paragraph("Customer Detailed Report");
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Add customer details to the document
        document.add(new Paragraph("\nCustomer ID: " + customer.getCustomerId()));
        document.add(new Paragraph("First Name: " + customer.getFirstName()));
        document.add(new Paragraph("Last Name: " + customer.getLastName()));
        document.add(new Paragraph("Date of Birth: " + customer.getDob()));
        document.add(new Paragraph("Phone Number: " + customer.getPhoneNumber()));
       // document.add(new Paragraph("PAN Card: " + customer.getPanCard()));
       // document.add(new Paragraph("Aadhaar Card: " + customer.getAadhaarCard()));
        document.add(new Paragraph("City: " + customer.getCity().getCity_name()));
        document.add(new Paragraph("Is Active: " + (customer.isActive() ? "Yes" : "No")));
        document.add(new Paragraph("Is Verified: " + (customer.isVerified() ? "Yes" : "No")));

        // Add insurance policies in a table
        if (customer.getInsurancePolicies() != null && !customer.getInsurancePolicies().isEmpty()) {
            Paragraph insuranceTitle = new Paragraph("Insurance Policies");
            insuranceTitle.setSpacingBefore(10);
            document.add(insuranceTitle);

            PdfPTable table = new PdfPTable(5); // 5 columns
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            table.addCell("Policy ID");
            table.addCell("Scheme ID");
            table.addCell("Agent ID");
            table.addCell("Claim ID");
            table.addCell("Premium Amount");

            customer.getInsurancePolicies().forEach(policy -> {
                table.addCell(String.valueOf(policy.getInsuranceId()));
                table.addCell(String.valueOf(policy.getInsuranceScheme()));
                table.addCell(String.valueOf(policy.getAgent()));
                table.addCell(String.valueOf(policy.getClaim()));
                table.addCell(String.valueOf(policy.getPremiumAmount()));
            });

            document.add(table);
        } else {
            document.add(new Paragraph("No insurance policies found for this customer."));
        }

        // Close the document
        document.close();
    }
   
    @Override
    public void generateAllCommissionsReport(HttpServletResponse response) throws IOException, DocumentException {
        // Set the content type for the response to PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=all_commissions_report.pdf");

        // Fetch all commission details from the repository
        List<Commission> commissions = commissionRepository.findAll();

        // Map Commission entities to CommissionDto
        List<CommissionDto> commissionDtos = commissions.stream()
                .map(this::mapToCommissionDto)
                .collect(Collectors.toList());

        // Create a Document object
        Document document = new Document(PageSize.A4);

        // Create a PdfWriter object to write to the response output stream
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        // Add a watermark (optional)
        Phrase watermark = new Phrase("LifeGuard");
        PdfContentByte canvas = writer.getDirectContentUnder();
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, 297.5f, 421, 45);

        // Open the document
        document.open();

        // Add a title for the report
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("All Commissions Report", fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Add a blank space before the table
        document.add(new Paragraph("\n"));

        // Create a table with 6 columns
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100); // Set the table width to 100% of the page width
        table.setSpacingBefore(10f); // Set spacing before the table
        table.setSpacingAfter(10f); // Set spacing after the table

        // Set table column widths
        float[] columnWidths = {1f, 2f, 2f, 2f, 2f, 2f};
        table.setWidths(columnWidths);

       
        // Close the document
        document.close();
    }

    

    // Helper method to add table headers
    private void addTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell(new Paragraph("Commission ID"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Agent Name"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Policy Name"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Amount"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Date"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Commission Type"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }
    
    @Override
    public void generateAgentCommissionsReport(HttpServletResponse response) throws IOException, DocumentException {
        // Set the content type for the response to PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=agent_commissions_report.pdf");

        // Get the currently authenticated user's email
        String email = getEmailFromSecurityContext();

        // Fetch the agent details using the email
        Agent agent = agentRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        // Fetch the commissions for the agent
        List<Commission> commissions = commissionRepository.findByAgent(agent);

        // Create a Document object
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        // Open the document
        document.open();

        // Add a title for the report
        Paragraph title = new Paragraph("Agent Commissions Report");
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Add agent details
        document.add(new Paragraph("\nAgent ID: " + agent.getAgentId()));
        document.add(new Paragraph("Name: " + agent.getFirstName() + " " + agent.getLastName()));
        document.add(new Paragraph("Email: " + agent.getUser().getEmail()));
        document.add(new Paragraph("Phone Number: " + agent.getPhoneNumber()));
        document.add(new Paragraph("City: " + agent.getCity().getCity_name()));
        document.add(new Paragraph("Is Active: " + (agent.isActive() ? "Yes" : "No")));
        document.add(new Paragraph("Is Verified: " + (agent.isVerified() ? "Yes" : "No")));

        // Add a table for commissions
        PdfPTable table = new PdfPTable(5); // 5 columns
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Table headers
        table.addCell("Commission ID");
        table.addCell("Policy Name");
        table.addCell("Amount");
        table.addCell("Date");
        table.addCell("Commission Type");

        // Add rows
        for (Commission commission : commissions) {
            table.addCell(String.valueOf(commission.getCommissionId()));
            table.addCell(commission.getInsurancePolicy().getInsuranceScheme().getInsuranceScheme());
            table.addCell(String.format("%.2f", commission.getAmount()));
            table.addCell(commission.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            table.addCell(commission.getCommissionType());
        }

        document.add(table);

        // Close the document
        document.close();
    }

    private String getEmailFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null;
    }
}
