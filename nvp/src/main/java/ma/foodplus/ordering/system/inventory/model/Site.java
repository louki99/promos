package ma.foodplus.ordering.system.inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sites")
public class Site {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(name = "site_code", unique = true, nullable = false)
    private String siteCode;

    @Column(name = "address_line1")
    private String addressLine1;

    private String city;
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "capacity_sqm")
    private Double capacitySqm;

    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL)
    private List<Depot> depots;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
} 