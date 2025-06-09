package ma.foodplus.ordering.system.promos.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dynamic_conditions")
@Data
public class DynamicCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String conditionType;

    @Column(nullable = false)
    private String conditionValue;

    @Column
    private String operator;

    @Column
    private String entityType;

    @Column
    private String entityId;

    @Column
    private boolean isActive;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;
} 