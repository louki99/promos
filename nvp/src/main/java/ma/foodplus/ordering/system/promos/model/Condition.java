package ma.foodplus.ordering.system.promos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "promotion_conditions")
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConditionType conditionType;

    @NotBlank
    @Column(nullable = false)
    private String attribute;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Operator operator;

    @NotBlank
    @Column(nullable = false)
    private String value;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "customer_group_id")
    private Long customerGroupId;

    @Column(name = "required_loyalty_level")
    private Integer requiredLoyaltyLevel;

    @Column(name = "payment_method")
    private String paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id")
    private PromotionRule rule;

    public enum ConditionType {
        CART_SUBTOTAL,
        PRODUCT_IN_CART,
        CUSTOMER_IN_GROUP,
        TIME_OF_DAY,
        DAY_OF_WEEK,
        CUSTOMER_LOYALTY_LEVEL,
        PAYMENT_METHOD
    }

    public enum Operator {
        EQUAL,
        NOT_EQUAL,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN,
        LESS_THAN_OR_EQUAL,
        CONTAINS,
        NOT_CONTAINS,
        IN,
        NOT_IN
    }

    // Constructors
    public Condition() {
    }

    public Condition(ConditionType conditionType, String attribute, Operator operator, String value) {
        this.conditionType = conditionType;
        this.attribute = attribute;
        this.operator = operator;
        this.value = value;
    }

    // Business Logic Methods
    public boolean evaluate() {
        switch (conditionType) {
            case CART_SUBTOTAL:
                return evaluateNumericComparison(new BigDecimal(value));
            case PRODUCT_IN_CART:
                return evaluateProductInCart();
            case CUSTOMER_IN_GROUP:
                return evaluateCustomerGroup();
            case TIME_OF_DAY:
                return evaluateTimeOfDay();
            case DAY_OF_WEEK:
                return evaluateDayOfWeek();
            case CUSTOMER_LOYALTY_LEVEL:
                return evaluateLoyaltyLevel();
            case PAYMENT_METHOD:
                return evaluatePaymentMethod();
            default:
                return false;
        }
    }

    private boolean evaluateNumericComparison(BigDecimal threshold) {
        BigDecimal actualValue = new BigDecimal(attribute);
        switch (operator) {
            case EQUAL:
                return actualValue.compareTo(threshold) == 0;
            case NOT_EQUAL:
                return actualValue.compareTo(threshold) != 0;
            case GREATER_THAN:
                return actualValue.compareTo(threshold) > 0;
            case GREATER_THAN_OR_EQUAL:
                return actualValue.compareTo(threshold) >= 0;
            case LESS_THAN:
                return actualValue.compareTo(threshold) < 0;
            case LESS_THAN_OR_EQUAL:
                return actualValue.compareTo(threshold) <= 0;
            default:
                return false;
        }
    }

    private boolean evaluateProductInCart() {
        // Implementation depends on cart context
        return false;
    }

    private boolean evaluateCustomerGroup() {
        // Implementation depends on customer context
        return false;
    }

    private boolean evaluateTimeOfDay() {
        // Implementation depends on time context
        return false;
    }

    private boolean evaluateDayOfWeek() {
        // Implementation depends on time context
        return false;
    }

    private boolean evaluateLoyaltyLevel() {
        // Implementation depends on customer context
        return false;
    }

    private boolean evaluatePaymentMethod() {
        // Implementation depends on payment context
        return false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public Long getCustomerGroupId() {
        return customerGroupId;
    }

    public void setCustomerGroupId(Long customerGroupId) {
        this.customerGroupId = customerGroupId;
    }

    public Integer getRequiredLoyaltyLevel() {
        return requiredLoyaltyLevel;
    }

    public void setRequiredLoyaltyLevel(Integer requiredLoyaltyLevel) {
        this.requiredLoyaltyLevel = requiredLoyaltyLevel;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PromotionRule getRule() {
        return rule;
    }

    public void setRule(PromotionRule rule) {
        this.rule = rule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return Objects.equals(id, condition.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Condition{" +
                "id=" + id +
                ", conditionType=" + conditionType +
                ", attribute='" + attribute + '\'' +
                ", operator=" + operator +
                ", value='" + value + '\'' +
                '}';
    }
} 