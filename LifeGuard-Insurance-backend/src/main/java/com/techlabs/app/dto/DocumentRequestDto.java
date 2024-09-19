package com.techlabs.app.dto;

import com.techlabs.app.entity.DocumentType;
import lombok.Data;

@Data
public class DocumentRequestDto {

    private DocumentType documentName; // This could be an enum or a type you define elsewhere

    private boolean verified;

    private int customeId; // Assuming customerId is used to reference a customer

    private int verifyBy; // Assuming employeeId is used to reference an employee

    private byte[] content; // The actual document content

	public DocumentType getDocumentName() {
		return documentName;
	}

	public void setDocumentName(DocumentType documentName) {
		this.documentName = documentName;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public int getCustomeId() {
		return customeId;
	}

	public void setCustomeId(int customeId) {
		this.customeId = customeId;
	}

	public int getVerifyBy() {
		return verifyBy;
	}

	public void setVerifyBy(int verifyBy) {
		this.verifyBy = verifyBy;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
    
    
}