package ma.foodplus.ordering.system.partner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * DTO for Contact Person operations.
 * 
 * <p>This DTO contains contact person information for creating and updating
 * contact persons associated with partners.</p>
 * 
 * @author FoodPlus Development Team
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactPersonDTO {
    
    private Long id;
    
    @NotBlank(message = "Contact person name is required")
    private String name;
    
    private String position;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]{8,20}$", message = "Invalid phone number format")
    private String phone;
    
    private Boolean preferred = false;
    
    private String notes;
    
    private Boolean active = true;
    
    private ZonedDateTime createdAt;
    
    private ZonedDateTime updatedAt;
    
    private String createdBy;
    
    private String updatedBy;
    
    private Long partnerId;
    
    /**
     * Checks if this contact person is the preferred contact.
     * 
     * @return true if this is the preferred contact
     */
    public boolean isPreferred() {
        return preferred != null && preferred;
    }
    
    /**
     * Checks if this contact person is active.
     * 
     * @return true if this contact person is active
     */
    public boolean isActive() {
        return active != null && active;
    }
} 