package ma.foodplus.ordering.system.inventory.service;

import ma.foodplus.ordering.system.product.model.Product;
import java.util.List;

public interface BulkInventoryService {
    /**
     * Check if a product is available in the requested quantity
     * @param product The product to check
     * @param quantity The quantity to check
     * @return true if the product is available in the requested quantity
     */
    boolean checkAvailability(Product product, Integer quantity);

    /**
     * Get recommended quantities for bulk ordering
     * @param product The product to get recommendations for
     * @return List of recommended quantities
     */
    List<Integer> getRecommendedQuantities(Product product);

    /**
     * Reserve stock for a bulk order
     * @param product The product to reserve
     * @param quantity The quantity to reserve
     */
    void reserveStock(Product product, Integer quantity);

    /**
     * Release reserved stock
     * @param product The product to release
     * @param quantity The quantity to release
     */
    void releaseStock(Product product, Integer quantity);
}