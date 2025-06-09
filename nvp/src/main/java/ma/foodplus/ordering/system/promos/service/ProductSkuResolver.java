package ma.foodplus.ordering.system.promos.service;

import java.math.BigDecimal;

/**
 * An interface that defines the contract for resolving promotional SKU points for a product.
 * This abstraction allows the core promotion engine to be decoupled from the specific
 * data source (e.g., database, cache, mock data).
 */
public interface ProductSkuResolver {

    /**
     * Gets the promotional SKU points for a specific product.
     *
     * @param productId The ID of the product for which to retrieve the points.
     * @return The SKU points as a BigDecimal. Implementations should handle cases where the
     *         product is not found gracefully, typically by returning BigDecimal.ZERO.
     */
    BigDecimal getSkuPointsForProduct(Long productId);
}
