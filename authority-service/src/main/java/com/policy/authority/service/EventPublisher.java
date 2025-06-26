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
    
    @Value("${app.kafka.topics.policy-events:policy-events}")
    private String policyEventsTopic;
    
    @Value("${app.kafka.enabled:false}")
    private boolean isKafkaEnabled;
    
    @Value("${app.kafka.timeout.ms:1000}")
    private long kafkaTimeoutMs;
    
    public EventPublisher(KafkaTemplate<String, PolicyEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaEnabled = isKafkaEnabled;
    }
    
    public void publishEvent(PolicyEvent event) {
        if (!kafkaEnabled) {
            logger.info("Kafka publishing disabled. Event {} logged but not published.", event.getEventType());
            return;
        }
        
        try {
            CompletableFuture<SendResult<String, PolicyEvent>> future = 
                kafkaTemplate.send(policyEventsTopic, event.getPolicyId().toString(), event);
            
            // Add timeout to avoid hanging
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
