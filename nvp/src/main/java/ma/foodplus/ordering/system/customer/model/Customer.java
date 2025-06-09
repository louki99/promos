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
import java.time.ZonedDateTime;

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

    private String telephone;
    private String telecopie;
    private String email;
    private String address;
    
    @Column(name = "code_postal")
    private String codePostal;
    
    private String ville;
    private String country;
    
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cate_tarif_id")
    private CategoryTarif cateTarif;

    @Type(JsonBinaryType.class)
    @Column(name = "num_payeur", columnDefinition = "jsonb")
    private String numPayeur;

    @Column(name = "max_credit", precision = 24, scale = 6)
    private BigDecimal maxCredit;

    @Column(name = "current_credit", precision = 24, scale = 6)
    private BigDecimal currentCredit;

    @Column(name = "payment_term_days")
    private Integer paymentTermDays;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "registration_number")
    private String registrationNumber;

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

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
} 