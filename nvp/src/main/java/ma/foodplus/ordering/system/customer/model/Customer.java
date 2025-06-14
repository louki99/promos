package ma.foodplus.ordering.system.customer.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.foodplus.ordering.system.common.model.CategoryTarif;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ct_num", nullable = false, unique = true)
    private String ctNum;

    @Column(name = "ice", nullable = false)
    private String ice;

    @Column(nullable = false)
    private String description;

    // Contact Information
    private String telephone;
    private String telecopie;
    private String email;
    private String address;
    private String city;
    private String country;
    private String region;
    private String postalCode;
    
    // B2B Specific Information
    @Column(name = "company_name")
    private String companyName;
    
    @Column(name = "legal_form")
    private String legalForm; // SARL, SA, etc.
    
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

    // B2B Contract Information
    @Column(name = "contract_number")
    private String contractNumber;
    
    @Column(name = "contract_start_date")
    private ZonedDateTime contractStartDate;
    
    @Column(name = "contract_end_date")
    private ZonedDateTime contractEndDate;
    
    @Column(name = "contract_type")
    private String contractType;
    
    @Column(name = "contract_terms", columnDefinition = "TEXT")
    private String contractTerms;
    
    @Column(name = "payment_terms", columnDefinition = "TEXT")
    private String paymentTerms;
    
    @Column(name = "delivery_terms", columnDefinition = "TEXT")
    private String deliveryTerms;
    
    @Column(name = "special_conditions", columnDefinition = "TEXT")
    private String specialConditions;

    // B2B Credit Information
    @Column(name = "credit_limit", precision = 24, scale = 6)
    private BigDecimal creditLimit;
    
    @Column(name = "credit_rating")
    private String creditRating;
    
    @Column(name = "credit_score")
    private Integer creditScore;
    
    @Column(name = "payment_history", columnDefinition = "TEXT")
    private String paymentHistory;
    
    @Column(name = "outstanding_balance", precision = 24, scale = 6)
    private BigDecimal outstandingBalance;
    
    @Column(name = "last_payment_date")
    private ZonedDateTime lastPaymentDate;
    
    @Column(name = "payment_term_days")
    private Integer paymentTermDays;
    
    @Column(name = "preferred_payment_method")
    private String preferredPaymentMethod;
    
    @Column(name = "bank_account_info", columnDefinition = "TEXT")
    private String bankAccountInfo;

    // B2B Business Status
    @Column(name = "is_vip")
    private boolean isVip;

    @Column(name = "loyalty_points")
    private Integer loyaltyPoints;

    @Column(name = "last_order_date")
    private ZonedDateTime lastOrderDate;

    @Column(name = "total_orders")
    private Integer totalOrders;

    @Column(name = "total_spent", precision = 24, scale = 6)
    private BigDecimal totalSpent;
    
    @Column(name = "average_order_value", precision = 24, scale = 6)
    private BigDecimal averageOrderValue;
    
    @Column(name = "customer_since")
    private ZonedDateTime customerSince;
    
    @Column(name = "preferred_delivery_time")
    private String preferredDeliveryTime;
    
    @Column(name = "preferred_delivery_days")
    private String preferredDeliveryDays;
    
    @Column(name = "special_handling_instructions", columnDefinition = "TEXT")
    private String specialHandlingInstructions;

    // Relationships
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "customer_group_members",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<CustomerGroup> customerGroups = new HashSet<>();

    // Audit
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
    
    @Column(name = "last_activity_date")
    private ZonedDateTime lastActivityDate;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "updated_by")
    private String updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // Business Methods
    public boolean isB2B() {
        return customerType == CustomerType.B2B;
    }

    public boolean isB2C() {
        return customerType == CustomerType.B2C;
    }

    public boolean hasValidContract() {
        if (!isB2B()) return false;
        if (contractStartDate == null || contractEndDate == null) return false;
        ZonedDateTime now = ZonedDateTime.now();
        return now.isAfter(contractStartDate) && now.isBefore(contractEndDate);
    }

    public boolean canPlaceOrder() {
        if (!active) return false;
        if (isB2B() && !hasValidContract()) return false;
        if (isB2B() && outstandingBalance.compareTo(creditLimit) >= 0) return false;
        return true;
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

    @Column(name = "customer_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CustomerType customerType = CustomerType.B2C;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_tarif_id")
    private CategoryTarif cateTarif;
} 