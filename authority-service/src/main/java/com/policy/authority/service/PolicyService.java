package com.policy.authority.service;

import com.policy.authority.dto.PolicyRequest;
import com.policy.authority.event.PolicyCreatedEvent;
import com.policy.authority.event.PolicyDeletedEvent;
import com.policy.authority.event.PolicyUpdatedEvent;
import com.policy.authority.event.PolicyVersionRevertedEvent;
import com.policy.authority.exception.ResourceNotFoundException;
import com.policy.authority.model.Policy;
import com.policy.authority.model.PolicyStatus;
import com.policy.authority.model.PolicyVersion;
import com.policy.authority.repository.PolicyRepository;
import com.policy.authority.repository.PolicyVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PolicyService {
    private final PolicyRepository policyRepository;
    private final PolicyVersionRepository policyVersionRepository;
    private final EventPublisher eventPublisher;

    public PolicyService(PolicyRepository policyRepository, 
                        PolicyVersionRepository policyVersionRepository,
                        EventPublisher eventPublisher) {
        this.policyRepository = policyRepository;
        this.policyVersionRepository = policyVersionRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    public Optional<Policy> getPolicyById(UUID id) {
        return policyRepository.findById(id);
    }

    @Transactional
    public Policy createPolicy(PolicyRequest policyRequest, String username) {
        Policy policy = new Policy(
            policyRequest.getName(), 
            policyRequest.getDescription(), 
            policyRequest.getPolicyDefinition()
        );
        
        // Set additional fields
        policy.setStandardMappings(policyRequest.getStandardMappings());
        policy.setTemplateId(policyRequest.getTemplateId());
        policy.setStatus(PolicyStatus.DRAFT);
        policy.setCreatedBy(username);
        policy.setUpdatedBy(username);
        
        // Save the policy first to get an ID
        policy = policyRepository.save(policy);
        
        // Create initial version (1.0.0)
        PolicyVersion version = new PolicyVersion(
            policy, 
            "1.0.0", 
            policyRequest.getPolicyDefinition(), 
            username, 
            "Initial version"
        );
        policy.addVersion(version);
        
        policy = policyRepository.save(policy);
        
        // Publish policy created event
        eventPublisher.publishEvent(new PolicyCreatedEvent(
            policy.getId(),
            policy.getName(),
            username
        ));
        
        return policy;
    }

    @Transactional
    public Policy updatePolicy(UUID id, PolicyRequest updatedPolicy, String username) {
        return policyRepository.findById(id)
                .map(policy -> {
                    policy.setName(updatedPolicy.getName());
                    policy.setDescription(updatedPolicy.getDescription());
                    policy.setPolicyDefinition(updatedPolicy.getPolicyDefinition());
                    policy.setStandardMappings(updatedPolicy.getStandardMappings());
                    policy.setTemplateId(updatedPolicy.getTemplateId());
                    policy.setUpdatedBy(username);
                    
                    // Create a new version
                    String currentVersion = policy.getCurrentVersion() != null 
                        ? policy.getCurrentVersion().getVersionNumber() 
                        : "0.0.0";
                    
                    String newVersion = incrementVersion(currentVersion);
                    
                    PolicyVersion version = new PolicyVersion(
                        policy, 
                        newVersion, 
                        updatedPolicy.getPolicyDefinition(), 
                        username, 
                        updatedPolicy.getChangeNotes()
                    );
                    policy.addVersion(version);
                    
                    Policy savedPolicy = policyRepository.save(policy);
                    
                    // Publish policy updated event
                    eventPublisher.publishEvent(new PolicyUpdatedEvent(
                        savedPolicy.getId(),
                        savedPolicy.getName(),
                        username,
                        newVersion,
                        updatedPolicy.getChangeNotes()
                    ));
                    
                    return savedPolicy;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
    }

    @Transactional
    public void deletePolicy(UUID id) {
        Policy policy = policyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
        
        String policyName = policy.getName();
        String username = policy.getUpdatedBy();
        
        policyRepository.deleteById(id);
        
        // Publish policy deleted event
        eventPublisher.publishEvent(new PolicyDeletedEvent(id, policyName, username));
    }
    
    public List<PolicyVersion> getPolicyVersions(UUID policyId) {
        Policy policy = policyRepository.findById(policyId)
            .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + policyId));
        return policy.getVersions();
    }
    
    public PolicyVersion getPolicyVersion(UUID policyId, String versionNumber) {
        return policyVersionRepository.findByPolicyIdAndVersionNumber(policyId, versionNumber)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Version " + versionNumber + " not found for policy with id: " + policyId));
    }
    
    @Transactional
    public Policy revertToVersion(UUID policyId, String versionNumber, String username) {
        Policy policy = policyRepository.findById(policyId)
            .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + policyId));
        
        PolicyVersion targetVersion = policyVersionRepository.findByPolicyIdAndVersionNumber(policyId, versionNumber)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Version " + versionNumber + " not found for policy with id: " + policyId));
        
        // Update the policy definition from the target version
        policy.setPolicyDefinition(targetVersion.getContent());
        policy.setUpdatedBy(username);
        
        // Get current version before creating a new one
        String currentVersion = policy.getCurrentVersion() != null 
            ? policy.getCurrentVersion().getVersionNumber() 
            : "0.0.0";
            
        // Create a new version that is a copy of the target
        String newVersion = incrementVersion(currentVersion);
        
        PolicyVersion revertedVersion = new PolicyVersion(
            policy, 
            newVersion, 
            targetVersion.getContent(), 
            username, 
            "Reverted to version " + versionNumber
        );
        policy.addVersion(revertedVersion);
        
        Policy savedPolicy = policyRepository.save(policy);
        
        // Publish policy version reverted event
        eventPublisher.publishEvent(new PolicyVersionRevertedEvent(
            policy.getId(),
            policy.getName(),
            username,
            currentVersion,
            versionNumber
        ));
        
        return savedPolicy;
    }
    
    @Transactional
    public Policy updatePolicyStatus(UUID id, PolicyStatus status, String username) {
        Policy policy = policyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + id));
        
        policy.setStatus(status);
        policy.setUpdatedBy(username);
        return policyRepository.save(policy);
    }
    
    public List<Policy> getPoliciesByStandard(String standard) {
        return policyRepository.findByStandardMappingsContaining(standard);
    }
    
    public List<Policy> getPoliciesByTemplate(String templateId) {
        return policyRepository.findByTemplateId(templateId);
    }
    
    public List<Policy> getPoliciesByStatus(PolicyStatus status) {
        return policyRepository.findByStatus(status);
    }
    
    // Helper method to increment version number (X.Y.Z)
    private String incrementVersion(String currentVersion) {
        String[] parts = currentVersion.split("\\.");
        if (parts.length != 3) {
            return "1.0.0"; // Default if format is incorrect
        }
        
        try {
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            int patch = Integer.parseInt(parts[2]);
            
            // Increment minor version, reset patch
            minor++;
            patch = 0;
            
            return major + "." + minor + "." + patch;
        } catch (NumberFormatException e) {
            return "1.0.0"; // Default if parsing fails
        }
    }
}
