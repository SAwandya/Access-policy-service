package com.policy.authority.repository;

import com.policy.authority.model.PolicyVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PolicyVersionRepository extends JpaRepository<PolicyVersion, UUID> {
    List<PolicyVersion> findByPolicyIdOrderByCreatedAtDesc(UUID policyId);
    Optional<PolicyVersion> findByPolicyIdAndVersionNumber(UUID policyId, String versionNumber);
}
