

package com.techlabs.app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public

class Claim {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	@PositiveOrZero(message = "Amount must be a non-negative number")
	@Column(nullable = false)
	private Double claimAmount;
	@NotBlank
	@Column(nullable = false)
	private String bankName;
	@NotBlank
	@Column(nullable = false)
	private String branchName;
	@NotBlank
	@Column(nullable = false)
	private String bankAccountId;
	@NotBlank
	@Column(nullable = false)
	private String ifscCode;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDateTime date = LocalDateTime.now();
	
	private String claimedStatus = ClaimStatus.PENDING.name();
	
	@Column(name="is_cancel", nullable=false)
	private boolean isCancel;
	@OneToOne
	@JoinColumn(name = "policyId")
	private InsurancePolicy policy;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "agentId")
	private Agent agent;
	
	
	public boolean isCancel() {
		return isCancel;
	}

	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

	public long getClaimId() {
		return id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(String bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getClaimedStatus() {
		return claimedStatus;
	}

	public void setClaimedStatus(String claimedStatus) {
		this.claimedStatus = claimedStatus;
	}

	public InsurancePolicy getPolicy() {
		return policy;
	}

	public void setPolicy(InsurancePolicy policy) {
		this.policy = policy;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	

}