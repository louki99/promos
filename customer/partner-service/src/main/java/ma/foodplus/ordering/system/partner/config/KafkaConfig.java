package ma.foodplus.ordering.system.partner.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka configuration for the Partner Service.
 * 
 * <p>This configuration sets up Kafka producer and topics for event publishing
 * to enable integration with other microservices in the FoodPlus ecosystem.</p>
 * 
 * @author FoodPlus Development Team
 * @version 3.0.0
 */
@Slf4j
@Configuration
public class KafkaConfig {
    
    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;
    
    @Value("${kafka.topics.partner-events:partner-events}")
    private String partnerEventsTopic;
    
    @Value("${kafka.topics.contract-events:contract-events}")
    private String contractEventsTopic;
    
    @Value("${kafka.topics.credit-events:credit-events}")
    private String creditEventsTopic;
    
    @Value("${kafka.topics.loyalty-events:loyalty-events}")
    private String loyaltyEventsTopic;
    
    @Value("${kafka.topics.supplier-events:supplier-events}")
    private String supplierEventsTopic;
    
    @Value("${kafka.topics.group-events:group-events}")
    private String groupEventsTopic;
    
    @Value("${kafka.topics.business-events:business-events}")
    private String businessEventsTopic;
    
    /**
     * Creates the Kafka producer factory with proper serialization.
     * 
     * @return the producer factory
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    
    /**
     * Creates the Kafka template for sending messages.
     * 
     * @return the Kafka template
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
    /**
     * Creates the ObjectMapper for JSON serialization.
     * 
     * @return the ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
    
    /**
     * Creates the partner events topic.
     * 
     * @return the partner events topic
     */
    @Bean
    public NewTopic partnerEventsTopic() {
        return TopicBuilder.name(partnerEventsTopic)
                .partitions(3)
                .replicas(1)
                .configs(Map.of("retention.ms", "604800000")) // 7 days
                .build();
    }
    
    /**
     * Creates the contract events topic.
     * 
     * @return the contract events topic
     */
    @Bean
    public NewTopic contractEventsTopic() {
        return TopicBuilder.name(contractEventsTopic)
                .partitions(3)
                .replicas(1)
                .configs(Map.of("retention.ms", "2592000000")) // 30 days
                .build();
    }
    
    /**
     * Creates the credit events topic.
     * 
     * @return the credit events topic
     */
    @Bean
    public NewTopic creditEventsTopic() {
        return TopicBuilder.name(creditEventsTopic)
                .partitions(3)
                .replicas(1)
                .configs(Map.of("retention.ms", "2592000000")) // 30 days
                .build();
    }
    
    /**
     * Creates the loyalty events topic.
     * 
     * @return the loyalty events topic
     */
    @Bean
    public NewTopic loyaltyEventsTopic() {
        return TopicBuilder.name(loyaltyEventsTopic)
                .partitions(3)
                .replicas(1)
                .configs(Map.of("retention.ms", "604800000")) // 7 days
                .build();
    }
    
    /**
     * Creates the supplier events topic.
     * 
     * @return the supplier events topic
     */
    @Bean
    public NewTopic supplierEventsTopic() {
        return TopicBuilder.name(supplierEventsTopic)
                .partitions(3)
                .replicas(1)
                .configs(Map.of("retention.ms", "2592000000")) // 30 days
                .build();
    }
    
    /**
     * Creates the group events topic.
     * 
     * @return the group events topic
     */
    @Bean
    public NewTopic groupEventsTopic() {
        return TopicBuilder.name(groupEventsTopic)
                .partitions(3)
                .replicas(1)
                .configs(Map.of("retention.ms", "604800000")) // 7 days
                .build();
    }
    
    /**
     * Creates the business events topic.
     * 
     * @return the business events topic
     */
    @Bean
    public NewTopic businessEventsTopic() {
        return TopicBuilder.name(businessEventsTopic)
                .partitions(3)
                .replicas(1)
                .configs(Map.of("retention.ms", "604800000")) // 7 days
                .build();
    }
} 