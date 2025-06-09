package ma.foodplus.ordering.system.promos.dto;

/**
 * A Data Transfer Object (DTO) that represents a free item granted to the
 * customer as a result of a promotion (e.g., "Buy X, Get Y Free").
 *
 * A list of these objects will be included in the final API response to
 * clearly indicate any promotional gifts.
 */
public class FreeItemDto {

    private final Long productId;
    private final String productName;
    private final int quantity;

    /**
     * A human-readable text explaining why this item was granted for free.
     * Example: "Promotional gift from 'SUMMER2024' offer".
     */
    private final String reason;

    /**
     * Constructs a new FreeItemDto.
     *
     * @param productId The unique ID of the free product.
     * @param productName The name of the free product.
     * @param quantity The number of free units granted.
     * @param reason A description of the promotion that granted this item.
     */
    public FreeItemDto(Long productId, String productName, int quantity, String reason) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.reason = reason;
    }


    // --- Getters Only (Immutable DTO) ---

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getReason() {
        return reason;
    }
}
