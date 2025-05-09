package com.policy.authority.repository;

import com.policy.authority.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, UUID> { }
