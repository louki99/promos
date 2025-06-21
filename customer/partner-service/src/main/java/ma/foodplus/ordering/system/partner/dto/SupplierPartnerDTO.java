package ma.foodplus.ordering.system.partner.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ma.foodplus.ordering.system.partner.domain.PartnerType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * DTO for Supplier Partner operations.
 * 
 * <p>This DTO contains all supplier-specific fields and validation rules
 * for creating and updating supplier partners in the system.</p>
 * 
 * @author FoodPlus Development Team
 * @version 3.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierPartnerDTO extends BasePartnerDTO {
    
    @NotBlank(message = "Supplier code is required")
    @Pattern(regexp = "^[A-Z0-9]{3,10}$", message = "Supplier code must be 3-10 alphanumeric characters")
    private String supplierCode;
    
    @NotBlank(message = "Supplier category is required")
    @Pattern(regexp = "^(FOOD|BEVERAGE|PACKAGING|EQUIPMENT|SERVICES|OTHER)$", 
            message = "Supplier category must be one of: FOOD, BEVERAGE, PACKAGING, EQUIPMENT, SERVICES, OTHER")
    private String supplierCategory;
    
    @Pattern(regexp = "^[A-D]$", message = "Supplier rating must be A, B, C, or D")
    private String supplierRating;
    
    @DecimalMin(value = "0.0", message = "Delivery performance score must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Delivery performance score must be between 0 and 100")
    private BigDecimal deliveryPerformanceScore;
    
    @DecimalMin(value = "0.0", message = "Quality score must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Quality score must be between 0 and 100")
    private BigDecimal qualityScore;
    
    @DecimalMin(value = "0.0", message = "Price competitiveness score must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Price competitiveness score must be between 0 and 100")
    private BigDecimal priceCompetitivenessScore;
    
    @Min(value = 0, message = "Payment terms days must be non-negative")
    @Max(value = 365, message = "Payment terms days cannot exceed 365")
    private Integer paymentTermsDays;
    
    @DecimalMin(value = "0.0", message = "Minimum order amount must be non-negative")
    private BigDecimal minimumOrderAmount;
    
    @Min(value = 1, message = "Lead time days must be at least 1")
    @Max(value = 365, message = "Lead time days cannot exceed 365")
    private Integer leadTimeDays;
    
    @Pattern(regexp = "^(ISO9001|ISO22000|HACCP|BRC|FSSC22000|OTHER|)$", 
            message = "Certification must be one of: ISO9001, ISO22000, HACCP, BRC, FSSC22000, OTHER")
    private String certificationIso;
    
    private ZonedDateTime supplierSince;
    
    private ZonedDateTime lastAuditDate;
    
    private ZonedDateTime nextAuditDate;
    
    @Pattern(regexp = "^(ACTIVE|SUSPENDED|BLACKLISTED|PENDING_APPROVAL)$", 
            message = "Supplier status must be one of: ACTIVE, SUSPENDED, BLACKLISTED, PENDING_APPROVAL")
    private String supplierStatus = "PENDING_APPROVAL";
    
    @Pattern(regexp = "^(LOW|MEDIUM|HIGH|CRITICAL)$", 
            message = "Risk level must be one of: LOW, MEDIUM, HIGH, CRITICAL")
    private String riskLevel = "MEDIUM";
    
    @Size(max = 1000, message = "Supplier notes cannot exceed 1000 characters")
    private String supplierNotes;
    
    // Company Information (inherited from BasePartnerDTO but with supplier-specific validation)
    @NotBlank(message = "Company name is required for suppliers")
    @Size(max = 255, message = "Company name cannot exceed 255 characters")
    private String companyName;
    
    @Size(max = 100, message = "Legal form cannot exceed 100 characters")
    private String legalForm;
    
    @Size(max = 100, message = "Registration number cannot exceed 100 characters")
    private String registrationNumber;
    
    @Size(max = 100, message = "Tax ID cannot exceed 100 characters")
    private String taxId;
    
    @Size(max = 100, message = "VAT number cannot exceed 100 characters")
    private String vatNumber;
    
    @Size(max = 500, message = "Business activity cannot exceed 500 characters")
    private String businessActivity;
    
    @DecimalMin(value = "0.0", message = "Annual turnover must be non-negative")
    private BigDecimal annualTurnover;
    
    @Min(value = 1, message = "Number of employees must be at least 1")
    @Max(value = 100000, message = "Number of employees cannot exceed 100,000")
    private Integer numberOfEmployees;
    
    // Contract Information (inherited from BasePartnerDTO but with supplier-specific validation)
    @NotBlank(message = "Contract number is required for suppliers")
    @Size(max = 100, message = "Contract number cannot exceed 100 characters")
    private String contractNumber;
    
    @NotNull(message = "Contract start date is required for suppliers")
    private ZonedDateTime contractStartDate;
    
    @NotNull(message = "Contract end date is required for suppliers")
    private ZonedDateTime contractEndDate;
    
    @Size(max = 100, message = "Contract type cannot exceed 100 characters")
    private String contractType;
    
    @Size(max = 1000, message = "Contract terms cannot exceed 1000 characters")
    private String contractTerms;
    
    @Size(max = 500, message = "Payment terms cannot exceed 500 characters")
    private String paymentTerms;
    
    @Size(max = 500, message = "Delivery terms cannot exceed 500 characters")
    private String deliveryTerms;
    
    @Size(max = 1000, message = "Special conditions cannot exceed 1000 characters")
    private String specialConditions;
    
    /**
     * Gets the partner type for suppliers.
     * 
     * @return SUPPLIER partner type
     */
    @Override
    public PartnerType getPartnerType() {
        return PartnerType.SUPPLIER;
    }
    
    /**
     * Validates that contract end date is after start date.
     * 
     * @return true if contract dates are valid
     */
    public boolean isValidContractDates() {
        if (contractStartDate == null || contractEndDate == null) {
            return false;
        }
        return contractEndDate.isAfter(contractStartDate);
    }
    
    /**
     * Validates that next audit date is in the future.
     * 
     * @return true if next audit date is valid
     */
    public boolean isValidNextAuditDate() {
        if (nextAuditDate == null) {
            return true; // Optional field
        }
        return nextAuditDate.isAfter(ZonedDateTime.now());
    }
    
    /**
     * Calculates the overall performance score.
     * 
     * @return the calculated performance score or null if insufficient data
     */
    public BigDecimal getCalculatedPerformanceScore() {
        int scoreCount = 0;
        BigDecimal totalScore = BigDecimal.ZERO;
        
        if (deliveryPerformanceScore != null) {
            totalScore = totalScore.add(deliveryPerformanceScore);
            scoreCount++;
        }
        
        if (qualityScore != null) {
            totalScore = totalScore.add(qualityScore);
            scoreCount++;
        }
        
        if (priceCompetitivenessScore != null) {
            totalScore = totalScore.add(priceCompetitivenessScore);
            scoreCount++;
        }
        
        if (scoreCount > 0) {
            return totalScore.divide(BigDecimal.valueOf(scoreCount), 2, BigDecimal.ROUND_HALF_UP);
        }
        
        return null;
    }
} 