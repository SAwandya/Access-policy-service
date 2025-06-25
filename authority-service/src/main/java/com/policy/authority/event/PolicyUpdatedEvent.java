package com.policy.authority.event;

import java.util.UUID;

public class PolicyUpdatedEvent extends PolicyEvent {
    private final String versionNumber;
    private final String changeNotes;
    
    public PolicyUpdatedEvent(UUID policyId, String policyName, String username, 
                             String versionNumber, String changeNotes) {
        super(policyId, policyName, username);
        this.versionNumber = versionNumber;
        this.changeNotes = changeNotes;
    }
    
    public String getVersionNumber() {
        return versionNumber;
    }
    
    public String getChangeNotes() {
        return changeNotes;
    }
    
    @Override
    public String getEventType() {
        return "POLICY_UPDATED";
    }
}
