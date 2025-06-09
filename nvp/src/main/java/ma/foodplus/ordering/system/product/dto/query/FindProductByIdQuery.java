package ma.foodplus.ordering.system.product.dto.query;

import ma.foodplus.ordering.system.domain.valueobject.ProductId;

public record FindProductByIdQuery(
    ProductId productId
) {} 