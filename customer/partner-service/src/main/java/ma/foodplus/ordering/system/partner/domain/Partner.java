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

    // Common Embedded Objects
    @Embedded
    private ContactInfo contactInfo;
    
    @Embedded
    private CreditInfo creditInfo;
    
    @Embedded
    private LoyaltyInfo loyaltyInfo;
    
    @Embedded
    private DeliveryPreference deliveryPreference;
    
    @Embedded
    private AuditInfo auditInfo;

    // Common Relationships
    @ManyToMany(fetch = FetchType.EAGER)
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
        if (getCreditInfo() == null || getCreditInfo().getCreditLimit() == null) {
            return false;
        }
        
        BigDecimal outstandingBalance = getCreditInfo().getOutstandingBalance() != null ? 
            getCreditInfo().getOutstandingBalance() : BigDecimal.ZERO;
        
        return outstandingBalance.add(amount).compareTo(getCreditInfo().getCreditLimit()) <= 0;
    }

    /**
     * Gets the available credit for the partner.
     * 
     * @return the available credit amount
     */
    public BigDecimal getAvailableCredit() {
        if (getCreditInfo() == null || getCreditInfo().getCreditLimit() == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal outstandingBalance = getCreditInfo().getOutstandingBalance() != null ? 
            getCreditInfo().getOutstandingBalance() : BigDecimal.ZERO;
        
        return getCreditInfo().getCreditLimit().subtract(outstandingBalance);
    }

    /**
     * Checks if the partner is VIP.
     * 
     * @return true if the partner is VIP
     */
    public boolean isVip() {
        return getLoyaltyInfo() != null && getLoyaltyInfo().isVip();
    }

    /**
     * Gets the loyalty level based on total spent.
     * 
     * @return loyalty level (0-5)
     */
    public int getLoyaltyLevel() {
        if (getLoyaltyInfo() == null || getLoyaltyInfo().getTotalSpent() == null) {
            return 0;
        }
        
        BigDecimal totalSpent = getLoyaltyInfo().getTotalSpent();
        
        if (totalSpent.compareTo(BigDecimal.valueOf(10000)) >= 0) return 5;
        if (totalSpent.compareTo(BigDecimal.valueOf(5000)) >= 0) return 4;
        if (totalSpent.compareTo(BigDecimal.valueOf(2000)) >= 0) return 3;
        if (totalSpent.compareTo(BigDecimal.valueOf(500)) >= 0) return 2;
        if (totalSpent.compareTo(BigDecimal.valueOf(100)) >= 0) return 1;
        
        return 0;
    }

    public void addLoyaltyPoints(int points) {
        if (loyaltyInfo == null) {
            loyaltyInfo = new LoyaltyInfo();
        }
        this.loyaltyInfo.setLoyaltyPoints((this.loyaltyInfo.getLoyaltyPoints() == null ? 0 : this.loyaltyInfo.getLoyaltyPoints()) + points);
    }

    public void updateOrderStats(BigDecimal orderValue) {
        if (loyaltyInfo == null) {
            loyaltyInfo = new LoyaltyInfo();
        }
        this.loyaltyInfo.setTotalOrders((this.loyaltyInfo.getTotalOrders() == null ? 0 : this.loyaltyInfo.getTotalOrders()) + 1);
        this.loyaltyInfo.setTotalSpent((this.loyaltyInfo.getTotalSpent() == null ? BigDecimal.ZERO : this.loyaltyInfo.getTotalSpent()).add(orderValue));
        this.loyaltyInfo.setAverageOrderValue(this.loyaltyInfo.getTotalSpent().divide(BigDecimal.valueOf(this.loyaltyInfo.getTotalOrders()), 2, RoundingMode.HALF_UP));
        this.loyaltyInfo.setLastOrderDate(ZonedDateTime.now());
    }

    // Convenience methods for backward compatibility
    public String getTelephone() {
        return contactInfo != null ? contactInfo.getTelephone() : null;
    }

    public void setTelephone(String telephone) {
        if (contactInfo == null) contactInfo = new ContactInfo();
        contactInfo.setTelephone(telephone);
    }

    public String getEmail() {
        return contactInfo != null ? contactInfo.getEmail() : null;
    }

    public void setEmail(String email) {
        if (contactInfo == null) contactInfo = new ContactInfo();
        contactInfo.setEmail(email);
    }

    public String getAddress() {
        return contactInfo != null ? contactInfo.getAddress() : null;
    }

    public void setAddress(String address) {
        if (contactInfo == null) contactInfo = new ContactInfo();
        contactInfo.setAddress(address);
    }

    public boolean isActive() {
        return auditInfo != null && auditInfo.isActive();
    }

    public void setActive(boolean active) {
        if (auditInfo == null) auditInfo = new AuditInfo();
        auditInfo.setActive(active);
    }

    public ZonedDateTime getCreatedAt() {
        return auditInfo != null ? auditInfo.getCreatedAt() : null;
    }

    public ZonedDateTime getUpdatedAt() {
        return auditInfo != null ? auditInfo.getUpdatedAt() : null;
    }

    public Integer getLoyaltyPoints() {
        return loyaltyInfo != null ? loyaltyInfo.getLoyaltyPoints() : null;
    }

    public void setLoyaltyPoints(Integer loyaltyPoints) {
        if (loyaltyInfo == null) loyaltyInfo = new LoyaltyInfo();
        loyaltyInfo.setLoyaltyPoints(loyaltyPoints);
    }

    public BigDecimal getCreditLimit() {
        return creditInfo != null ? creditInfo.getCreditLimit() : null;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        if (creditInfo == null) creditInfo = new CreditInfo();
        creditInfo.setCreditLimit(creditLimit);
    }

    public BigDecimal getOutstandingBalance() {
        return creditInfo != null ? creditInfo.getOutstandingBalance() : null;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        if (creditInfo == null) creditInfo = new CreditInfo();
        creditInfo.setOutstandingBalance(outstandingBalance);
    }
}