package ma.foodplus.ordering.system.product.service.domain.event.product;

import ma.foodplus.ordering.system.product.service.domain.entity.Product;

import java.time.ZonedDateTime;

public class ProductDeletedEvent extends ProductEvent{

    public ProductDeletedEvent(Product product,
                               ZonedDateTime createdAt) {
        super(product, createdAt);
    }
}