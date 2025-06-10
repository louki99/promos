package ma.foodplus.ordering.system.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import ma.foodplus.ordering.system.customer.model.CustomerType;

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
    private String taxId;
    private String vatNumber;
    private String businessActivity;
    private BigDecimal annualTurnover;
    private Integer numberOfEmployees;

    // B2B Contract Information
    private String contractNumber;
    private ZonedDateTime contractStartDate;
    private ZonedDateTime contractEndDate;
    private String contractType;
    private String contractTerms;
    private String paymentTerms;
    private String deliveryTerms;
    private String specialConditions;

    // Financial Information
    @NotNull(message = "Category tariff ID is required")
    private Long categoryTarifId;

    private BigDecimal creditLimit;
    private BigDecimal currentCredit;
    private Integer paymentTermDays;
    private String creditRating;
    private Integer creditScore;
    private String paymentHistory;
    private BigDecimal outstandingBalance;
    private ZonedDateTime lastPaymentDate;
    private String preferredPaymentMethod;
    private String bankAccountInfo;

    // Business Status
    private boolean isVip;
    private Integer loyaltyPoints;
    private ZonedDateTime lastOrderDate;
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private BigDecimal averageOrderValue;
    private ZonedDateTime customerSince;
    private String preferredDeliveryTime;
    private String preferredDeliveryDays;
    private String specialHandlingInstructions;

    // Audit
    private String notes;
    private boolean active;
    private ZonedDateTime lastActivityDate;
    private String createdBy;
    private String updatedBy;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
} 