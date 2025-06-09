package ma.foodplus.ordering.system.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.foodplus.ordering.system.product.enums.SuiviStock;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true )
    private String reference;

    @Column(unique = true, nullable = false)
    private String sku; // Stock Keeping Unit

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(unique = true, length = 36)
    private String barcode;

    @Column(name = "family_code", length = 19)
    private String familyCode;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "family_id")
//    private ProductFamily family;

    @Column(length = 100)
    private String category1;

    @Column(length = 100)
    private String category2;

    @Column(length = 100)
    private String category3;

    @Column(length = 100)
    private String category4;

    @Column(name = "sale_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "sale_unit", length = 50)
    private String saleUnit;

    @Column(name = "price_including_tax", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceIncludingTax;

    // This is the new field for promotional points
    @Column(name = "promo_sku_points", nullable = false, precision = 10, scale = 2)
    private BigDecimal promoSkuPoints = BigDecimal.ZERO;

    @Column(length = 500)
    private String photo;

    @Column(nullable = false)
    private Boolean deliverable = false;

    @Column(nullable = false)
    private Boolean inactive = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_tracking", nullable = false, length = 20)
    private SuiviStock stockTracking = SuiviStock.Aucun;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
} 