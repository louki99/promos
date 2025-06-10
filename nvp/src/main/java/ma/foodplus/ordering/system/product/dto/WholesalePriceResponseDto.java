package ma.foodplus.ordering.system.product.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WholesalePriceResponseDto {
    private Long productId;
    private String productName;
    private String sku;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal wholesalePrice;
    private BigDecimal bulkDiscount;
    private BigDecimal taxAmount;
    private BigDecimal totalPrice;
    private String appliedTier;
    private Boolean requiresContract;
    private String contractNumber;
    private String pricingNotes;
} 