package ma.foodplus.ordering.system.partner.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * B2B Partner entity representing business-to-business partners.
 * 
 * <p>This class extends the base Partner class and adds B2B-specific attributes
 * such as company information, contract details, and business-specific validation rules.</p>
 * 
 * @author FoodPlus Development Team
 * @version 2.0.0
 */
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("B2B")
public class B2BPartner extends Partner {

    // B2B-specific Business Methods
    @Override
    public PartnerType getPartnerType() {
        return PartnerType.B2B;
    }

    /**
     * Checks if the B2B partner has a valid contract.
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
     * Gets the number of days until contract expiration.
     * 
     * @return number of days until expiration, negative if already expired
     */
    public long getDaysUntilContractExpiration() {
        if (getContractEndDate() == null) {
            return -1;
        }
        
        ZonedDateTime now = ZonedDateTime.now();
        return java.time.Duration.between(now, getContractEndDate()).toDays();
    }

    /**
     * Checks if the partner has overdue payments.
     * 
     * @return true if the partner has overdue payments
     */
    public boolean hasOverduePayments() {
        if (getOutstandingBalance() == null) {
            return false;
        }
        
        return getOutstandingBalance().compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Checks if the B2B partner can place an order.
     * B2B partners need:
     * 1. Active status
     * 2. Valid contract
     * 3. Sufficient credit limit
     * 
     * @return true if the partner can place an order
     */
    @Override
    public boolean canPlaceOrder() {
        // Check if partner is active
        if (!isActive()) {
            return false;
        }
        
        // Check if contract is valid
        if (!hasValidContract()) {
            return false;
        }
        
        // Check credit limit
        if (getCreditLimit() == null) {
            return false;
        }
        
        BigDecimal outstandingBalance = getOutstandingBalance() != null ? 
            getOutstandingBalance() : BigDecimal.ZERO;
        
        return outstandingBalance.compareTo(getCreditLimit()) < 0;
    }

    /**
     * Validates if the B2B partner meets all requirements.
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
} 