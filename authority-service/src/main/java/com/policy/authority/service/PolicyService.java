package com.policy.authority.service;

import com.policy.authority.model.Policy;
import com.policy.authority.repository.PolicyRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PolicyService {
    private final PolicyRepository policyRepository;

    public PolicyService(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    public Optional<Policy> getPolicyById(UUID id) {
        return policyRepository.findById(id);
    }

    public Policy createPolicy(Policy policy) {
        return policyRepository.save(policy);
    }

    public Policy updatePolicy(UUID id, Policy updatedPolicy) {
        return policyRepository.findById(id)
                .map(policy -> {
                    policy.setName(updatedPolicy.getName());
                    policy.setDescription(updatedPolicy.getDescription());
                    policy.setPolicyDefinition(updatedPolicy.getPolicyDefinition());
                    return policyRepository.save(policy);
                })
                .orElseThrow(() -> new RuntimeException("Policy not found"));
    }

    public void deletePolicy(UUID id) {
        policyRepository.deleteById(id);
    }
}
