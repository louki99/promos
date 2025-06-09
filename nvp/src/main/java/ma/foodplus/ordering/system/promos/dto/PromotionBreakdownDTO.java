package ma.foodplus.ordering.system.promos.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO representing a detailed breakdown of how a promotion affects a cart.
 */
@Data
@Builder
public class PromotionBreakdownDTO {
    private String promotionCode;
    private String promotionName;
    private BigDecimal originalTotal;
    private BigDecimal discountTotal;
    private BigDecimal finalTotal;
    private Map<Long, BigDecimal> itemDiscounts; // Product ID -> Discount amount
} 