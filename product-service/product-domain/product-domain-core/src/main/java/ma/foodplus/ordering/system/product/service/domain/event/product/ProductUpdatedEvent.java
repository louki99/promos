package ma.foodplus.ordering.system.product.service.domain.event.product;

import ma.foodplus.ordering.system.product.service.domain.entity.Product;

import java.time.ZonedDateTime;

public class ProductUpdatedEvent extends ProductEvent {

    private final Product product;

    public ProductUpdatedEvent(Product product, ZonedDateTime createdAt) {
        super(product, createdAt);
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}