package com.techlabs.app.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.techlabs.app.entity.Payment;

public class InsurancePolicyDto {
    private long insuranceId;
    private long insuranceSchemeId;
    private String insuranceSchemeName; 
    private long agentId;
    private long claimId;
    private List<NomineeDto> nominees;
    private List<Long> paymentIds;
    private Set<Long> documentIds;  
    private Set<SubmittedDocumentDto> documents;  
    private List<Long> customerIds;
    private List<PaymentDto> payments=new ArrayList<>();
    private double premiumAmount;
    private int policyTerm;
    private int installmentPeriod;
    private double profitRatio;  
    private double claimAmount;  

    private LocalDate issuedDate; 
    private LocalDate maturityDate; 

    
    private long insurancePlanId;   
    private String insurancePlanName; 
    
    

    
    public List<PaymentDto> getPayments() {
		return payments;
	}

	public void setPayments(List<PaymentDto> payments) {
		this.payments = payments;
	}

	public long getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(long insuranceId) {
        this.insuranceId = insuranceId;
    }

    public long getInsuranceSchemeId() {
        return insuranceSchemeId;
    }

    public void setInsuranceSchemeId(long insuranceSchemeId) {
        this.insuranceSchemeId = insuranceSchemeId;
    }

    public String getInsuranceSchemeName() {
        return insuranceSchemeName;
    }

    public void setInsuranceSchemeName(String insuranceSchemeName) {
        this.insuranceSchemeName = insuranceSchemeName;
    }

    public long getAgentId() {
        return agentId;
    }

    public void setAgentId(long agentId) {
        this.agentId = agentId;
    }

    public long getClaimId() {
        return claimId;
    }

    public void setClaimId(long claimId) {
        this.claimId = claimId;
    }

    public List<NomineeDto> getNominees() {
        return nominees;
    }

    public void setNominees(List<NomineeDto> nominees) {
        this.nominees = nominees;
    }

    public List<Long> getPaymentIds() {
        return paymentIds;
    }

    public void setPaymentIds(List<Long> paymentIds) {
        this.paymentIds = paymentIds;
    }

    public Set<Long> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(Set<Long> documentIds) {
        this.documentIds = documentIds;
    }

    public Set<SubmittedDocumentDto> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<SubmittedDocumentDto> documents) {
        this.documents = documents;
    }

    public List<Long> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<Long> customerIds) {
        this.customerIds = customerIds;
    }

    public double getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public int getPolicyTerm() {
        return policyTerm;
    }

    public void setPolicyTerm(int policyTerm) {
        this.policyTerm = policyTerm;
    }

    public int getInstallmentPeriod() {
        return installmentPeriod;
    }

    public void setInstallmentPeriod(int installmentPeriod) {
        this.installmentPeriod = installmentPeriod;
    }

    public double getProfitRatio() {
        return profitRatio;
    }

    public void setProfitRatio(double profitRatio) {
        this.profitRatio = profitRatio;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public long getInsurancePlanId() {
        return insurancePlanId;
    }

    public void setInsurancePlanId(long insurancePlanId) {
        this.insurancePlanId = insurancePlanId;
    }

    public String getInsurancePlanName() {
        return insurancePlanName;
    }

    public void setInsurancePlanName(String insurancePlanName) {
        this.insurancePlanName = insurancePlanName;
    }
}
