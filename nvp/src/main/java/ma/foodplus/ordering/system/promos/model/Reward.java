package ma.foodplus.ordering.system.promos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardType type;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "discount_percentage")
    private BigDecimal discountPercentage;

    @Column(name = "target_entity_id")
    private String targetEntityId;

    @Column(name = "target_entity_type")
    @Enumerated(EnumType.STRING)
    private TargetEntityType targetEntityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    public enum RewardType {
        FIXED_AMOUNT,
        PERCENTAGE,
        FREE_PRODUCT,
        POINTS_MULTIPLIER
    }

    public enum TargetEntityType {
        PRODUCT,
        PRODUCT_FAMILY,
        CATEGORY,
        CART
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RewardType getType() {
        return type;
    }

    public void setType(RewardType type) {
        this.type = type;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getTargetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntityId(String targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    public TargetEntityType getTargetEntityType() {
        return targetEntityType;
    }

    public void setTargetEntityType(TargetEntityType targetEntityType) {
        this.targetEntityType = targetEntityType;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public BigDecimal calculateDiscount(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }

        switch (type) {
            case FIXED_AMOUNT:
                return discountAmount != null ? discountAmount : BigDecimal.ZERO;
            case PERCENTAGE:
                return discountPercentage != null ? 
                    value.multiply(discountPercentage).divide(new BigDecimal("100")) : 
                    BigDecimal.ZERO;
            case FREE_PRODUCT:
                return value; // Full discount for free product
            case POINTS_MULTIPLIER:
                return BigDecimal.ZERO; // Points are handled separately
            default:
                return BigDecimal.ZERO;
        }
    }
}