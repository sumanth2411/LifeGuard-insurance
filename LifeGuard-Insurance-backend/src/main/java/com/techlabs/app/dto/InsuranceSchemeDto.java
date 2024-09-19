package com.techlabs.app.dto;

import java.util.Set;

import lombok.Data;

@Data
public class InsuranceSchemeDto {
	private long insuranceSchemeId;
	private String insuranceScheme;
	private int minimumPolicyTerm;
	private int maximumPolicyTerm;
	private int minimumAge;
	private int maximumAge;
	private double minimumInvestmentAmount;
	private double maximumInvestmentAmount;
	private double profitRatio;

	private double newRegistrationCommission;
	private double installmentPaymentCommission;
	private String description;
	private boolean active = true; // Set active to true by default
	private long insurancePlanId;
	private String schemeImage;
	private Set<SchemeDocumentDto> schemeDocument;

	public Set<SchemeDocumentDto> getSchemeDocument() {
		return schemeDocument;
	}

	public void setSchemeDocument(Set<SchemeDocumentDto> schemeDocument) {
		this.schemeDocument = schemeDocument;
	}

	public long getInsuranceSchemeId() {
		return insuranceSchemeId;
	}

	public String getSchemeImage() {
		return schemeImage;
	}

	public void setSchemeImage(String schemeImage) {
		this.schemeImage = schemeImage;
	}

	public void setInsuranceSchemeId(long insuranceSchemeId) {
		this.insuranceSchemeId = insuranceSchemeId;
	}

	public String getInsuranceScheme() {
		return insuranceScheme;
	}

	public void setInsuranceScheme(String insuranceScheme) {
		this.insuranceScheme = insuranceScheme;
	}

	public int getMinimumPolicyTerm() {
		return minimumPolicyTerm;
	}

	public void setMinimumPolicyTerm(int minimumPolicyTerm) {
		this.minimumPolicyTerm = minimumPolicyTerm;
	}

	public int getMaximumPolicyTerm() {
		return maximumPolicyTerm;
	}

	public void setMaximumPolicyTerm(int maximumPolicyTerm) {
		this.maximumPolicyTerm = maximumPolicyTerm;
	}

	public int getMinimumAge() {
		return minimumAge;
	}

	public void setMinimumAge(int minimumAge) {
		this.minimumAge = minimumAge;
	}

	public int getMaximumAge() {
		return maximumAge;
	}

	public void setMaximumAge(int maximumAge) {
		this.maximumAge = maximumAge;
	}

	public double getMinimumInvestmentAmount() {
		return minimumInvestmentAmount;
	}

	public void setMinimumInvestmentAmount(double minimumInvestmentAmount) {
		this.minimumInvestmentAmount = minimumInvestmentAmount;
	}

	public double getMaximumInvestmentAmount() {
		return maximumInvestmentAmount;
	}

	public void setMaximumInvestmentAmount(double maximumInvestmentAmount) {
		this.maximumInvestmentAmount = maximumInvestmentAmount;
	}

	public double getProfitRatio() {
		return profitRatio;
	}

	public void setProfitRatio(double profitRatio) {
		this.profitRatio = profitRatio;
	}

	public double getNewRegistrationCommission() {
		return newRegistrationCommission;
	}

	public void setNewRegistrationCommission(double newRegistrationCommission) {
		this.newRegistrationCommission = newRegistrationCommission;
	}

	public double getInstallmentPaymentCommission() {
		return installmentPaymentCommission;
	}

	public void setInstallmentPaymentCommission(double installmentPaymentCommission) {
		this.installmentPaymentCommission = installmentPaymentCommission;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(long insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}
}
