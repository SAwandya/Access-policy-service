package com.policy.authority.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class PolicyRequest {
    @NotBlank
    private String name;
    
    @NotBlank
    private String description;
    
    @NotBlank
    private String policyDefinition;
    
    private List<String> standardMappings = new ArrayList<>();
    
    private String templateId;
    
    private String changeNotes;
    
    // Default constructor
    public PolicyRequest() {
    }
    
    // All-args constructor
    public PolicyRequest(String name, String description, String policyDefinition, 
                        List<String> standardMappings, String templateId, String changeNotes) {
        this.name = name;
        this.description = description;
        this.policyDefinition = policyDefinition;
        this.standardMappings = standardMappings;
        this.templateId = templateId;
        this.changeNotes = changeNotes;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getPolicyDefinition() {
        return policyDefinition;
    }
    
    public List<String> getStandardMappings() {
        return standardMappings;
    }
    
    public String getTemplateId() {
        return templateId;
    }
    
    public String getChangeNotes() {
        return changeNotes;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setPolicyDefinition(String policyDefinition) {
        this.policyDefinition = policyDefinition;
    }
    
    public void setStandardMappings(List<String> standardMappings) {
        this.standardMappings = standardMappings;
    }
    
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    
    public void setChangeNotes(String changeNotes) {
        this.changeNotes = changeNotes;
    }
}
