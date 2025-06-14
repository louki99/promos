package ma.foodplus.ordering.system.product.dto;

import java.math.BigDecimal;

public record ProductAttributesDTO(
    BigDecimal weight,
    String weightUnit,
    String dimensions,
    String packageSize,
    Integer packageQuantity,
    String supplierCode,
    String supplierName,
    String countryOfOrigin,
    String certification
) {} 