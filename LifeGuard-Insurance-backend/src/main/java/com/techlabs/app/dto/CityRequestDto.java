package com.techlabs.app.dto;

import lombok.Data;

@Data
public class CityRequestDto {

    private String name;
    private Long state_id;  // Updated to match the updated naming convention
    private boolean isActive; 
   
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getState_id() {
		return state_id;
	}
	public void setState_id(Long state_id) {
		this.state_id = state_id;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

    
}
