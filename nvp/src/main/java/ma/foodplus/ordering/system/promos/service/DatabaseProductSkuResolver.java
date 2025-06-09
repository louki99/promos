package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;

/**
 * A production-ready implementation of the ProductSkuResolver.
 * It retrieves SKU points for products directly from the database
 * using a Spring Data JPA repository.
 */

/**
 * A production-ready implementation of the {@link ProductSkuResolver} interface.
 *
 * This service retrieves SKU points for products directly from the database
 * by using the {@link ProductRepository}. It is designed to be efficient
 * and robust for a real-world application.
 */
@Service("databaseProductSkuResolver") // Naming the bean for clarity and potential qualification
@RequiredArgsConstructor
public class DatabaseProductSkuResolver implements ProductSkuResolver {

    // Dependency on the Spring Data JPA repository
    private final ProductRepository productRepository;

    /**
     * {@inheritDoc}
     *
     * This method executes a highly optimized query to fetch only the SKU points
     * from the database for the given product ID.
     *
     * @param productId The ID of the product.
     * @return The SKU points from the database, or {@link BigDecimal#ZERO} if the product ID
     *         is null or not found in the database.
     */
    @Override
    public BigDecimal getSkuPointsForProduct(Long productId) {
        // Guard clause for invalid input
        if (productId == null) {
            return BigDecimal.ZERO;
        }

        // The repository method returns an Optional<BigDecimal>.
        // The orElse() method provides a default value if the Optional is empty
        // (i.e., the product was not found), which prevents NullPointerExceptions.
        return productRepository.findPromoSkuPointsById(productId)
                .orElse(BigDecimal.ZERO);
    }
}