package com.techlabs.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "document")
@Data
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documentId")
    private int documentId;

    @Enumerated(EnumType.STRING) 
    @Column(name = "document_name")
    private DocumentType documentName;

    @Column(name = "verified")
    private boolean verified;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customerId")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "verified_by", referencedColumnName = "employeeId")
    private Employee verifyBy;

    @Lob
    @Column(name = "content", columnDefinition = "LONGBLOB")
    private byte[] content;

	public Document(int documentId, DocumentType documentName, boolean verified, Customer customer, Employee verifyBy,
			byte[] content) {
		super();
		this.documentId = documentId;
		this.documentName = documentName;
		this.verified = verified;
		this.customer = customer;
		this.verifyBy = verifyBy;
		this.content = content;
	}

	public Document() {
		
	}

	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Employee getVerifyBy() {
		return verifyBy;
	}

	public void setVerifyBy(Employee verifyBy) {
		this.verifyBy = verifyBy;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
	
	

    
}
