package com.policy.authority.controller;

import com.policy.authority.dto.PolicyRequest;
import com.policy.authority.model.Policy;
import com.policy.authority.model.PolicyStatus;
import com.policy.authority.model.PolicyVersion;
import com.policy.authority.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {
    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping
    @Operation(summary = "Get all policies", description = "Returns all pliocies")
    public List<Policy> getAllPolicies(
            @RequestParam(required = false) PolicyStatus status,
            @RequestParam(required = false) String standard,
            @RequestParam(required = false) String templateId) {
        
        if (status != null) {
            return policyService.getPoliciesByStatus(status);
        } else if (standard != null) {
            return policyService.getPoliciesByStandard(standard);
        } else if (templateId != null) {
            return policyService.getPoliciesByTemplate(templateId);
        }
        
        return policyService.getAllPolicies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Policy> getPolicyById(@PathVariable UUID id) {
        return policyService.getPolicyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Policy> createPolicy(
            @Valid @RequestBody PolicyRequest policy,
            HttpServletRequest request) {
        String username = getCurrentUsername();
        Policy createdPolicy = policyService.createPolicy(policy, username);
        return new ResponseEntity<>(createdPolicy, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Policy> updatePolicy(
            @PathVariable UUID id,
            @Valid @RequestBody PolicyRequest policy) {
        String username = getCurrentUsername();
        Policy updatedPolicy = policyService.updatePolicy(id, policy, username);
        return ResponseEntity.ok(updatedPolicy);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicy(@PathVariable UUID id) {
        String username = getCurrentUsername();
        policyService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/versions")
    public ResponseEntity<List<PolicyVersion>> getPolicyVersions(@PathVariable UUID id) {
        List<PolicyVersion> versions = policyService.getPolicyVersions(id);
        return ResponseEntity.ok(versions);
    }
    
    @GetMapping("/{id}/versions/{versionNumber}")
    public ResponseEntity<PolicyVersion> getPolicyVersion(
            @PathVariable UUID id, 
            @PathVariable String versionNumber) {
        PolicyVersion version = policyService.getPolicyVersion(id, versionNumber);
        return ResponseEntity.ok(version);
    }
    
    @PostMapping("/{id}/revert/{versionNumber}")
    public ResponseEntity<Policy> revertToVersion(
            @PathVariable UUID id, 
            @PathVariable String versionNumber) {
        String username = getCurrentUsername();
        Policy policy = policyService.revertToVersion(id, versionNumber, username);
        return ResponseEntity.ok(policy);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Policy> updatePolicyStatus(
            @PathVariable UUID id,
            @RequestParam PolicyStatus status) {
        String username = getCurrentUsername();
        Policy policy = policyService.updatePolicyStatus(id, status, username);
        return ResponseEntity.ok(policy);
    }
    
    @GetMapping("/standards")
    public ResponseEntity<Map<String, String>> getStandards() {
        // In a real implementation, these would come from a database
        Map<String, String> standards = new HashMap<>();
        standards.put("ISO27001", "ISO 27001 - Information Security Management");
        standards.put("PCIDSS", "PCI DSS - Payment Card Industry Data Security Standard");
        standards.put("GDPR", "GDPR - General Data Protection Regulation");
        standards.put("HIPAA", "HIPAA - Health Insurance Portability and Accountability Act");
        return ResponseEntity.ok(standards);
    }
    
    @GetMapping("/templates")
    public ResponseEntity<Map<String, String>> getTemplates() {
        // In a real implementation, these would come from a database
        Map<String, String> templates = new HashMap<>();
        templates.put("access-control", "Access Control Policy Template");
        templates.put("data-protection", "Data Protection Policy Template");
        templates.put("incident-response", "Incident Response Policy Template");
        return ResponseEntity.ok(templates);
    }
    
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "system";
        }
        return authentication.getName();
    }
}
