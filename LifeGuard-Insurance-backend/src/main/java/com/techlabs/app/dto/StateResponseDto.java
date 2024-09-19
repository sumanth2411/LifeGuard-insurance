package com.techlabs.app.dto;

import java.util.List;
import lombok.Data;

@Data
public class StateResponseDto {

	private Long stateId;
	private String name;
	private boolean isActive;
	private List<CityResponseDto> cities;

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

	public List<CityResponseDto> getCities() {
		return cities;
	}

	public void setCities(List<CityResponseDto> cities) {
		this.cities = cities;
	}

}