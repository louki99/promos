package ma.foodplus.ordering.system.partner.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Supplier Partner entity representing supplier/fournisseur partners.
 * 
 * <p>This class extends the base Partner class and adds supplier-specific attributes
 * such as supplier information, performance metrics, and supply chain management features.</p>
 * 
 * @author FoodPlus Development Team
 * @version 3.0.0
 */
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("SUPPLIER")
public class SupplierPartner extends Partner {
    
    // Supplier-specific attributes
    @Column(name = "supplier_code", unique = true)
    private String supplierCode;
    
    @Column(name = "supplier_category")
    private String supplierCategory; // e.g., FOOD, BEVERAGE, PACKAGING, EQUIPMENT
    
    @Column(name = "supplier_rating")
    private String supplierRating; // e.g., A, B, C, D
    
    @Column(name = "delivery_performance_score")
    private BigDecimal deliveryPerformanceScore; // 0-100
    
    @Column(name = "quality_score")
    private BigDecimal qualityScore; // 0-100
    
    @Column(name = "price_competitiveness_score")
    private BigDecimal priceCompetitivenessScore; // 0-100
    
    @Column(name = "payment_terms_days")
    private Integer paymentTermsDays;
    
    @Column(name = "minimum_order_amount")
    private BigDecimal minimumOrderAmount;
    
    @Column(name = "lead_time_days")
    private Integer leadTimeDays;
    
    @Column(name = "certification_iso")
    private String certificationIso; // e.g., ISO9001, ISO22000, HACCP
    
    @Column(name = "supplier_since")
    private ZonedDateTime supplierSince;
    
    @Column(name = "last_audit_date")
    private ZonedDateTime lastAuditDate;
    
    @Column(name = "next_audit_date")
    private ZonedDateTime nextAuditDate;
    
    @Column(name = "supplier_status")
    private String supplierStatus; // e.g., ACTIVE, SUSPENDED, BLACKLISTED, PENDING_APPROVAL
    
    @Column(name = "risk_level")
    private String riskLevel; // e.g., LOW, MEDIUM, HIGH, CRITICAL
    
    @Column(name = "supplier_notes")
    private String supplierNotes;

    // Supplier-specific Business Methods
    @Override
    public PartnerType getPartnerType() {
        return PartnerType.SUPPLIER;
    }

    /**
     * Checks if the supplier partner has a valid contract.
     * 
     * @return true if the partner has a valid contract
     */
    public boolean hasValidContract() {
        if (getContractStartDate() == null || getContractEndDate() == null) {
            return false;
        }
        ZonedDateTime now = ZonedDateTime.now();
        return now.isAfter(getContractStartDate()) && now.isBefore(getContractEndDate());
    }

    /**
     * Checks if the contract is expiring soon.
     * 
     * @param daysThreshold the number of days to consider as "soon"
     * @return true if the contract expires within the threshold
     */
    public boolean isContractExpiringSoon(int daysThreshold) {
        if (getContractEndDate() == null) {
            return false;
        }
        
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime thresholdDate = now.plusDays(daysThreshold);
        
        return getContractEndDate().isBefore(thresholdDate) && 
               getContractEndDate().isAfter(now);
    }

    /**
     * Gets the overall supplier performance score.
     * 
     * @return the calculated performance score (0-100)
     */
    public BigDecimal getOverallPerformanceScore() {
        BigDecimal totalScore = BigDecimal.ZERO;
        int scoreCount = 0;
        
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
        
        return scoreCount > 0 ? totalScore.divide(BigDecimal.valueOf(scoreCount), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
    }

    /**
     * Checks if the supplier is due for an audit.
     * 
     * @return true if the supplier needs an audit
     */
    public boolean isDueForAudit() {
        if (nextAuditDate == null) {
            return false;
        }
        
        ZonedDateTime now = ZonedDateTime.now();
        return now.isAfter(nextAuditDate);
    }

    /**
     * Checks if the supplier can supply based on their status and performance.
     * 
     * @return true if the supplier can supply
     */
    @Override
    public boolean canPlaceOrder() {
        // Check if partner is active
        if (!isActive()) {
            return false;
        }
        
        // Check if supplier status allows supply
        if ("SUSPENDED".equals(supplierStatus) || "BLACKLISTED".equals(supplierStatus)) {
            return false;
        }
        
        // Check if contract is valid
        if (!hasValidContract()) {
            return false;
        }
        
        // Check if performance score is acceptable (above 60)
        BigDecimal performanceScore = getOverallPerformanceScore();
        if (performanceScore.compareTo(BigDecimal.valueOf(60)) < 0) {
            return false;
        }
        
        return true;
    }

    /**
     * Validates if the supplier partner meets all requirements.
     * 
     * @return true if the partner is valid
     */
    @Override
    public boolean isValid() {
        // Check basic requirements
        if (getCtNum() == null || getCtNum().trim().isEmpty()) {
            return false;
        }
        
        if (getIce() == null || getIce().trim().isEmpty()) {
            return false;
        }
        
        if (getDescription() == null || getDescription().trim().isEmpty()) {
            return false;
        }
        
        // Check supplier-specific requirements
        if (supplierCode == null || supplierCode.trim().isEmpty()) {
            return false;
        }
        
        if (supplierCategory == null || supplierCategory.trim().isEmpty()) {
            return false;
        }
        
        // Check company information
        if (getCompanyName() == null || getCompanyName().trim().isEmpty()) {
            return false;
        }
        
        // Check contract information
        if (getContractNumber() == null || getContractNumber().trim().isEmpty()) {
            return false;
        }
        
        if (getContractStartDate() == null || getContractEndDate() == null) {
            return false;
        }
        
        if (getContractEndDate().isBefore(getContractStartDate())) {
            return false;
        }
        
        return true;
    }

    /**
     * Gets a risk assessment based on supplier metrics.
     * 
     * @return risk assessment description
     */
    public String getRiskAssessment() {
        if ("BLACKLISTED".equals(supplierStatus)) {
            return "CRITICAL - Supplier is blacklisted";
        }
        
        if ("SUSPENDED".equals(supplierStatus)) {
            return "HIGH - Supplier is suspended";
        }
        
        BigDecimal performanceScore = getOverallPerformanceScore();
        if (performanceScore.compareTo(BigDecimal.valueOf(40)) < 0) {
            return "HIGH - Very low performance score";
        } else if (performanceScore.compareTo(BigDecimal.valueOf(60)) < 0) {
            return "MEDIUM - Below average performance";
        } else if (performanceScore.compareTo(BigDecimal.valueOf(80)) < 0) {
            return "LOW - Good performance";
        } else {
            return "VERY LOW - Excellent performance";
        }
    }
} 