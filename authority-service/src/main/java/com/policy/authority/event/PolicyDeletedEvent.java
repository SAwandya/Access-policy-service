package com.policy.authority.event;

import java.util.UUID;

public class PolicyDeletedEvent extends PolicyEvent {
    
    public PolicyDeletedEvent(UUID policyId, String policyName, String username) {
        super(policyId, policyName, username);
    }
    
    @Override
    public String getEventType() {
        return "POLICY_DELETED";
    }
}
