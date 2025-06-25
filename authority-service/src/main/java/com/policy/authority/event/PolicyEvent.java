package com.policy.authority.event;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class PolicyEvent {
    private final UUID policyId;
    private final String policyName;
    private final UUID eventId;
    private final LocalDateTime timestamp;
    private final String username;
    
    public PolicyEvent(UUID policyId, String policyName, String username) {
        this.policyId = policyId;
        this.policyName = policyName;
        this.eventId = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
        this.username = username;
    }
    
    public UUID getPolicyId() {
        return policyId;
    }
    
    public String getPolicyName() {
        return policyName;
    }
    
    public UUID getEventId() {
        return eventId;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getUsername() {
        return username;
    }
    
    public abstract String getEventType();
}
