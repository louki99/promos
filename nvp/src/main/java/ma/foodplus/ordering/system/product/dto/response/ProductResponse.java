package ma.foodplus.ordering.system.product.dto.response;

import ma.foodplus.ordering.system.product.enums.SuiviStock;

import java.math.BigDecimal;

public record ProductResponse(
    Long id,
    String reference,
    String title,
    String description,
    String barcode,
    String familyCode,
    String category1,
    String category2,
    String category3,
    String category4,
    BigDecimal salePrice,
    String saleUnit,
    BigDecimal priceIncludingTax,
    String photo,
    Boolean deliverable,
    Boolean inactive,
    SuiviStock stockTracking
) {}