package com.policy.authority.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.policy.authority.event.PolicyEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class EventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);
    
    private final KafkaTemplate<String, PolicyEvent> kafkaTemplate;
    private final boolean kafkaEnabled;
    private final String policyEventsTopic;
    private final long kafkaTimeoutMs;
    
    // Spring will resolve the @Value annotations and pass them into the constructor
    public EventPublisher(KafkaTemplate<String, PolicyEvent> kafkaTemplate,
                            @Value("${app.kafka.enabled}") boolean kafkaEnabled,
                            @Value("${app.kafka.topics.policy-events:policy-events}") String policyEventsTopic,
                            @Value("${app.kafka.timeout.ms:1000}") long kafkaTimeoutMs) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaEnabled = kafkaEnabled; // Now kafkaEnabled has the correct value
        this.policyEventsTopic = policyEventsTopic;
        this.kafkaTimeoutMs = kafkaTimeoutMs;
    }
    
    public void publishEvent(PolicyEvent event) {
        // This logic now works perfectly
        if (!kafkaEnabled) {
            logger.info("Kafka publishing disabled. Event {} logged but not published.", event.getEventType());
            return;
        }
        
        try {
            CompletableFuture<SendResult<String, PolicyEvent>> future = 
                kafkaTemplate.send(policyEventsTopic, event.getPolicyId().toString(), event);
            
            future.completeOnTimeout(null, kafkaTimeoutMs, TimeUnit.MILLISECONDS)
                .whenComplete((result, ex) -> {
                    if (ex == null && result != null) {
                        logger.info("Published event [{}] for policy: {}", 
                            event.getEventType(), event.getPolicyId());
                    } else {
                        logger.warn("Failed to publish event [{}] for policy: {}. " + 
                            "Continuing without Kafka. Error: {}", 
                            event.getEventType(), event.getPolicyId(), 
                            ex != null ? ex.getMessage() : "Timeout occurred");
                    }
                });
        } catch (Exception e) {
            logger.warn("Error occurred while publishing event: {}. Continuing without Kafka.", 
                e.getMessage());
        }
    }
}