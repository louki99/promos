package ma.foodplus.ordering.system.partner.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.foodplus.ordering.system.partner.domain.PartnerInteraction;

import java.time.ZonedDateTime;

/**
 * DTO for Partner Interaction operations.
 * 
 * <p>This DTO contains interaction information for creating and viewing
 * partner interactions and audit logs.</p>
 * 
 * @author FoodPlus Development Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerInteractionDTO {
    
    private Long id;
    
    @NotBlank(message = "User is required")
    private String user;
    
    @NotBlank(message = "Action is required")
    private String action;
    
    private String details;
    
    private String ipAddress;
    
    private String userAgent;
    
    private String sessionId;
    
    private PartnerInteraction.InteractionType interactionType = PartnerInteraction.InteractionType.GENERAL;
    
    private PartnerInteraction.InteractionSeverity severity = PartnerInteraction.InteractionSeverity.INFO;
    
    private ZonedDateTime timestamp;
    
    private Long partnerId;
    
    /**
     * Checks if this interaction is of a specific type.
     * 
     * @param type the interaction type to check
     * @return true if this interaction is of the specified type
     */
    public boolean isOfType(PartnerInteraction.InteractionType type) {
        return this.interactionType == type;
    }
    
    /**
     * Checks if this interaction has a specific severity level.
     * 
     * @param severity the severity level to check
     * @return true if this interaction has the specified severity
     */
    public boolean hasSeverity(PartnerInteraction.InteractionSeverity severity) {
        return this.severity == severity;
    }
    
    /**
     * Checks if this interaction is an error or critical.
     * 
     * @return true if this interaction is an error or critical
     */
    public boolean isErrorOrCritical() {
        return this.severity == PartnerInteraction.InteractionSeverity.ERROR || 
               this.severity == PartnerInteraction.InteractionSeverity.CRITICAL;
    }
} 