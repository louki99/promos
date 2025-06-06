package ma.foodplus.ordering.system.product.service.domain.event.product;

import ma.foodplus.ordering.system.domain.event.DomainEvent;
import ma.foodplus.ordering.system.product.service.domain.entity.Product;

import java.time.ZonedDateTime;

public abstract class ProductEvent implements DomainEvent<Product>{
    private final Product product;
    private final ZonedDateTime createdAt;

    public ProductEvent(Product product,ZonedDateTime createdAt) {
        this.product=product;
        this.createdAt = createdAt;
    }

    public Product getProduct() {
        return product;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
