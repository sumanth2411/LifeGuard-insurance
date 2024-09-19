package com.techlabs.app.dto;

import com.techlabs.app.entity.DocumentType;
import lombok.Data;

@Data
public class DocumentResponseDto {

    private int documentId;

    private String documentName;

    private boolean verified;

    private Long customer_Id; // You might want to include the customer's ID

    private Long verified_By; // You might want to include the employee's ID who verified the document

    private byte[] content; // The actual document content

	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public Long getCustomer_Id() {
		return customer_Id;
	}

	public void setCustomer_Id(Long customer_Id) {
		this.customer_Id = customer_Id;
	}

	public Long getVerified_By() {
		return verified_By;
	}

	public void setVerified_By(Long verified_By) {
		this.verified_By = verified_By;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
    
    


}