package ma.foodplus.ordering.system.product.dto;

import java.math.BigDecimal;

public record BulkSettingsDTO(
    Integer minimumOrderQuantity,
    Integer maximumOrderQuantity,
    Integer bulkDiscountThreshold,
    BigDecimal bulkDiscountPercentage,
    Integer bulkPackageSize,
    String bulkPackageUnit
) {} 