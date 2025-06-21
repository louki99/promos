package ma.foodplus.ordering.system.partner.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * B2C Partner entity representing business-to-consumer partners.
 * 
 * <p>This class extends the base Partner class and adds B2C-specific attributes
 * and validation rules for individual customers.</p>
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
@DiscriminatorValue("B2C")
@Table(name = "partners")
public class B2CPartner extends Partner {
    
    // B2C-specific attributes
    @Column(name = "personal_id_number")
    private String personalIdNumber;
    
    @Column(name = "date_of_birth")
    private String dateOfBirth;
    
    @Column(name = "preferred_language")
    private String preferredLanguage;
    
    @Column(name = "marketing_consent")
    private Boolean marketingConsent = false;

    // B2C-specific Business Methods
    @Override
    public PartnerType getPartnerType() {
        return PartnerType.B2C;
    }

    /**
     * Checks if the B2C partner can place an order.
     * B2C partners need:
     * 1. Active status
     * 2. Valid personal information
     * 
     * @return true if the partner can place an order
     */
    @Override
    public boolean canPlaceOrder() {
        // Check if partner is active
        if (!isActive()) {
            return false;
        }
        
        // Check if personal information is valid
        if (personalIdNumber == null || personalIdNumber.trim().isEmpty()) {
            return false;
        }
        
        // For B2C, credit limit is optional but if set, check it
        if (getCreditInfo() != null && getCreditInfo().getCreditLimit() != null) {
            BigDecimal outstandingBalance = getCreditInfo().getOutstandingBalance() != null ? 
                getCreditInfo().getOutstandingBalance() : BigDecimal.ZERO;
            
            if (outstandingBalance.compareTo(getCreditInfo().getCreditLimit()) >= 0) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Checks if the partner is eligible for marketing communications.
     * 
     * @return true if marketing consent is given
     */
    public boolean isEligibleForMarketing() {
        return marketingConsent != null && marketingConsent;
    }

    /**
     * Gets the age of the partner based on date of birth.
     * 
     * @return age in years, -1 if date of birth is not available
     */
    public int getAge() {
        if (dateOfBirth == null || dateOfBirth.trim().isEmpty()) {
            return -1;
        }
        
        try {
            // Assuming dateOfBirth is in format "yyyy-MM-dd"
            java.time.LocalDate birthDate = java.time.LocalDate.parse(dateOfBirth);
            java.time.LocalDate now = java.time.LocalDate.now();
            return java.time.Period.between(birthDate, now).getYears();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Checks if the partner is a minor.
     * 
     * @return true if the partner is under 18
     */
    public boolean isMinor() {
        int age = getAge();
        return age >= 0 && age < 18;
    }

    /**
     * Validates if the personal ID number format is correct.
     * 
     * @return true if the personal ID number format is valid
     */
    public boolean hasValidPersonalIdNumber() {
        if (personalIdNumber == null || personalIdNumber.trim().isEmpty()) {
            return false;
        }
        
        // Basic validation - can be enhanced based on specific country requirements
        return personalIdNumber.matches("^[A-Z0-9]{5,20}$");
    }

    /**
     * Validates if the B2C partner meets all requirements.
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
        
        // Check personal information
        if (personalIdNumber == null || personalIdNumber.trim().isEmpty()) {
            return false;
        }
        
        // Check contact information
        if (getContactInfo() == null || getContactInfo().getTelephone() == null || getContactInfo().getTelephone().trim().isEmpty()) {
            return false;
        }
        
        return true;
    }

    /**
     * Gets the personal ID number.
     * 
     * @return the personal ID number
     */
    public String getPersonalIdNumber() {
        return personalIdNumber;
    }

    /**
     * Sets the personal ID number.
     * 
     * @param personalIdNumber the personal ID number
     */
    public void setPersonalIdNumber(String personalIdNumber) {
        this.personalIdNumber = personalIdNumber;
    }

    /**
     * Gets the date of birth.
     * 
     * @return the date of birth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth.
     * 
     * @param dateOfBirth the date of birth
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the preferred language.
     * 
     * @return the preferred language
     */
    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    /**
     * Sets the preferred language.
     * 
     * @param preferredLanguage the preferred language
     */
    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    /**
     * Gets the marketing consent.
     * 
     * @return the marketing consent
     */
    public Boolean getMarketingConsent() {
        return marketingConsent;
    }

    /**
     * Sets the marketing consent.
     * 
     * @param marketingConsent the marketing consent
     */
    public void setMarketingConsent(Boolean marketingConsent) {
        this.marketingConsent = marketingConsent;
    }
} 