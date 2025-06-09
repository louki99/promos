package ma.foodplus.ordering.system.inventory.service;

public interface InventoryService {
    /**
     * Get the current stock level for a product
     * @param productId The product ID
     * @return The current stock level
     */
    int getProductStockLevel(Long productId);
} 