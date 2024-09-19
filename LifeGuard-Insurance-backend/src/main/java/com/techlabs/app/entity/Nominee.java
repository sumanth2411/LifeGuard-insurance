package com.techlabs.app.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Nominee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nominee Name is required")
    @Column(nullable = false)
    private String nomineeName;

    @NotBlank(message = "Relation status is required")
    @Column(nullable = false)
    private String relationStatus;

    @ManyToOne(fetch = FetchType.LAZY) // Set fetch type to LAZY
    @JoinColumn(name = "insurance_policy_id")
    private InsurancePolicy insurancePolicy;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public InsurancePolicy getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(InsurancePolicy insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }
}
