package com.techlabs.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class AgentResponseDto {

    private long agentId;  // Corresponds to the agentId field in the Agent entity

    private String firstName;
    
    private String lastName;

    private String email;  // Assuming this comes from the associated User entity

    private String phoneNumber;

    private boolean isActive;

    private String cityName;  // From the City entity

    private boolean verified;
    
    private double totalCommission;
    private List<CommissionDto> commissions;
    
    

    // Getters and Setters if not using Lombok
    

    public AgentResponseDto() {
		
	}

	public long getAgentId() {
        return agentId;
    }

    public double getTotalCommission() {
		return totalCommission;
	}

	public void setTotalCommission(double totalCommission) {
		this.totalCommission = totalCommission;
	}

	public List<CommissionDto> getCommissions() {
		return commissions;
	}

	public void setCommissions(List<CommissionDto> commissions) {
		this.commissions = commissions;
	}

	public void setAgentId(long agentId) {
        this.agentId = agentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}