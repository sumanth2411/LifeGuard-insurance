package com.techlabs.app.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "insurance_policy")
public class InsurancePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long insuranceId;

    @ManyToOne
    @JoinColumn(name = "insurance_scheme_id")
    private InsuranceScheme insuranceScheme;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @OneToOne
    @JoinColumn(name = "claim_id")
    private Claim claim;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "insurance_policy_id")
    private List<Nominee> nominees = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "insurance_policy_id")
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "insurance_policy_id")
    private Set<SubmittedDocument> documents = new HashSet<>();

    @ManyToMany(mappedBy = "insurancePolicies")
    private List<Customer> customers = new ArrayList<>();

    @OneToMany(mappedBy = "insurancePolicy")
    private List<Transaction> transactions = new ArrayList<>();

    private LocalDate issuedDate = LocalDate.now();
    private LocalDate maturityDate;
    private double premiumAmount;

    @Column(name = "policy_status")
    private String policyStatus = PolicyStatus.PENDING.name();


    private int policyTerm; 
    private double registeredCommission;
    private int installmentPeriod; 
    private double installmentPayment;
    private double totalAmountPaid;
    private double claimAmount;
    
    @Column(name = "verified")
    private boolean verified=false;
    
    
    
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	public long getInsuranceId() {
		return insuranceId;
	}
	public void setInsuranceId(long insuranceId) {
		this.insuranceId = insuranceId;
	}
	public InsuranceScheme getInsuranceScheme() {
		return insuranceScheme;
	}
	public void setInsuranceScheme(InsuranceScheme insuranceScheme) {
		this.insuranceScheme = insuranceScheme;
	}
	public Agent getAgent() {
		return agent;
	}
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	public Claim getClaim() {
		return claim;
	}
	public void setClaim(Claim claim) {
		this.claim = claim;
	}
	public List<Nominee> getNominees() {
		return nominees;
	}
	public void setNominees(List<Nominee> nominees) {
		this.nominees = nominees;
	}
	public List<Payment> getPayments() {
		return payments;
	}
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
	public Set<SubmittedDocument> getDocuments() {
		return documents;
	}
	public void setDocuments(Set<SubmittedDocument> documents) {
		this.documents = documents;
	}
	public List<Customer> getCustomers() {
		return customers;
	}
	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
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
	public double getPremiumAmount() {
		return premiumAmount;
	}
	public void setPremiumAmount(double premiumAmount) {
		this.premiumAmount = premiumAmount;
	}
	public String getPolicyStatus() {
		return policyStatus;
	}
	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}
	public int getPolicyTerm() {
		return policyTerm;
	}
	public void setPolicyTerm(int policyTerm) {
		this.policyTerm = policyTerm;
	}
	public double getRegisteredCommission() {
		return registeredCommission;
	}
	public void setRegisteredCommission(double registeredCommission) {
		this.registeredCommission = registeredCommission;
	}
	public int getInstallmentPeriod() {
		return installmentPeriod;
	}
	public void setInstallmentPeriod(int installmentPeriod) {
		this.installmentPeriod = installmentPeriod;
	}
	public double getInstallmentPayment() {
		return installmentPayment;
	}
	public void setInstallmentPayment(double installmentPayment) {
		this.installmentPayment = installmentPayment;
	}
	public double getTotalAmountPaid() {
		return totalAmountPaid;
	}
	public void setTotalAmountPaid(double totalAmountPaid) {
		this.totalAmountPaid = totalAmountPaid;
	}
	public double getClaimAmount() {
		return claimAmount;
	}
	public void setClaimAmount(double claimAmount) {
		this.claimAmount = claimAmount;
	}
	

    
}
