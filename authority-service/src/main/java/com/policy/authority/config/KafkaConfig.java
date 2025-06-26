package com.policy.authority.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.support.ProducerListener;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.policy.authority.event.PolicyEvent;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class KafkaConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);
    
    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;
    
    @Bean
    public ProducerFactory<String, PolicyEvent> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    
    @Bean
    public KafkaTemplate<String, PolicyEvent> kafkaTemplate() {
        logger.info("Configuring Kafka with bootstrap servers: {}", bootstrapServers);
        KafkaTemplate<String, PolicyEvent> template = new KafkaTemplate<>(producerFactory());
        
        // Add a listener to check Kafka connection on startup
        template.setProducerListener(new ProducerListener<String, PolicyEvent>() {
            @Override
            public void onSuccess(ProducerRecord<String, PolicyEvent> record, RecordMetadata metadata) {
                logger.info("Kafka connection test successful. Topic: {}, Partition: {}, Offset: {}",
                    metadata.topic(), metadata.partition(), metadata.offset());
            }
            
            @Override
            public void onError(ProducerRecord<String, PolicyEvent> record, RecordMetadata metadata, Exception exception) {
                logger.error("Kafka connection test failed: {}", exception.getMessage(), exception);
            }
        });
        
        return template;
    }
}
