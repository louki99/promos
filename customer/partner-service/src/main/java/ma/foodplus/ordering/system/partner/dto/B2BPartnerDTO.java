package ma.foodplus.ordering.system.partner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * DTO for B2B Partner containing business-specific attributes.
 * 
 * <p>This DTO extends BasePartnerDTO and adds B2B-specific fields such as
 * company information, contract details, and business metrics.</p>
 * 
 * @author FoodPlus Development Team
 * @version 2.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class B2BPartnerDTO extends BasePartnerDTO {
    
    // B2B Specific Information
    @NotBlank(message = "Company name is required for B2B partners")
    private String companyName;
    
    private String legalForm;
    private String registrationNumber;
    private String taxId; //identifiant fiscal
    private String vatNumber; //numéro de TVA
    private String businessActivity; //activité commerciale
    private BigDecimal annualTurnover; //chiffre d'affaires annuel
    private Integer numberOfEmployees; //nombre d'employés

    // B2B Contract Information
    @NotBlank(message = "Contract number is required for B2B partners")
    private String contractNumber; //numéro de contrat
    
    @NotNull(message = "Contract start date is required for B2B partners")
    private ZonedDateTime contractStartDate; //date de début du contrat
    
    @NotNull(message = "Contract end date is required for B2B partners")
    private ZonedDateTime contractEndDate; //date de fin du contrat
    
    private String contractType; //type de contrat (e.g., standard, custom)
    private String contractTerms; //conditions du contrat
    private String paymentTerms; //conditions de paiement
    private String deliveryTerms; //conditions de livraison
    private String specialConditions; //conditions spéciales
} 