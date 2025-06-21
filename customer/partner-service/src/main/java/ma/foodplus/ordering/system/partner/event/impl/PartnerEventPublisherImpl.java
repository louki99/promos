package ma.foodplus.ordering.system.partner.event.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.partner.domain.Partner;
import ma.foodplus.ordering.system.partner.domain.PartnerType;
import ma.foodplus.ordering.system.partner.event.PartnerEvent;
import ma.foodplus.ordering.system.partner.event.PartnerEventPublisher;
import ma.foodplus.ordering.system.partner.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Kafka implementation of the Partner Event Publisher.
 * 
 * <p>This service publishes partner events to Kafka topics for integration
 * with other microservices in the FoodPlus ecosystem.</p>
 * 
 * @author FoodPlus Development Team
 * @version 3.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerEventPublisherImpl implements PartnerEventPublisher {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final PartnerRepository partnerRepository;
    
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
    
    @Override
    public void publishEvent(PartnerEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            String topic = determineTopic(event);
            String key = generateEventKey(event);
            
            log.info("Publishing event {} to topic {} with key {}", event.getEventType(), topic, key);
            
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, eventJson);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Event published successfully to topic {} partition {} offset {}", 
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to publish event to topic {}: {}", topic, ex.getMessage(), ex);
                }
            });
            
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event {}: {}", event.getEventType(), e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error publishing event {}: {}", event.getEventType(), e.getMessage(), e);
        }
    }
    
    @Override
    public void publishPartnerLifecycleEvent(PartnerEvent.EventType eventType, Long partnerId, Map<String, Object> partnerData) {
        PartnerEvent event = createBaseEvent(eventType, partnerId);
        if (partnerData != null) {
            event.setEventData(partnerData);
        }
        publishEvent(event);
    }
    
    @Override
    public void publishContractEvent(PartnerEvent.EventType eventType, Long partnerId, Map<String, Object> contractData) {
        PartnerEvent event = createBaseEvent(eventType, partnerId);
        if (contractData != null) {
            event.setEventData(contractData);
        }
        publishEvent(event);
    }
    
    @Override
    public void publishCreditEvent(PartnerEvent.EventType eventType, Long partnerId, Map<String, Object> creditData) {
        PartnerEvent event = createBaseEvent(eventType, partnerId);
        if (creditData != null) {
            event.setEventData(creditData);
        }
        publishEvent(event);
    }
    
    @Override
    public void publishLoyaltyEvent(PartnerEvent.EventType eventType, Long partnerId, Map<String, Object> loyaltyData) {
        PartnerEvent event = createBaseEvent(eventType, partnerId);
        if (loyaltyData != null) {
            event.setEventData(loyaltyData);
        }
        publishEvent(event);
    }
    
    @Override
    public void publishSupplierEvent(PartnerEvent.EventType eventType, Long partnerId, Map<String, Object> supplierData) {
        PartnerEvent event = createBaseEvent(eventType, partnerId);
        if (supplierData != null) {
            event.setEventData(supplierData);
        }
        publishEvent(event);
    }
    
    @Override
    public void publishGroupEvent(PartnerEvent.EventType eventType, Long partnerId, Map<String, Object> groupData) {
        PartnerEvent event = createBaseEvent(eventType, partnerId);
        if (groupData != null) {
            event.setEventData(groupData);
        }
        publishEvent(event);
    }
    
    @Override
    public void publishBusinessEvent(PartnerEvent.EventType eventType, Long partnerId, Map<String, Object> businessData) {
        PartnerEvent event = createBaseEvent(eventType, partnerId);
        if (businessData != null) {
            event.setEventData(businessData);
        }
        publishEvent(event);
    }
    
    /**
     * Creates a base event with partner information.
     * 
     * @param eventType the event type
     * @param partnerId the partner ID
     * @return the base event
     */
    private PartnerEvent createBaseEvent(PartnerEvent.EventType eventType, Long partnerId) {
        PartnerEvent event = PartnerEvent.create(eventType, partnerId);
        
        // Enrich event with partner information
        partnerRepository.findById(partnerId).ifPresent(partner -> {
            event.setCtNum(partner.getCtNum());
            event.setIce(partner.getIce());
            event.setPartnerType(partner.getPartnerType());
            event.setPartnerDescription(partner.getDescription());
        });
        
        return event;
    }
    
    /**
     * Determines the appropriate Kafka topic based on the event type.
     * 
     * @param event the partner event
     * @return the Kafka topic name
     */
    private String determineTopic(PartnerEvent event) {
        if (event.isContractEvent()) {
            return contractEventsTopic;
        } else if (event.isCreditEvent()) {
            return creditEventsTopic;
        } else if (event.isSupplierEvent()) {
            return supplierEventsTopic;
        } else {
            switch (event.getEventType()) {
                case LOYALTY_POINTS_ADDED:
                case LOYALTY_POINTS_REDEEMED:
                case VIP_STATUS_CHANGED:
                case LOYALTY_LEVEL_CHANGED:
                    return loyaltyEventsTopic;
                    
                case PARTNER_ADDED_TO_GROUP:
                case PARTNER_REMOVED_FROM_GROUP:
                case GROUP_CREATED:
                case GROUP_UPDATED:
                    return groupEventsTopic;
                    
                case ORDER_PLACED:
                case ORDER_CANCELLED:
                case MINIMUM_ORDER_NOT_MET:
                    return businessEventsTopic;
                    
                default:
                    return partnerEventsTopic;
            }
        }
    }
    
    /**
     * Generates a unique key for the Kafka message.
     * 
     * @param event the partner event
     * @return the event key
     */
    private String generateEventKey(PartnerEvent event) {
        return String.format("%s-%s-%s", 
                event.getPartnerType() != null ? event.getPartnerType().name() : "UNKNOWN",
                event.getPartnerId(),
                event.getEventType().name());
    }
} 