package ma.foodplus.ordering.system.partner.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract base class for all partners in the system.
 * 
 * <p>This class contains common attributes and behavior shared by both B2B and B2C partners.
 * The inheritance strategy uses SINGLE_TABLE for better performance and simpler queries.</p>
 * 
 * @author FoodPlus Development Team
 * @version 2.0.0
 */
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "partner_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "partners")
public abstract class Partner {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ct_num", nullable = false, unique = true)
    private String ctNum;

    @Column(name = "ice", nullable = false)
    private String ice;

    @Column(nullable = false)
    private String description;

    // Contact Information (Individual columns to match database)
    @Column(name = "telephone")
    private String telephone;
    
    @Column(name = "telecopie")
    private String telecopie;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "region")
    private String region;
    
    @Column(name = "postal_code")
    private String postalCode;
    
    // Credit Information (Individual columns to match database)
    @Column(name = "credit_limit")
    private BigDecimal creditLimit;
    
    @Column(name = "credit_rating")
    private String creditRating;
    
    @Column(name = "credit_score")
    private Integer creditScore;
    
    @Column(name = "payment_history")
    private String paymentHistory;
    
    @Column(name = "outstanding_balance")
    private BigDecimal outstandingBalance;
    
    @Column(name = "last_payment_date")
    private ZonedDateTime lastPaymentDate;
    
    @Column(name = "payment_term_days")
    private Integer paymentTermDays;
    
    @Column(name = "preferred_payment_method")
    private String preferredPaymentMethod;
    
    @Column(name = "bank_account_info")
    private String bankAccountInfo;
    
    // Loyalty Information (Individual columns to match database)
    @Column(name = "is_vip")
    private Boolean isVip;
    
    @Column(name = "loyalty_points")
    private Integer loyaltyPoints;
    
    @Column(name = "total_orders")
    private Integer totalOrders;
    
    @Column(name = "total_spent")
    private BigDecimal totalSpent;
    
    @Column(name = "average_order_value")
    private BigDecimal averageOrderValue;
    
    @Column(name = "last_order_date")
    private ZonedDateTime lastOrderDate;
    
    @Column(name = "partner_since")
    private ZonedDateTime partnerSince;
    
    // Delivery Preference (Individual columns to match database)
    @Column(name = "preferred_delivery_time")
    private String preferredDeliveryTime;
    
    @Column(name = "preferred_delivery_days")
    private String preferredDeliveryDays;
    
    @Column(name = "special_handling_instructions")
    private String specialHandlingInstructions;
    
    // Audit Information (Individual columns to match database)
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "last_activity_date")
    private ZonedDateTime lastActivityDate;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // Company Information (Individual columns to match database)
    @Column(name = "company_name")
    private String companyName;
    
    @Column(name = "legal_form")
    private String legalForm;
    
    @Column(name = "registration_number")
    private String registrationNumber;
    
    @Column(name = "tax_id")
    private String taxId;
    
    @Column(name = "vat_number")
    private String vatNumber;
    
    @Column(name = "business_activity")
    private String businessActivity;
    
    @Column(name = "annual_turnover")
    private BigDecimal annualTurnover;
    
    @Column(name = "number_of_employees")
    private Integer numberOfEmployees;
    
    // Contract Information (Individual columns to match database)
    @Column(name = "contract_number")
    private String contractNumber;
    
    @Column(name = "contract_start_date")
    private ZonedDateTime contractStartDate;
    
    @Column(name = "contract_end_date")
    private ZonedDateTime contractEndDate;
    
    @Column(name = "contract_type")
    private String contractType;
    
    @Column(name = "contract_terms")
    private String contractTerms;
    
    @Column(name = "payment_terms")
    private String paymentTerms;
    
    @Column(name = "delivery_terms")
    private String deliveryTerms;
    
    @Column(name = "special_conditions")
    private String specialConditions;

    // Partner Groups (Many-to-Many relationship)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "partner_group_members",
        joinColumns = @JoinColumn(name = "partner_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<PartnerGroup> partnerGroups = new HashSet<>();

    @Column(name = "category_tarif_id")
    private Long cateTarif;

    // Abstract methods that must be implemented by subclasses
    public abstract PartnerType getPartnerType();
    
    /**
     * Checks if the partner can place an order based on their type-specific rules.
     * 
     * @return true if the partner can place an order
     */
    public abstract boolean canPlaceOrder();
    
    /**
     * Validates if the partner meets all requirements for their type.
     * 
     * @return true if the partner is valid
     */
    public abstract boolean isValid();

    // Common Business Methods
    public boolean isB2B() {
        return getPartnerType() == PartnerType.B2B;
    }

    public boolean isB2C() {
        return getPartnerType() == PartnerType.B2C;
    }

    /**
     * Checks if the partner has sufficient credit for a given amount.
     * 
     * @param amount the amount to check
     * @return true if the partner has sufficient credit
     */
    public boolean hasSufficientCredit(BigDecimal amount) {
        if (getCreditLimit() == null) {
            return false;
        }
        
        BigDecimal outstandingBalance = getOutstandingBalance() != null ? 
            getOutstandingBalance() : BigDecimal.ZERO;
        
        return outstandingBalance.add(amount).compareTo(getCreditLimit()) <= 0;
    }

    /**
     * Gets the available credit for the partner.
     * 
     * @return the available credit amount
     */
    public BigDecimal getAvailableCredit() {
        if (getCreditLimit() == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal outstandingBalance = getOutstandingBalance() != null ? 
            getOutstandingBalance() : BigDecimal.ZERO;
        
        return getCreditLimit().subtract(outstandingBalance);
    }

    /**
     * Checks if the partner is VIP.
     * 
     * @return true if the partner is VIP
     */
    public boolean isVip() {
        return getIsVip() != null && getIsVip();
    }

    /**
     * Gets the loyalty level based on total spent.
     * 
     * @return loyalty level (0-5)
     */
    public int getLoyaltyLevel() {
        if (getTotalSpent() == null) {
            return 0;
        }
        
        BigDecimal totalSpent = getTotalSpent();
        
        if (totalSpent.compareTo(BigDecimal.valueOf(10000)) >= 0) return 5;
        if (totalSpent.compareTo(BigDecimal.valueOf(5000)) >= 0) return 4;
        if (totalSpent.compareTo(BigDecimal.valueOf(2000)) >= 0) return 3;
        if (totalSpent.compareTo(BigDecimal.valueOf(500)) >= 0) return 2;
        if (totalSpent.compareTo(BigDecimal.valueOf(100)) >= 0) return 1;
        
        return 0;
    }

    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints = (this.loyaltyPoints == null ? 0 : this.loyaltyPoints) + points;
    }

    public void updateOrderStats(BigDecimal orderValue) {
        this.totalOrders = (this.totalOrders == null ? 0 : this.totalOrders) + 1;
        this.totalSpent = (this.totalSpent == null ? BigDecimal.ZERO : this.totalSpent).add(orderValue);
        this.averageOrderValue = this.totalSpent.divide(BigDecimal.valueOf(this.totalOrders), 2, RoundingMode.HALF_UP);
        this.lastOrderDate = ZonedDateTime.now();
    }

    // Convenience methods for backward compatibility
    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return getIsActive() != null && getIsActive();
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Integer getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Integer loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    // Helper methods for partner groups
    public Set<PartnerGroup> getPartnerGroups() {
        return partnerGroups != null ? partnerGroups : new HashSet<>();
    }

    public void setPartnerGroups(Set<PartnerGroup> partnerGroups) {
        this.partnerGroups = partnerGroups != null ? partnerGroups : new HashSet<>();
    }

    public void addPartnerGroup(PartnerGroup group) {
        if (this.partnerGroups == null) {
            this.partnerGroups = new HashSet<>();
        }
        this.partnerGroups.add(group);
    }

    public void removePartnerGroup(PartnerGroup group) {
        if (this.partnerGroups != null) {
            this.partnerGroups.remove(group);
        }
    }
}