package ma.foodplus.ordering.system.product.service.domain.ports.output.repository;

import ma.foodplus.ordering.system.product.service.domain.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product createProduct(Product product);
    Optional<Product> findProductById(UUID productId);
    void deleteProduct(Product product);
    List<Product> getAllProducts(int page, int size, String sortBy, String sortDirection);
}
