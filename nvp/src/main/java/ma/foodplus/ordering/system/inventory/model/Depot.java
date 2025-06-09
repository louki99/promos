package ma.foodplus.ordering.system.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "depots")
public class Depot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(name = "depot_code", unique = true, nullable = false)
    private String depotCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

    @Column(name = "depot_type")
    @Enumerated(EnumType.STRING)
    private DepotType depotType;

    @Column(name = "capacity_cubic_meters")
    private Double capacityCubicMeters;

    @Column(name = "temperature_range")
    private String temperatureRange;

    @Column(name = "is_refrigerated")
    private boolean isRefrigerated;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "security_level")
    private Integer securityLevel;

    @Column(name = "access_restrictions")
    private String accessRestrictions;

    @Column(name = "handling_equipment")
    private String handlingEquipment;

    @Column(name = "special_requirements", columnDefinition = "TEXT")
    private String specialRequirements;

    @OneToMany(mappedBy = "depot", cascade = CascadeType.ALL)
    private List<ProductStock> productStocks;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public enum DepotType {
        MAIN_WAREHOUSE,
        DISTRIBUTION_CENTER,
        COLD_STORAGE,
        DRY_STORAGE,
        HAZARDOUS_MATERIALS,
        TEMPORARY_STORAGE,
        CROSS_DOCKING
    }
} 