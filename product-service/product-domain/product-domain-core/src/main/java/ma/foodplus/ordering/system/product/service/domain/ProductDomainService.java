package ma.foodplus.ordering.system.product.service.domain;

import ma.foodplus.ordering.system.product.service.domain.entity.Product;
import ma.foodplus.ordering.system.product.service.domain.event.product.ProductCreatedEvent;
import ma.foodplus.ordering.system.product.service.domain.event.product.ProductDeletedEvent;
import ma.foodplus.ordering.system.product.service.domain.event.product.ProductUpdatedEvent;

public interface ProductDomainService{

    ProductCreatedEvent createProduct(Product product);

    ProductDeletedEvent deleteProduct(Product product);

    ProductUpdatedEvent updateProduct(Product product);
}
