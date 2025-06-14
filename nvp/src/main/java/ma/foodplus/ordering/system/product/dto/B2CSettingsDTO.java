package ma.foodplus.ordering.system.product.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record B2CSettingsDTO(
    BigDecimal b2cRetailPrice,
    BigDecimal b2cPromoPrice,
    ZonedDateTime b2cPromoStartDate,
    ZonedDateTime b2cPromoEndDate,
    BigDecimal b2cLoyaltyPointsMultiplier,
    Boolean b2cDisplayInCatalog,
    Boolean b2cFeatured
) {} 