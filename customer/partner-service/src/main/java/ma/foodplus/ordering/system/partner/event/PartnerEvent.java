package ma.foodplus.ordering.system.partner.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.foodplus.ordering.system.partner.domain.PartnerType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Partner Event model for event-driven architecture.
 * 
 * <p>This class represents events that occur in the partner lifecycle and can be published
 * to Kafka for integration with other microservices in the FoodPlus ecosystem.</p>
 * 
 * @author FoodPlus Development Team
 * @version 3.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerEvent {
    
    /**
     * Event types that can be published by the partner service.
     */
    public enum EventType {
        // Partner lifecycle events
        PARTNER_CREATED,
        PARTNER_UPDATED,
        PARTNER_ACTIVATED,
        PARTNER_DEACTIVATED,
        PARTNER_DELETED,
        
        // Contract management events
        CONTRACT_CREATED,
        CONTRACT_RENEWED,
        CONTRACT_EXPIRED,
        CONTRACT_TERMINATED,
        
        // Credit management events
        CREDIT_LIMIT_UPDATED,
        PAYMENT_PROCESSED,
        PAYMENT_OVERDUE,
        CREDIT_RATING_CHANGED,
        
        // Loyalty events
        LOYALTY_POINTS_ADDED,
        LOYALTY_POINTS_REDEEMED,
        VIP_STATUS_CHANGED,
        LOYALTY_LEVEL_CHANGED,
        
        // Group management events
        PARTNER_ADDED_TO_GROUP,
        PARTNER_REMOVED_FROM_GROUP,
        GROUP_CREATED,
        GROUP_UPDATED,
        
        // Supplier-specific events
        SUPPLIER_PERFORMANCE_UPDATED,
        SUPPLIER_AUDIT_SCHEDULED,
        SUPPLIER_RISK_ASSESSMENT_CHANGED,
        SUPPLIER_STATUS_CHANGED,
        
        // Business events
        ORDER_PLACED,
        ORDER_CANCELLED,
        MINIMUM_ORDER_NOT_MET,
        CONTRACT_EXPIRING_SOON
    }
    
    /**
     * Event severity levels for monitoring and alerting.
     */
    public enum EventSeverity {
        INFO,       // Informational events
        WARNING,    // Warning events that need attention
        ERROR,      // Error events that need immediate attention
        CRITICAL    // Critical events that need immediate action
    }
    
    // Event metadata
    private String eventId;
    private EventType eventType;
    private EventSeverity severity;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime timestamp;
    
    private String source = "partner-service";
    private String version = "3.0.0";
    
    // Partner information
    private Long partnerId;
    private String ctNum;
    private String ice;
    private PartnerType partnerType;
    private String partnerDescription;
    
    // Event-specific data
    private Map<String, Object> eventData;
    
    // User context
    private String userId;
    private String userEmail;
    private String userRole;
    
    // Business context
    private String correlationId;
    private String businessUnit;
    private String environment;
    
    // Audit information
    private String ipAddress;
    private String userAgent;
    private String sessionId;
    
    /**
     * Creates a new partner event with default values.
     * 
     * @param eventType the type of event
     * @param partnerId the partner ID
     * @return a new partner event
     */
    public static PartnerEvent create(EventType eventType, Long partnerId) {
        return PartnerEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType(eventType)
                .severity(EventSeverity.INFO)
                .timestamp(ZonedDateTime.now())
                .partnerId(partnerId)
                .build();
    }
    
    /**
     * Creates a new partner event with severity.
     * 
     * @param eventType the type of event
     * @param severity the event severity
     * @param partnerId the partner ID
     * @return a new partner event
     */
    public static PartnerEvent create(EventType eventType, EventSeverity severity, Long partnerId) {
        return PartnerEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType(eventType)
                .severity(severity)
                .timestamp(ZonedDateTime.now())
                .partnerId(partnerId)
                .build();
    }
    
    /**
     * Adds event data to the event.
     * 
     * @param key the data key
     * @param value the data value
     * @return this event for chaining
     */
    public PartnerEvent withData(String key, Object value) {
        if (this.eventData == null) {
            this.eventData = new java.util.HashMap<>();
        }
        this.eventData.put(key, value);
        return this;
    }
    
    /**
     * Sets the user context for the event.
     * 
     * @param userId the user ID
     * @param userEmail the user email
     * @param userRole the user role
     * @return this event for chaining
     */
    public PartnerEvent withUserContext(String userId, String userEmail, String userRole) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userRole = userRole;
        return this;
    }
    
    /**
     * Sets the business context for the event.
     * 
     * @param correlationId the correlation ID
     * @param businessUnit the business unit
     * @return this event for chaining
     */
    public PartnerEvent withBusinessContext(String correlationId, String businessUnit) {
        this.correlationId = correlationId;
        this.businessUnit = businessUnit;
        return this;
    }
    
    /**
     * Checks if this is a critical event that requires immediate attention.
     * 
     * @return true if the event is critical
     */
    public boolean isCritical() {
        return EventSeverity.CRITICAL.equals(severity) || EventSeverity.ERROR.equals(severity);
    }
    
    /**
     * Checks if this is a contract-related event.
     * 
     * @return true if the event is contract-related
     */
    public boolean isContractEvent() {
        return eventType == EventType.CONTRACT_CREATED ||
               eventType == EventType.CONTRACT_RENEWED ||
               eventType == EventType.CONTRACT_EXPIRED ||
               eventType == EventType.CONTRACT_TERMINATED ||
               eventType == EventType.CONTRACT_EXPIRING_SOON;
    }
    
    /**
     * Checks if this is a credit-related event.
     * 
     * @return true if the event is credit-related
     */
    public boolean isCreditEvent() {
        return eventType == EventType.CREDIT_LIMIT_UPDATED ||
               eventType == EventType.PAYMENT_PROCESSED ||
               eventType == EventType.PAYMENT_OVERDUE ||
               eventType == EventType.CREDIT_RATING_CHANGED;
    }
    
    /**
     * Checks if this is a supplier-related event.
     * 
     * @return true if the event is supplier-related
     */
    public boolean isSupplierEvent() {
        return eventType == EventType.SUPPLIER_PERFORMANCE_UPDATED ||
               eventType == EventType.SUPPLIER_AUDIT_SCHEDULED ||
               eventType == EventType.SUPPLIER_RISK_ASSESSMENT_CHANGED ||
               eventType == EventType.SUPPLIER_STATUS_CHANGED;
    }
} 