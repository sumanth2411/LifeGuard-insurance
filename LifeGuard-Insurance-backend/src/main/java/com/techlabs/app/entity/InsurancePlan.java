package com.techlabs.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "insurance_plan")
public class InsurancePlan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long insurancePlanId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private boolean active = true;



	@OneToMany(mappedBy = "insurancePlan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<InsuranceScheme> insuranceSchemes;

	public long getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(long insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<InsuranceScheme> getInsuranceSchemes() {
		return insuranceSchemes;
	}

	public void setInsuranceSchemes(List<InsuranceScheme> insuranceSchemes) {
		this.insuranceSchemes = insuranceSchemes;
	}

}