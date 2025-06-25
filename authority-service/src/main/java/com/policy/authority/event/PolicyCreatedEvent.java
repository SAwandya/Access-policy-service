package com.policy.authority.event;

import java.util.UUID;

public class PolicyCreatedEvent extends PolicyEvent {
    
    public PolicyCreatedEvent(UUID policyId, String policyName, String username) {
        super(policyId, policyName, username);
    }
    
    @Override
    public String getEventType() {
        return "POLICY_CREATED";
    }
}
