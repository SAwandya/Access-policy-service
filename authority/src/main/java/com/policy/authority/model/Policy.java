package com.policy.authority.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    // Constructors, getters, setters
    public Policy() {}

    public Policy(String name, String description, String policyDefinition) {
        this.name = name;
        this.description = description;
        this.policyDefinition = policyDefinition;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPolicyDefinition() { return policyDefinition; }
    public void setPolicyDefinition(String policyDefinition) { this.policyDefinition = policyDefinition; }
}
