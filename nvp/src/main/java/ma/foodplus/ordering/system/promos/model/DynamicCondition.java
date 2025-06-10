package ma.foodplus.ordering.system.promos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ma.foodplus.ordering.system.order.model.Order;

import java.util.Objects;

@Entity
@Table(name = "promotion_dynamic_conditions")
public class DynamicCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "condition_type", nullable = false)
    private String conditionType;

    @NotBlank
    @Column(name = "condition_value", nullable = false)
    private String conditionValue;

    @NotBlank
    @Column(name = "operator", nullable = false)
    private String operator;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "description")
    private String description;

    @Column(name = "threshold")
    private Double threshold;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Transient // We don't want to persist this
    private Order order;

    // Constructors
    public DynamicCondition() {
    }

    public DynamicCondition(String conditionType, String conditionValue, String operator) {
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
        this.operator = operator;
        this.isActive = true;
    }

    // Business Logic Methods
    public boolean evaluate(String value) {
        if (!isActive || value == null) {
            return false;
        }

        switch (operator.toUpperCase()) {
            case "EQUALS":
                return value.equals(conditionValue);
            case "NOT_EQUALS":
                return !value.equals(conditionValue);
            case "CONTAINS":
                return value.contains(conditionValue);
            case "NOT_CONTAINS":
                return !value.contains(conditionValue);
            case "STARTS_WITH":
                return value.startsWith(conditionValue);
            case "ENDS_WITH":
                return value.endsWith(conditionValue);
            default:
                return false;
        }
    }

    public boolean evaluateNumeric(Double value) {
        if (!isActive || value == null) {
            return false;
        }

        Double conditionValueDouble = Double.parseDouble(conditionValue);
        switch (operator.toUpperCase()) {
            case "EQUALS":
                return value.equals(conditionValueDouble);
            case "NOT_EQUALS":
                return !value.equals(conditionValueDouble);
            case "GREATER_THAN":
                return value > conditionValueDouble;
            case "GREATER_THAN_OR_EQUALS":
                return value >= conditionValueDouble;
            case "LESS_THAN":
                return value < conditionValueDouble;
            case "LESS_THAN_OR_EQUALS":
                return value <= conditionValueDouble;
            default:
                return false;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicCondition that = (DynamicCondition) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "DynamicCondition{" +
                "id=" + id +
                ", conditionType='" + conditionType + '\'' +
                ", conditionValue='" + conditionValue + '\'' +
                ", operator='" + operator + '\'' +
                ", isActive=" + isActive +
                '}';
    }
} 