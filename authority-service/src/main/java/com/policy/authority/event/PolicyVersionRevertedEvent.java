package com.policy.authority.event;

import java.util.UUID;

public class PolicyVersionRevertedEvent extends PolicyEvent {
    private final String fromVersion;
    private final String toVersion;
    
    public PolicyVersionRevertedEvent(UUID policyId, String policyName, String username,
                                     String fromVersion, String toVersion) {
        super(policyId, policyName, username);
        this.fromVersion = fromVersion;
        this.toVersion = toVersion;
    }
    
    public String getFromVersion() {
        return fromVersion;
    }
    
    public String getToVersion() {
        return toVersion;
    }
    
    @Override
    public String getEventType() {
        return "POLICY_VERSION_REVERTED";
    }
}
