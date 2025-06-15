package ma.foodplus.ordering.system.promos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rewards")
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardType rewardType;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(nullable = false)
    private String description;

    private String productId;
    private String categoryId;
    private String familyCode;
    private Integer quantity;
    private Boolean isPercentage;
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    public enum RewardType {
        DISCOUNT_PERCENTAGE,
        DISCOUNT_AMOUNT,
        FREE_PRODUCT,
        FREE_SHIPPING,
        LOYALTY_POINTS,
        GIFT_CARD,
        CASHBACK
    }

    public BigDecimal calculateDiscount(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Value must be non-negative");
        }

        switch (rewardType) {
            case DISCOUNT_PERCENTAGE:
                return value.multiply(this.value).divide(new BigDecimal("100"));
            case DISCOUNT_AMOUNT:
                return this.value.min(value);
            case FREE_PRODUCT:
                return value;
            default:
                return BigDecimal.ZERO;
        }
    }
}