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
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("B2B")
@Table(name = "partners")
public class B2BPartner extends Partner {
    
    // B2B-specific Embedded Objects
    @Embedded
    private CompanyInfo companyInfo;
    
    @Embedded
    private ContractInfo contractInfo;

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
        if (contractInfo == null || contractInfo.getContractStartDate() == null || contractInfo.getContractEndDate() == null) {
            return false;
        }
        ZonedDateTime now = ZonedDateTime.now();
        return now.isAfter(contractInfo.getContractStartDate()) && now.isBefore(contractInfo.getContractEndDate());
    }

    /**
     * Checks if the contract is expiring soon.
     * 
     * @param daysThreshold the number of days to consider as "soon"
     * @return true if the contract expires within the threshold
     */
    public boolean isContractExpiringSoon(int daysThreshold) {
        if (contractInfo == null || contractInfo.getContractEndDate() == null) {
            return false;
        }
        
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime thresholdDate = now.plusDays(daysThreshold);
        
        return contractInfo.getContractEndDate().isBefore(thresholdDate) && 
               contractInfo.getContractEndDate().isAfter(now);
    }

    /**
     * Gets the number of days until contract expiration.
     * 
     * @return number of days until expiration, negative if already expired
     */
    public long getDaysUntilContractExpiration() {
        if (contractInfo == null || contractInfo.getContractEndDate() == null) {
            return -1;
        }
        
        ZonedDateTime now = ZonedDateTime.now();
        return java.time.Duration.between(now, contractInfo.getContractEndDate()).toDays();
    }

    /**
     * Checks if the partner has overdue payments.
     * 
     * @return true if the partner has overdue payments
     */
    public boolean hasOverduePayments() {
        if (getCreditInfo() == null || getCreditInfo().getOutstandingBalance() == null) {
            return false;
        }
        
        return getCreditInfo().getOutstandingBalance().compareTo(BigDecimal.ZERO) > 0;
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
        if (getCreditInfo() == null || getCreditInfo().getCreditLimit() == null) {
            return false;
        }
        
        BigDecimal outstandingBalance = getCreditInfo().getOutstandingBalance() != null ? 
            getCreditInfo().getOutstandingBalance() : BigDecimal.ZERO;
        
        return outstandingBalance.compareTo(getCreditInfo().getCreditLimit()) < 0;
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
        if (companyInfo == null || companyInfo.getCompanyName() == null || companyInfo.getCompanyName().trim().isEmpty()) {
            return false;
        }
        
        // Check contract information
        if (contractInfo == null || contractInfo.getContractNumber() == null || contractInfo.getContractNumber().trim().isEmpty()) {
            return false;
        }
        
        if (contractInfo.getContractStartDate() == null || contractInfo.getContractEndDate() == null) {
            return false;
        }
        
        if (contractInfo.getContractEndDate().isBefore(contractInfo.getContractStartDate())) {
            return false;
        }
        
        return true;
    }

    /**
     * Gets the company name.
     * 
     * @return the company name
     */
    public String getCompanyName() {
        return companyInfo != null ? companyInfo.getCompanyName() : null;
    }

    /**
     * Sets the company name.
     * 
     * @param companyName the company name
     */
    public void setCompanyName(String companyName) {
        if (companyInfo == null) companyInfo = new CompanyInfo();
        companyInfo.setCompanyName(companyName);
    }

    /**
     * Gets the contract number.
     * 
     * @return the contract number
     */
    public String getContractNumber() {
        return contractInfo != null ? contractInfo.getContractNumber() : null;
    }

    /**
     * Sets the contract number.
     * 
     * @param contractNumber the contract number
     */
    public void setContractNumber(String contractNumber) {
        if (contractInfo == null) contractInfo = new ContractInfo();
        contractInfo.setContractNumber(contractNumber);
    }

    /**
     * Gets the contract start date.
     * 
     * @return the contract start date
     */
    public ZonedDateTime getContractStartDate() {
        return contractInfo != null ? contractInfo.getContractStartDate() : null;
    }

    /**
     * Sets the contract start date.
     * 
     * @param contractStartDate the contract start date
     */
    public void setContractStartDate(ZonedDateTime contractStartDate) {
        if (contractInfo == null) contractInfo = new ContractInfo();
        contractInfo.setContractStartDate(contractStartDate);
    }

    /**
     * Gets the contract end date.
     * 
     * @return the contract end date
     */
    public ZonedDateTime getContractEndDate() {
        return contractInfo != null ? contractInfo.getContractEndDate() : null;
    }

    /**
     * Sets the contract end date.
     * 
     * @param contractEndDate the contract end date
     */
    public void setContractEndDate(ZonedDateTime contractEndDate) {
        if (contractInfo == null) contractInfo = new ContractInfo();
        contractInfo.setContractEndDate(contractEndDate);
    }

    /**
     * Gets the annual turnover.
     * 
     * @return the annual turnover
     */
    public BigDecimal getAnnualTurnover() {
        return companyInfo != null ? companyInfo.getAnnualTurnover() : null;
    }

    /**
     * Sets the annual turnover.
     * 
     * @param annualTurnover the annual turnover
     */
    public void setAnnualTurnover(BigDecimal annualTurnover) {
        if (companyInfo == null) companyInfo = new CompanyInfo();
        companyInfo.setAnnualTurnover(annualTurnover);
    }

    /**
     * Gets the number of employees.
     * 
     * @return the number of employees
     */
    public Integer getNumberOfEmployees() {
        return companyInfo != null ? companyInfo.getNumberOfEmployees() : null;
    }

    /**
     * Sets the number of employees.
     * 
     * @param numberOfEmployees the number of employees
     */
    public void setNumberOfEmployees(Integer numberOfEmployees) {
        if (companyInfo == null) companyInfo = new CompanyInfo();
        companyInfo.setNumberOfEmployees(numberOfEmployees);
    }
} 