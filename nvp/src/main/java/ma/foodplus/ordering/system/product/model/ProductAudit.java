package ma.foodplus.ordering.system.product.model;

import jakarta.persistence.*;
import lombok.*;
import ma.foodplus.ordering.system.product.service.ProductAuditService.AuditAction;

import java.time.ZonedDateTime;
import java.util.Map;

@Entity
@Table(name = "product_audits")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAudit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private AuditAction action;
    
    @Column(name = "timestamp", nullable = false)
    private ZonedDateTime timestamp;
    
    @ElementCollection
    @CollectionTable(
        name = "product_audit_changes",
        joinColumns = @JoinColumn(name = "audit_id")
    )
    @MapKeyColumn(name = "field_name")
    @Column(name = "change_value")
    private Map<String, String> changes;
    
    @ElementCollection
    @CollectionTable(
        name = "product_audit_details",
        joinColumns = @JoinColumn(name = "audit_id")
    )
    @MapKeyColumn(name = "field_name")
    @Column(name = "field_value")
    private Map<String, String> details;
    
    @Version
    private Long version;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
    }
}