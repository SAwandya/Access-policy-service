package com.policy.authority.component;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class KafkaConnectionHealthCheck {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaConnectionHealthCheck.class);
    
    private final KafkaAdmin kafkaAdmin;
    
    @Value("${app.kafka.enabled:true}")
    private boolean kafkaEnabled;
    
    public KafkaConnectionHealthCheck(KafkaAdmin kafkaAdmin) {
        this.kafkaAdmin = kafkaAdmin;
    }
    
    @EventListener(ApplicationStartedEvent.class)
    public void checkKafkaConnection() {
        if (!kafkaEnabled) {
            logger.info("Kafka is disabled. Skipping connection check.");
            return;
        }
        
        logger.info("Checking Kafka connection...");
        
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            DescribeClusterResult clusterDescription = adminClient.describeCluster(new DescribeClusterOptions().timeoutMs(5000));
            
            String clusterId = clusterDescription.clusterId().get(5, TimeUnit.SECONDS);
            int nodeCount = clusterDescription.nodes().get(5, TimeUnit.SECONDS).size();
            String controllerId = clusterDescription.controller().get(5, TimeUnit.SECONDS).idString();
            
            logger.info("✅ Successfully connected to Kafka cluster:");
            logger.info("   - Cluster ID: {}", clusterId);
            logger.info("   - Number of brokers: {}", nodeCount);
            logger.info("   - Controller ID: {}", controllerId);
            
        } catch (Exception e) {
            logger.error("❌ Failed to connect to Kafka: {}", e.getMessage());
            logger.error("Kafka connection details: {}", kafkaAdmin.getConfigurationProperties().get("bootstrap.servers"));
            logger.error("Stack trace:", e);
        }
    }
}
