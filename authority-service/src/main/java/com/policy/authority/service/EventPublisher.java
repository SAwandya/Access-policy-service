package com.policy.authority.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.policy.authority.event.PolicyEvent;

import java.util.concurrent.CompletableFuture;

@Service
public class EventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);
    
    private final KafkaTemplate<String, PolicyEvent> kafkaTemplate;
    
    @Value("${app.kafka.topics.policy-events:policy-events}")
    private String policyEventsTopic;
    
    public EventPublisher(KafkaTemplate<String, PolicyEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void publishEvent(PolicyEvent event) {
        try {
            CompletableFuture<SendResult<String, PolicyEvent>> future = 
                kafkaTemplate.send(policyEventsTopic, event.getPolicyId().toString(), event);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Published event [{}] for policy: {}", 
                        event.getEventType(), event.getPolicyId());
                } else {
                    logger.error("Failed to publish event [{}] for policy: {}", 
                        event.getEventType(), event.getPolicyId(), ex);
                }
            });
        } catch (Exception e) {
            logger.error("Error occurred while publishing event: {}", e.getMessage(), e);
        }
    }
}
