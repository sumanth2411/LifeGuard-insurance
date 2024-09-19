
package com.techlabs.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "insurance_schemes")
public class InsuranceScheme {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long insuranceSchemeId;

	@Column(nullable = false)
	private String insuranceScheme;

	private int minimumPolicyTerm;

	private int maximumPolicyTerm;

	private int minimumAge;

	private int maximumAge;

	private double minimumInvestmentAmount;

	private double maximumInvestmentAmount;

	private double profitRatio;

	@Column(name = "scheme_image", columnDefinition = "LONGTEXT")
	private String schemeImage;

	private double newRegistrationCommission;

	private double installmentPaymentCommission;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false)
	private boolean active = true; // Set active to true by default

	@ManyToOne
	@JoinColumn(name = "insurance_plan_id")
	private InsurancePlan insurancePlan;

	@OneToMany(mappedBy = "insuranceScheme", cascade = CascadeType.ALL)
	private List<InsurancePolicy> insurancePolicies;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "scheme_documents_mapping", joinColumns = @JoinColumn(name = "insurance_scheme_id"), inverseJoinColumns = @JoinColumn(name = "scheme_document_id"))
	private Set<SchemeDocument> schemeDocuments;

	public long getInsuranceSchemeId() {
		return insuranceSchemeId;
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

	public String getSchemeImage() {
		return schemeImage;
	}

	public void setSchemeImage(String schemeImage) {
		this.schemeImage = schemeImage;
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

	public InsurancePlan getInsurancePlan() {
		return insurancePlan;
	}

	public void setInsurancePlan(InsurancePlan insurancePlan) {
		this.insurancePlan = insurancePlan;
	}

	public List<InsurancePolicy> getInsurancePolicies() {
		return insurancePolicies;
	}

	public void setInsurancePolicies(List<InsurancePolicy> insurancePolicies) {
		this.insurancePolicies = insurancePolicies;
	}

	public Set<SchemeDocument> getSchemeDocuments() {
		return schemeDocuments;
	}

	public void setSchemeDocuments(Set<SchemeDocument> schemeDocuments) {
		this.schemeDocuments = schemeDocuments;
	}

}
