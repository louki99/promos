package ma.foodplus.ordering.system.inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_stocks")
public class ProductStock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depot_id", nullable = false)
    private Depot depot;

    @Column(name = "depot_name")
    private String depotName;

    @Column(nullable = false, precision = 24, scale = 6)
    private BigDecimal quantity;

    @Column(name = "reserved_quantity", nullable = false, precision = 24, scale = 6)
    private BigDecimal reservedQuantity = BigDecimal.ZERO;

    @Column(name = "unit_cost", nullable = false, precision = 24, scale = 6)
    private BigDecimal unitCost;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "quality_status", nullable = false)
    private QualityStatus qualityStatus = QualityStatus.INSPECTED;

    @Column(name = "quality_notes", length = 1000)
    private String qualityNotes;

    // Stock level management
    @Column(name = "min_stock_level", precision = 24, scale = 6)
    private BigDecimal minStockLevel;

    @Column(name = "max_stock_level", precision = 24, scale = 6)
    private BigDecimal maxStockLevel;

    @Column(name = "reorder_point", precision = 24, scale = 6)
    private BigDecimal reorderPoint;

    @Column(name = "reorder_quantity", precision = 24, scale = 6)
    private BigDecimal reorderQuantity;

    @Column(name = "shelf_life_days")
    private Integer shelfLifeDays;

    @Column(name = "storage_conditions", length = 100)
    private String storageConditions;

    @Column(name = "days_of_stock")
    private Integer daysOfStock;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "location_code")
    private String locationCode;

    @Column(name = "last_purchase_date")
    private ZonedDateTime lastPurchaseDate;

    @Column(name = "last_sale_date")
    private ZonedDateTime lastSaleDate;

    @Column(name = "notes", length = 500)
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public enum QualityStatus {
        INSPECTED,
        INSPECTION_REQUIRED,
        QUARANTINED,
        DAMAGED,
        RECALLED
    }
} 