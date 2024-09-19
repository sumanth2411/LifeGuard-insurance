package com.techlabs.app.dto;
import lombok.Data; 

@Data 
public class CityResponseDto {

     private Long cityId; 
      private String name; 
      private boolean isActive;
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
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