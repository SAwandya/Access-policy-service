package com.policy.authority.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class PolicyVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "policy_id")
    @JsonIgnore
    private Policy policy;
    
    private String versionNumber; // e.g., "1.0.0"
    
    @Column(length = 10000)
    private String content; // The policy definition at this version
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    private String createdBy;
    
    @Column(length = 1000)
    private String changeNotes;
    
    public PolicyVersion(Policy policy, String versionNumber, String content, String createdBy, String changeNotes) {
        this.policy = policy;
        this.versionNumber = versionNumber;
        this.content = content;
        this.createdBy = createdBy;
        this.changeNotes = changeNotes;
    }

    // Remove Lombok annotations and add getters/setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Policy getPolicy() {
        return policy;
    }
    
    public void setPolicy(Policy policy) {
        this.policy = policy;
    }
    
    public String getVersionNumber() {
        return versionNumber;
    }
    
    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getChangeNotes() {
        return changeNotes;
    }
    
    public void setChangeNotes(String changeNotes) {
        this.changeNotes = changeNotes;
    }
    
    // Default constructor to replace @NoArgsConstructor
    public PolicyVersion() {
    }
    
    // All-args constructor to replace @AllArgsConstructor
    public PolicyVersion(UUID id, Policy policy, String versionNumber, String content, 
                        LocalDateTime createdAt, String createdBy, String changeNotes) {
        this.id = id;
        this.policy = policy;
        this.versionNumber = versionNumber;
        this.content = content;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.changeNotes = changeNotes;
    }
}
