package ma.foodplus.ordering.system.product.dto.update;


import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import ma.foodplus.ordering.system.domain.valueobject.ProductId;
import ma.foodplus.ordering.system.product.enums.SuiviStock;

import java.math.BigDecimal;

public record UpdateProductCommand(
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

