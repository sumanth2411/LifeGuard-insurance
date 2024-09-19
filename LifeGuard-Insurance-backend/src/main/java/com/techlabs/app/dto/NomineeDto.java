package com.techlabs.app.dto;

import jakarta.validation.constraints.NotBlank;

public class NomineeDto {

	@NotBlank(message = "Nominee Name is required")
	private String nomineeName;

	@NotBlank(message = "Relation status is required")
	private String relationStatus;

	// Default constructor
	public NomineeDto() {
	}

	// Parameterized constructor
	public NomineeDto(String nomineeName, String relationStatus) {
		this.nomineeName = nomineeName;
		this.relationStatus = relationStatus;
	}

	// Getters and Setters
	public String getNomineeName() {
		return nomineeName;
	}

	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}

	public String getRelationStatus() {
		return relationStatus;
	}

	public void setRelationStatus(String relationStatus) {
		this.relationStatus = relationStatus;
	}

	@Override
	public String toString() {
		return "NomineeDto{" + "nomineeName='" + nomineeName + '\'' + ", relationStatus='" + relationStatus + '\''
				+ '}';
	}
}
