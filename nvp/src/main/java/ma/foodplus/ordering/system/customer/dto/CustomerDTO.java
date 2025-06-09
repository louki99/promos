package ma.foodplus.ordering.system.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @Pattern(regexp = "^[0-9]{10}$", message = "Telephone must be exactly 10 digits")
    private String telephone;

    @Email(message = "Invalid email format")
    private String email;

    private String address;
    private String codePostal;
    private String ville;
    private String country;
    private String taxId;
    private String registrationNumber;
    private String notes;

    @NotNull(message = "Category tariff ID is required")
    private Long categoryTarifId;

    private BigDecimal maxCredit;
    private BigDecimal currentCredit;
    private Integer paymentTermDays;
    private boolean isVip;
    private Integer loyaltyPoints;
    private LocalDateTime lastOrderDate;
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 