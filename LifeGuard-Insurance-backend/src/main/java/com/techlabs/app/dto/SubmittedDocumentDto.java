package com.techlabs.app.dto;

import jakarta.validation.constraints.NotBlank;

public class SubmittedDocumentDto {

	@NotBlank(message = "Document Name is required")
	private String documentName;

	@NotBlank(message = "Document Status is required")
	private String documentStatus;

	@NotBlank(message = "Document Image is required")
	private String documentImage;

	// Default constructor
	public SubmittedDocumentDto() {
	}

	// Parameterized constructor
	public SubmittedDocumentDto(String documentName, String documentStatus, String documentImage) {
		this.documentName = documentName;
		this.documentStatus = documentStatus;
		this.documentImage = documentImage;
	}

	// Getters and Setters
	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(String documentStatus) {
		this.documentStatus = documentStatus;
	}

	public String getDocumentImage() {
		return documentImage;
	}

	public void setDocumentImage(String documentImage) {
		this.documentImage = documentImage;
	}

	@Override
	public String toString() {
		return "SubmittedDocumentDto{" + "documentName='" + documentName + '\'' + ", documentStatus='" + documentStatus
				+ '\'' + ", documentImage='" + documentImage + '\'' + '}';
	}
}
