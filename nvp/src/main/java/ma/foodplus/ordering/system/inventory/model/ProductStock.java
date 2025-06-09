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

    @Column(name = "depot_id", nullable = false)
    private Long depotId;

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

    @Column(name = "minimum_quantity", precision = 24, scale = 6)
    private BigDecimal minimumQuantity;

    @Column(name = "maximum_quantity", precision = 24, scale = 6)
    private BigDecimal maximumQuantity;

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