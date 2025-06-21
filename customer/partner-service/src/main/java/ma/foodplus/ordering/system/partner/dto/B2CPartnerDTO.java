package ma.foodplus.ordering.system.partner.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO for B2C Partner containing consumer-specific attributes.
 * 
 * <p>This DTO extends BasePartnerDTO and adds B2C-specific fields such as
 * personal information, preferences, and marketing consent.</p>
 * 
 * @author FoodPlus Development Team
 * @version 2.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class B2CPartnerDTO extends BasePartnerDTO {
    
    // B2C-specific attributes
    @NotBlank(message = "Personal ID number is required for B2C partners")
    private String personalIdNumber;
    
    private String dateOfBirth;
    private String preferredLanguage;
    private Boolean marketingConsent = false;
} 