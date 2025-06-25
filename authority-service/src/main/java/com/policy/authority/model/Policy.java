package com.policy.authority.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    @Column(length = 4000)
    private String description;

    @NotBlank
    @Column(length = 10000)
    private String policyDefinition; // e.g., JSON or Rego policy text

    @Enumerated(EnumType.STRING)
    private PolicyStatus status = PolicyStatus.DRAFT;
    
    @ElementCollection
    private List<String> standardMappings = new ArrayList<>(); // e.g., ["ISO27001", "PCI-DSS"]
    
    private String templateId; // Reference to a template
    
    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("versionNumber DESC")
    private List<PolicyVersion> versions = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    private String createdBy;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private String updatedBy;
    
    // Constructors
    public Policy() {
    }
    
    public Policy(String name, String description, String policyDefinition) {
        this.name = name;
        this.description = description;
        this.policyDefinition = policyDefinition;
    }
    
    // Full constructor
    public Policy(UUID id, String name, String description, String policyDefinition, 
                  PolicyStatus status, List<String> standardMappings, String templateId,
                  List<PolicyVersion> versions, LocalDateTime createdAt, String createdBy,
                  LocalDateTime updatedAt, String updatedBy) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.policyDefinition = policyDefinition;
        this.status = status;
        this.standardMappings = standardMappings;
        this.templateId = templateId;
        this.versions = versions;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
    
    // Getters
    public UUID getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getPolicyDefinition() {
        return policyDefinition;
    }
    
    public PolicyStatus getStatus() {
        return status;
    }
    
    public List<String> getStandardMappings() {
        return standardMappings;
    }
    
    public String getTemplateId() {
        return templateId;
    }
    
    public List<PolicyVersion> getVersions() {
        return versions;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    // Setters
    public void setId(UUID id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setPolicyDefinition(String policyDefinition) {
        this.policyDefinition = policyDefinition;
    }
    
    public void setStatus(PolicyStatus status) {
        this.status = status;
    }
    
    public void setStandardMappings(List<String> standardMappings) {
        this.standardMappings = standardMappings;
    }
    
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    
    public void setVersions(List<PolicyVersion> versions) {
        this.versions = versions;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    // Helper methods
    public void addVersion(PolicyVersion version) {
        versions.add(version);
        version.setPolicy(this);
    }
    
    public PolicyVersion getCurrentVersion() {
        return versions.isEmpty() ? null : versions.get(0);
    }
}
