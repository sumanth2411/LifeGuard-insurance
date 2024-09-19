package com.techlabs.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "scheme_documents")
public class SchemeDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment for ID
    private Long id;

    @NotBlank 
    @Column(name="document_name",nullable=false)
    private String name;

    // Constructors
    public SchemeDocument() {
    }

    public SchemeDocument(String name) {
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
