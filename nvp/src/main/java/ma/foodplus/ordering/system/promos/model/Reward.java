package ma.foodplus.ordering.system.promos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardType rewardType;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false)
    private BigDecimal value;

    private Long targetEntityId;
    private String targetEntityType;

    public enum RewardType { 
        PERCENT_DISCOUNT_ON_ITEM, 
        FIXED_DISCOUNT_ON_CART, 
        FREE_PRODUCT 
    }

    // Constructors
    public Reward() {
    }

    public Reward(RewardType rewardType, BigDecimal value) {
        this.rewardType = rewardType;
        this.value = value;
        validateValue();
    }

    public Reward(RewardType rewardType, BigDecimal value, Long targetEntityId, String targetEntityType) {
        this.rewardType = rewardType;
        this.value = value;
        this.targetEntityId = targetEntityId;
        this.targetEntityType = targetEntityType;
        validateValue();
    }

    // Business Logic Methods
    private void validateValue() {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Reward value must be non-negative");
        }
        if (rewardType == RewardType.PERCENT_DISCOUNT_ON_ITEM && value.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Percentage discount cannot exceed 100%");
        }
    }

    public BigDecimal calculateDiscount(BigDecimal originalPrice) {
        if (originalPrice == null || originalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Original price must be non-negative");
        }

        switch (rewardType) {
            case PERCENT_DISCOUNT_ON_ITEM:
                return originalPrice.multiply(value).divide(new BigDecimal("100"));
            case FIXED_DISCOUNT_ON_CART:
                return value.min(originalPrice);
            case FREE_PRODUCT:
                return originalPrice;
            default:
                return BigDecimal.ZERO;
        }
    }

    public boolean requiresTargetEntity() {
        return rewardType == RewardType.PERCENT_DISCOUNT_ON_ITEM || 
               rewardType == RewardType.FREE_PRODUCT;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RewardType getRewardType() {
        return rewardType;
    }

    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
        validateValue();
    }

    public Long getTargetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntityId(Long targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    public String getTargetEntityType() {
        return targetEntityType;
    }

    public void setTargetEntityType(String targetEntityType) {
        this.targetEntityType = targetEntityType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reward reward = (Reward) o;
        return Objects.equals(id, reward.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reward{" +
                "id=" + id +
                ", rewardType=" + rewardType +
                ", value=" + value +
                ", targetEntityId=" + targetEntityId +
                '}';
    }
}