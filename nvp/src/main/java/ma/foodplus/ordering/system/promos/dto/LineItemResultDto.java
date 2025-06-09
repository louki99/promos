package ma.foodplus.ordering.system.promos.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * A Data Transfer Object (DTO) that represents the final, calculated state
 * of a single line item in the cart.
 *
 * This object is part of the final API response and provides a detailed breakdown
 * of pricing for each item, including any discounts that were applied to it.
 */
public class LineItemResultDto {

    private final Long productId;
    private final String productName;
    private final int quantity;

    /**
     * The original total price for this line (quantity * unitPrice), before any discounts.
     */
    private final BigDecimal originalPrice;

    /**
     * The total amount of discount that was allocated to this specific line item
     * from all applied promotions.
     */
    private final BigDecimal totalDiscount;

    /**
     * The final price for this line after all discounts have been subtracted.
     * This is calculated as (originalPrice - totalDiscount).
     */
    private final BigDecimal finalPrice;

    private final List<String> appliedPromotions;

    /**
     * Constructs a new LineItemResultDto.
     * This is typically created from a OrdertemContext object at the end of the
     * promotion calculation process.
     *
     * @param productId The product's unique ID.
     * @param productName The product's name.
     * @param quantity The number of units.
     * @param originalPrice The original total price for the line.
     * @param totalDiscount The total discount applied to the line.
     */
    public LineItemResultDto(Long productId, String productName, int quantity, BigDecimal originalPrice, BigDecimal totalDiscount, List<String> appliedPromotions) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.originalPrice = originalPrice;
        this.totalDiscount = totalDiscount;
        this.finalPrice = originalPrice.subtract(totalDiscount);
        this.appliedPromotions = appliedPromotions;
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

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public List<String> getAppliedPromotions() {
        return appliedPromotions;
    }
}
