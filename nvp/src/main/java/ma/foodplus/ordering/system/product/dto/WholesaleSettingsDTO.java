package ma.foodplus.ordering.system.product.dto;

import java.math.BigDecimal;

public record WholesaleSettingsDTO(
    BigDecimal wholesalePrice,
    Integer wholesaleMinimumQuantity,
    BigDecimal wholesaleDiscountPercentage,
    BigDecimal wholesaleTier1Price,
    Integer wholesaleTier1Quantity,
    BigDecimal wholesaleTier2Price,
    Integer wholesaleTier2Quantity,
    BigDecimal wholesaleTier3Price,
    Integer wholesaleTier3Quantity
) {} 