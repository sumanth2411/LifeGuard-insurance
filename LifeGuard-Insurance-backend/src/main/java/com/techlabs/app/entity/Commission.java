
package com.techlabs.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "commissions")
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commissionId;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private InsurancePolicy insurancePolicy;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime date=LocalDateTime.now();
    
    @Column(nullable = false)
    private String CommissionType;

    // Constructors, getters, setters, and toString method
    public Commission(Long commissionId, Agent agent, InsurancePolicy insurancePolicy, Double amount,
                      LocalDateTime date) {
        this.commissionId = commissionId;
        this.agent = agent;
        this.insurancePolicy = insurancePolicy;
        this.amount = amount;
        this.date = date;
    }

    public Commission() {
    }

    

    public Long getCommissionId() {
		return commissionId;
	}

	public void setCommissionId(Long commissionId) {
		this.commissionId = commissionId;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public InsurancePolicy getInsurancePolicy() {
		return insurancePolicy;
	}

	public void setInsurancePolicy(InsurancePolicy insurancePolicy) {
		this.insurancePolicy = insurancePolicy;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	

	public String getCommissionType() {
		return CommissionType;
	}

	public void setCommissionType(String commissionType) {
		CommissionType = commissionType;
	}

	
}
