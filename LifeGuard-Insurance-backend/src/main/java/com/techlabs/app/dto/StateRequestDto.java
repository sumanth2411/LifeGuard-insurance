package com.techlabs.app.dto;
import lombok.Data; 

@Data 
public class StateRequestDto {
    private Long stateId; 
    
      private String name;
      private boolean isActive;
	public Long getStateId() {
		return stateId;
	}
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	
      
      
}