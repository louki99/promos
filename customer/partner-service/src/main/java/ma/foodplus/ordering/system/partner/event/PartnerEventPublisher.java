package ma.foodplus.ordering.system.partner.event;

/**
 * Interface for publishing partner events to Kafka.
 * 
 * <p>This interface defines methods for publishing various types of partner events
 * to Kafka topics for integration with other microservices in the FoodPlus ecosystem.</p>
 * 
 * @author FoodPlus Development Team
 * @version 3.0.0
 */
public interface PartnerEventPublisher {
    
    /**
     * Publishes a partner event to the appropriate Kafka topic.
     * 
     * @param event the partner event to publish
     */
    void publishEvent(PartnerEvent event);
    
    /**
     * Publishes a partner lifecycle event.
     * 
     * @param eventType the type of lifecycle event
     * @param partnerId the partner ID
     * @param partnerData additional partner data
     */
    void publishPartnerLifecycleEvent(PartnerEvent.EventType eventType, Long partnerId, java.util.Map<String, Object> partnerData);
    
    /**
     * Publishes a contract management event.
     * 
     * @param eventType the type of contract event
     * @param partnerId the partner ID
     * @param contractData additional contract data
     */
    void publishContractEvent(PartnerEvent.EventType eventType, Long partnerId, java.util.Map<String, Object> contractData);
    
    /**
     * Publishes a credit management event.
     * 
     * @param eventType the type of credit event
     * @param partnerId the partner ID
     * @param creditData additional credit data
     */
    void publishCreditEvent(PartnerEvent.EventType eventType, Long partnerId, java.util.Map<String, Object> creditData);
    
    /**
     * Publishes a loyalty event.
     * 
     * @param eventType the type of loyalty event
     * @param partnerId the partner ID
     * @param loyaltyData additional loyalty data
     */
    void publishLoyaltyEvent(PartnerEvent.EventType eventType, Long partnerId, java.util.Map<String, Object> loyaltyData);
    
    /**
     * Publishes a supplier-specific event.
     * 
     * @param eventType the type of supplier event
     * @param partnerId the partner ID
     * @param supplierData additional supplier data
     */
    void publishSupplierEvent(PartnerEvent.EventType eventType, Long partnerId, java.util.Map<String, Object> supplierData);
    
    /**
     * Publishes a group management event.
     * 
     * @param eventType the type of group event
     * @param partnerId the partner ID
     * @param groupData additional group data
     */
    void publishGroupEvent(PartnerEvent.EventType eventType, Long partnerId, java.util.Map<String, Object> groupData);
    
    /**
     * Publishes a business event.
     * 
     * @param eventType the type of business event
     * @param partnerId the partner ID
     * @param businessData additional business data
     */
    void publishBusinessEvent(PartnerEvent.EventType eventType, Long partnerId, java.util.Map<String, Object> businessData);
} 