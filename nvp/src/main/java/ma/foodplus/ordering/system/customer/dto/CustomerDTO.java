package ma.foodplus.ordering.system.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import ma.foodplus.ordering.system.customer.model.CustomerType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class CustomerDTO {
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
    private CustomerType customerType;

    // Contact Information
    @Pattern(regexp = "^[0-9]{10}$", message = "Telephone must be exactly 10 digits")
    private String telephone;

    @Email(message = "Invalid email format")
    private String email;

    private String address;
    private String codePostal;
    private String ville;
    private String country;

    // B2B Specific Information
    private String companyName;
    private String legalForm;
    private String registrationNumber;
    private String taxId; //identifiant fiscal
    private String vatNumber; //numéro de TVA
    private String businessActivity; //activité commerciale
    private BigDecimal annualTurnover; //chiffre d'affaires annuel
    private Integer numberOfEmployees; //nombre d'employés

    // B2B Contract Information
    private String contractNumber; //numéro de contrat
    private ZonedDateTime contractStartDate; //date de début du contrat
    private ZonedDateTime contractEndDate; //date de fin du contrat
    private String contractType; //type de contrat (e.g., standard, custom)
    private String contractTerms; //conditions du contrat
    private String paymentTerms; //conditions de paiement
    private String deliveryTerms; //conditions de livraison
    private String specialConditions; //conditions spéciales

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
    @JsonProperty("isVip")
    private boolean isVip; //indique si le client est un VIP
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