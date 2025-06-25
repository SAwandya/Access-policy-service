package com.policy.authority.repository;

import com.policy.authority.model.Policy;
import com.policy.authority.model.PolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, UUID> {
    List<Policy> findByStatus(PolicyStatus status);
    List<Policy> findByStandardMappingsContaining(String standard);
    List<Policy> findByTemplateId(String templateId);
    List<Policy> findByCreatedBy(String createdBy);
}
