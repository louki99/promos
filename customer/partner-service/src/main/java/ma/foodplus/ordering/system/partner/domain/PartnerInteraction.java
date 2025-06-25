package ma.foodplus.ordering.system.partner.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

/**
 * Partner Interaction entity representing user interactions and logs with partners.
 * 
 * <p>This entity tracks all user interactions, actions, and changes made to partners
 * for audit and tracking purposes.</p>
 * 
 * @author FoodPlus Development Team
 * @version 1.0.0
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "partner_interactions")
public class PartnerInteraction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "User is required")
    @Column(name = "user_name", nullable = false)
    private String user;
    
    @NotBlank(message = "Action is required")
    @Column(name = "action", nullable = false)
    private String action;
    
    @Column(name = "details", columnDefinition = "TEXT")
    private String details;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "session_id")
    private String sessionId;
    
    @Column(name = "interaction_type")
    @Enumerated(EnumType.STRING)
    private InteractionType interactionType = InteractionType.GENERAL;
    
    @Column(name = "severity")
    @Enumerated(EnumType.STRING)
    private InteractionSeverity severity = InteractionSeverity.INFO;
    
    @CreationTimestamp
    @Column(name = "timestamp", nullable = false, updatable = false)
    private ZonedDateTime timestamp;
    
    // Relationship with Partner
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;
    
    /**
     * Interaction types for categorization.
     */
    public enum InteractionType {
        CREATE("Create"),
        UPDATE("Update"),
        DELETE("Delete"),
        VIEW("View"),
        LOGIN("Login"),
        LOGOUT("Logout"),
        CREDIT_UPDATE("Credit Update"),
        CONTRACT_UPDATE("Contract Update"),
        STATUS_CHANGE("Status Change"),
        DOCUMENT_UPLOAD("Document Upload"),
        DOCUMENT_VERIFY("Document Verification"),
        CONTACT_ADD("Contact Added"),
        CONTACT_UPDATE("Contact Updated"),
        CONTACT_REMOVE("Contact Removed"),
        NOTE_ADD("Note Added"),
        NOTE_UPDATE("Note Updated"),
        NOTE_DELETE("Note Deleted"),
        GENERAL("General");
        
        private final String displayName;
        
        InteractionType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Interaction severity levels.
     */
    public enum InteractionSeverity {
        DEBUG("Debug"),
        INFO("Information"),
        WARNING("Warning"),
        ERROR("Error"),
        CRITICAL("Critical");
        
        private final String displayName;
        
        InteractionSeverity(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Creates a new interaction with the current timestamp.
     * 
     * @param user the user performing the action
     * @param action the action performed
     * @param partner the partner involved
     * @return a new PartnerInteraction instance
     */
    public static PartnerInteraction create(String user, String action, Partner partner) {
        return PartnerInteraction.builder()
                .user(user)
                .action(action)
                .partner(partner)
                .timestamp(ZonedDateTime.now())
                .build();
    }
    
    /**
     * Creates a new interaction with details.
     * 
     * @param user the user performing the action
     * @param action the action performed
     * @param details additional details about the action
     * @param partner the partner involved
     * @return a new PartnerInteraction instance
     */
    public static PartnerInteraction create(String user, String action, String details, Partner partner) {
        return PartnerInteraction.builder()
                .user(user)
                .action(action)
                .details(details)
                .partner(partner)
                .timestamp(ZonedDateTime.now())
                .build();
    }
    
    /**
     * Creates a new interaction with full details.
     * 
     * @param user the user performing the action
     * @param action the action performed
     * @param details additional details about the action
     * @param interactionType the type of interaction
     * @param severity the severity level
     * @param partner the partner involved
     * @return a new PartnerInteraction instance
     */
    public static PartnerInteraction create(String user, String action, String details, 
                                          InteractionType interactionType, InteractionSeverity severity, 
                                          Partner partner) {
        return PartnerInteraction.builder()
                .user(user)
                .action(action)
                .details(details)
                .interactionType(interactionType)
                .severity(severity)
                .partner(partner)
                .timestamp(ZonedDateTime.now())
                .build();
    }
    
    /**
     * Checks if this interaction is of a specific type.
     * 
     * @param type the interaction type to check
     * @return true if this interaction is of the specified type
     */
    public boolean isOfType(InteractionType type) {
        return this.interactionType == type;
    }
    
    /**
     * Checks if this interaction has a specific severity level.
     * 
     * @param severity the severity level to check
     * @return true if this interaction has the specified severity
     */
    public boolean hasSeverity(InteractionSeverity severity) {
        return this.severity == severity;
    }
    
    /**
     * Checks if this interaction is an error or critical.
     * 
     * @return true if this interaction is an error or critical
     */
    public boolean isErrorOrCritical() {
        return this.severity == InteractionSeverity.ERROR || 
               this.severity == InteractionSeverity.CRITICAL;
    }
} 