package ma.foodplus.ordering.system.partner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import ma.foodplus.ordering.system.partner.domain.PartnerType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Base DTO containing common attributes for all partner types.
 * 
 * <p>This class contains the shared attributes between B2B and B2C partners.
 * Specific partner types should extend this class and add their unique attributes.</p>
 * 
 * @author FoodPlus Development Team
 * @version 2.0.0
 */
@Data
public abstract class BasePartnerDTO {
    
    private Long id;

    @NotBlank(message = "CT number is required")
    @Pattern(regexp = "^[A-Z0-9]{1,20}$", message = "CT number must be alphanumeric and between 1-20 characters")
    private String ctNum;

    @NotBlank(message = "ICE is required")
    @Pattern(regexp = "^[0-9]{15}$", message = "ICE must be exactly 15 digits")
    private String ice;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Customer type is required")
    private PartnerType partnerType;

    // Contact Information
    @Pattern(regexp = "^[0-9]{10}$", message = "Telephone must be exactly 10 digits")
    private String telephone;

    @Email(message = "Invalid email format")
    private String email;

    private String address;
    private String codePostal;
    private String ville;
    private String country;

    // Financial Information
    @NotNull(message = "Category tariff ID is required")
    private Long categoryTarifId;

    private BigDecimal creditLimit; //limite de crédit
    private BigDecimal currentCredit; //crédit actuel
    private Integer paymentTermDays; //délai de paiement en jours
    private String creditRating; //évaluation de crédit (e.g., A, B, C)
    private Integer creditScore; //score de crédit (0-100)
    private String paymentHistory; //historique de paiement (e.g., good, average, poor)
    private BigDecimal outstandingBalance; //solde impayé
    private ZonedDateTime lastPaymentDate; //date du dernier paiement
    private String preferredPaymentMethod; //méthode de paiement préférée (e.g., bank transfer, cheque)
    private String bankAccountInfo; //informations sur le compte bancaire

    // Business Status
    @JsonProperty("vip")
    private boolean vip; //indique si le client est un VIP
    private Integer loyaltyPoints; //points de fidélité
    private ZonedDateTime lastOrderDate; //date de la dernière commande
    private Integer totalOrders; //nombre total de commandes
    private BigDecimal totalSpent; //montant total dépensé
    private BigDecimal averageOrderValue; //valeur moyenne des commandes
    private ZonedDateTime customerSince; //date depuis laquelle le client est actif
    private String preferredDeliveryTime; //heure de livraison préférée
    private String preferredDeliveryDays; //jours de livraison préférés
    private String specialHandlingInstructions; //instructions spéciales de manipulation

    // Audit
    private String notes; //notes ou commentaires sur le client
    private boolean active; //indique si le client est actif
    private ZonedDateTime lastActivityDate; //date de la dernière activité du client
    private String createdBy; //utilisateur qui a créé l'enregistrement
    private String updatedBy; //utilisateur qui a mis à jour l'enregistrement
    private ZonedDateTime createdAt; // date de création de l'enregistrement
    private ZonedDateTime updatedAt; // date de mise à jour de l'enregistrement
} 