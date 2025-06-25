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
import java.util.List;
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

    // New fields for enhanced partner management
    @Column(name = "segment")
    private String segment;
    
    @Column(name = "industry_sector")
    private String industrySector;
    
    // Delivery Performance
    @Column(name = "late_deliveries")
    private Integer lateDeliveries = 0;
    
    @Column(name = "on_time_delivery_rate")
    private BigDecimal onTimeDeliveryRate;
    
    @Column(name = "average_delivery_days")
    private BigDecimal averageDeliveryDays;
    
    // Risk & Blocking
    @Column(name = "is_blocked")
    private Boolean isBlocked = false;
    
    @Column(name = "block_reason")
    private String blockReason;
    
    @Column(name = "risk_level")
    private String riskLevel = "LOW";
    
    // KYC & Compliance
    @Column(name = "kyc_status")
    private String kycStatus = "PENDING";
    
    @Column(name = "compliance_notes")
    private String complianceNotes;
    
    @Column(name = "blacklist_check")
    private Boolean blacklistCheck = false;
    
    @Column(name = "blacklist_reason")
    private String blacklistReason;

    // Relationships with new entities
    @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ContactPerson> contactPersons = new HashSet<>();
    
    @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Document> documents = new HashSet<>();
    
    @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PartnerInteraction> interactions = new HashSet<>();

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
        this.partnerGroups.remove(group);
        group.getPartners().remove(this);
    }

    // ========== Contact Person Management ==========
    
    /**
     * Adds a contact person to this partner.
     * 
     * @param contactPerson the contact person to add
     */
    public void addContactPerson(ContactPerson contactPerson) {
        this.contactPersons.add(contactPerson);
        contactPerson.setPartner(this);
    }
    
    /**
     * Removes a contact person from this partner.
     * 
     * @param contactPerson the contact person to remove
     */
    public void removeContactPerson(ContactPerson contactPerson) {
        this.contactPersons.remove(contactPerson);
        contactPerson.setPartner(null);
    }
    
    /**
     * Gets the preferred contact person for this partner.
     * 
     * @return the preferred contact person, or null if none exists
     */
    public ContactPerson getPreferredContactPerson() {
        return this.contactPersons.stream()
                .filter(ContactPerson::isPreferred)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Gets all active contact persons for this partner.
     * 
     * @return set of active contact persons
     */
    public Set<ContactPerson> getActiveContactPersons() {
        return this.contactPersons.stream()
                .filter(ContactPerson::isActive)
                .collect(java.util.stream.Collectors.toSet());
    }

    // ========== Document Management ==========
    
    /**
     * Adds a document to this partner.
     * 
     * @param document the document to add
     */
    public void addDocument(Document document) {
        this.documents.add(document);
        document.setPartner(this);
    }
    
    /**
     * Removes a document from this partner.
     * 
     * @param document the document to remove
     */
    public void removeDocument(Document document) {
        this.documents.remove(document);
        document.setPartner(null);
    }
    
    /**
     * Gets all active documents for this partner.
     * 
     * @return set of active documents
     */
    public Set<Document> getActiveDocuments() {
        return this.documents.stream()
                .filter(Document::isActive)
                .collect(java.util.stream.Collectors.toSet());
    }
    
    /**
     * Gets documents by type for this partner.
     * 
     * @param type the document type
     * @return set of documents of the specified type
     */
    public Set<Document> getDocumentsByType(String type) {
        return this.documents.stream()
                .filter(doc -> type.equals(doc.getType()))
                .collect(java.util.stream.Collectors.toSet());
    }
    
    /**
     * Gets expired documents for this partner.
     * 
     * @return set of expired documents
     */
    public Set<Document> getExpiredDocuments() {
        return this.documents.stream()
                .filter(Document::isExpired)
                .collect(java.util.stream.Collectors.toSet());
    }
    
    /**
     * Gets documents expiring soon for this partner.
     * 
     * @param daysThreshold the number of days threshold
     * @return set of documents expiring soon
     */
    public Set<Document> getDocumentsExpiringSoon(int daysThreshold) {
        return this.documents.stream()
                .filter(doc -> doc.isExpiringSoon(daysThreshold))
                .collect(java.util.stream.Collectors.toSet());
    }

    // ========== Interaction Logging ==========
    
    /**
     * Logs an interaction with this partner.
     * 
     * @param user the user performing the action
     * @param action the action performed
     */
    public void logInteraction(String user, String action) {
        PartnerInteraction interaction = PartnerInteraction.create(user, action, this);
        this.interactions.add(interaction);
    }
    
    /**
     * Logs an interaction with details.
     * 
     * @param user the user performing the action
     * @param action the action performed
     * @param details additional details about the action
     */
    public void logInteraction(String user, String action, String details) {
        PartnerInteraction interaction = PartnerInteraction.create(user, action, details, this);
        this.interactions.add(interaction);
    }
    
    /**
     * Logs an interaction with full details.
     * 
     * @param user the user performing the action
     * @param action the action performed
     * @param details additional details about the action
     * @param interactionType the type of interaction
     * @param severity the severity level
     */
    public void logInteraction(String user, String action, String details, 
                             PartnerInteraction.InteractionType interactionType, 
                             PartnerInteraction.InteractionSeverity severity) {
        PartnerInteraction interaction = PartnerInteraction.create(user, action, details, 
                                                                  interactionType, severity, this);
        this.interactions.add(interaction);
    }
    
    /**
     * Gets recent interactions for this partner.
     * 
     * @param limit the maximum number of interactions to return
     * @return list of recent interactions
     */
    public List<PartnerInteraction> getRecentInteractions(int limit) {
        return this.interactions.stream()
                .sorted((i1, i2) -> i2.getTimestamp().compareTo(i1.getTimestamp()))
                .limit(limit)
                .collect(java.util.stream.Collectors.toList());
    }

    // ========== Risk & Blocking Management ==========
    
    /**
     * Checks if this partner is blocked.
     * 
     * @return true if this partner is blocked
     */
    public boolean isBlocked() {
        return isBlocked != null && isBlocked;
    }
    
    /**
     * Blocks this partner.
     * 
     * @param reason the reason for blocking
     */
    public void block(String reason) {
        this.isBlocked = true;
        this.blockReason = reason;
        this.riskLevel = "HIGH";
    }
    
    /**
     * Unblocks this partner.
     */
    public void unblock() {
        this.isBlocked = false;
        this.blockReason = null;
        this.riskLevel = "LOW";
    }
    
    /**
     * Updates the risk level of this partner.
     * 
     * @param riskLevel the new risk level
     */
    public void updateRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
    
    /**
     * Checks if this partner is blacklisted.
     * 
     * @return true if this partner is blacklisted
     */
    public boolean isBlacklisted() {
        return blacklistCheck != null && blacklistCheck;
    }
    
    /**
     * Blacklists this partner.
     * 
     * @param reason the reason for blacklisting
     */
    public void blacklist(String reason) {
        this.blacklistCheck = true;
        this.blacklistReason = reason;
        this.isBlocked = true;
        this.riskLevel = "CRITICAL";
    }
    
    /**
     * Removes this partner from blacklist.
     */
    public void removeFromBlacklist() {
        this.blacklistCheck = false;
        this.blacklistReason = null;
        this.isBlocked = false;
        this.riskLevel = "LOW";
    }

    // ========== KYC & Compliance Management ==========
    
    /**
     * Updates the KYC status of this partner.
     * 
     * @param kycStatus the new KYC status
     */
    public void updateKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }
    
    /**
     * Checks if this partner's KYC is verified.
     * 
     * @return true if KYC is verified
     */
    public boolean isKycVerified() {
        return "VERIFIED".equals(this.kycStatus);
    }
    
    /**
     * Checks if this partner's KYC is pending.
     * 
     * @return true if KYC is pending
     */
    public boolean isKycPending() {
        return "PENDING".equals(this.kycStatus);
    }
    
    /**
     * Adds compliance notes to this partner.
     * 
     * @param notes the compliance notes to add
     */
    public void addComplianceNotes(String notes) {
        if (this.complianceNotes == null) {
            this.complianceNotes = notes;
        } else {
            this.complianceNotes += "\n" + notes;
        }
    }

    // ========== Delivery Performance Management ==========
    
    /**
     * Updates delivery performance metrics.
     * 
     * @param onTimeDeliveries number of on-time deliveries
     * @param totalDeliveries total number of deliveries
     * @param averageDays average delivery days
     */
    public void updateDeliveryPerformance(int onTimeDeliveries, int totalDeliveries, BigDecimal averageDays) {
        this.lateDeliveries = totalDeliveries - onTimeDeliveries;
        if (totalDeliveries > 0) {
            this.onTimeDeliveryRate = BigDecimal.valueOf(onTimeDeliveries)
                    .divide(BigDecimal.valueOf(totalDeliveries), 3, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        this.averageDeliveryDays = averageDays;
    }
    
    /**
     * Increments late deliveries count.
     */
    public void incrementLateDeliveries() {
        this.lateDeliveries = (this.lateDeliveries != null ? this.lateDeliveries : 0) + 1;
    }
    
    /**
     * Gets the delivery performance rating.
     * 
     * @return the delivery performance rating (EXCELLENT, GOOD, AVERAGE, POOR)
     */
    public String getDeliveryPerformanceRating() {
        if (this.onTimeDeliveryRate == null) {
            return "UNKNOWN";
        }
        
        double rate = this.onTimeDeliveryRate.doubleValue();
        if (rate >= 95.0) return "EXCELLENT";
        if (rate >= 85.0) return "GOOD";
        if (rate >= 70.0) return "AVERAGE";
        return "POOR";
    }

    // ========== Segmentation Management ==========
    
    /**
     * Updates the segment of this partner.
     * 
     * @param segment the new segment
     */
    public void updateSegment(String segment) {
        this.segment = segment;
    }
    
    /**
     * Updates the industry sector of this partner.
     * 
     * @param industrySector the new industry sector
     */
    public void updateIndustrySector(String industrySector) {
        this.industrySector = industrySector;
    }
    
    /**
     * Checks if this partner is in a specific segment.
     * 
     * @param segment the segment to check
     * @return true if this partner is in the specified segment
     */
    public boolean isInSegment(String segment) {
        return segment != null && segment.equals(this.segment);
    }
    
    /**
     * Checks if this partner is in a specific industry sector.
     * 
     * @param industrySector the industry sector to check
     * @return true if this partner is in the specified industry sector
     */
    public boolean isInIndustrySector(String industrySector) {
        return industrySector != null && industrySector.equals(this.industrySector);
    }
}