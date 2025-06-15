package ma.foodplus.ordering.system.promos.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PromotionTierDTO {
    private Integer id;
    private Integer ruleId;
    private BigDecimal minimumThreshold;
    private BigDecimal discountAmount;
    private BigDecimal discountPercentage;
    private RewardType rewardType;

    public enum RewardType {
        FIXED_AMOUNT,
        PERCENTAGE
    }

    public BigDecimal getDiscountAmount() {
        if (rewardType == RewardType.FIXED_AMOUNT) {
            return discountAmount;
        } else if (rewardType == RewardType.PERCENTAGE) {
            return minimumThreshold.multiply(discountPercentage.divide(BigDecimal.valueOf(100)));
        }
        return BigDecimal.ZERO;
    }
} 