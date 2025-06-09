package ma.foodplus.ordering.system.promos.service;

import ma.foodplus.ordering.system.promos.component.CartItem;

import java.math.BigDecimal;

/**
 * A stateful wrapper for a CartItem, used exclusively within the promotion engine.
 * It tracks the "live" state of an item as promotions are applied, including remaining
 * quantities and prices that are eligible for further discounts.
 * This approach avoids mutating the original CartItem data.
 */
public class CartItemContext {

    private final CartItem originalItem;

    private BigDecimal remainingPriceForDiscount; // The price still eligible for discounts.
    private int remainingQuantityForRewards;   // The quantity still eligible for rewards (like "buy X get Y").

    private BigDecimal totalDiscountApplied = BigDecimal.ZERO;

    /**
     * Constructs a context wrapper for a given CartItem.
     * @param originalItem The initial, immutable CartItem.
     */
    public CartItemContext(CartItem originalItem) {
        if (originalItem == null) {
            throw new IllegalArgumentException("Original CartItem cannot be null.");
        }
        this.originalItem = originalItem;
        this.remainingPriceForDiscount = originalItem.getOriginalTotalPrice();
        this.remainingQuantityForRewards = originalItem.getQuantity();
    }

    // --- Core Methods for Promotion Engine ---

    /**
     * Applies a discount amount to this item.
     * The discount is subtracted from the remaining price, and the total discount is tracked.
     *
     * @param discountAmount The amount of discount to apply. Must not be negative.
     */
    public void applyDiscount(BigDecimal discountAmount) {
        if (discountAmount == null || discountAmount.compareTo(BigDecimal.ZERO) < 0) {
            return; // Do not apply negative or null discounts.
        }

        // Ensure discount does not exceed the remaining price.
        BigDecimal applicableDiscount = discountAmount.min(this.remainingPriceForDiscount);

        this.totalDiscountApplied = this.totalDiscountApplied.add(applicableDiscount);
        this.remainingPriceForDiscount = this.remainingPriceForDiscount.subtract(applicableDiscount);
    }

    /**
     * Consumes a certain quantity of this item, typically because it was used to
     * satisfy a promotion condition (e.g., for a "buy X, get Y" cumulative promo).
     * This reduces the quantity available for future promotions.
     *
     * @param quantityToConsume The number of units to consume.
     */
    public void consumeQuantity(int quantityToConsume) {
        if (quantityToConsume <= 0) {
            return;
        }

        int consumedQuantity = Math.min(quantityToConsume, this.remainingQuantityForRewards);
        this.remainingQuantityForRewards -= consumedQuantity;
    }


    // --- Getters for Accessing State ---

    /**
     * @return The original, immutable CartItem.
     */
    public CartItem getOriginalItem() {
        return originalItem;
    }

    /**
     * @return The current price of the item that is still eligible for discounts.
     */
    public BigDecimal getRemainingPriceForDiscount() {
        return remainingPriceForDiscount;
    }

    /**
     * @return The quantity of this item that is still available to satisfy promotion conditions.
     */
    public int getRemainingQuantityForRewards() {
        return remainingQuantityForRewards;
    }

    /**

     * @return The total cumulative discount that has been applied to this item so far.
     */
    public BigDecimal getTotalDiscountApplied() {
        return totalDiscountApplied;
    }

    /**
     * @return The final price for this line item after all discounts have been applied.
     */
    public BigDecimal getFinalPrice() {
        return originalItem.getOriginalTotalPrice().subtract(totalDiscountApplied);
    }

    @Override
    public String toString() {
        return "CartItemContext{" +
                "originalItem=" + originalItem.getProductName() +
                ", remainingPrice=" + remainingPriceForDiscount +
                ", remainingQuantity=" + remainingQuantityForRewards +
                ", totalDiscount=" + totalDiscountApplied +
                '}';
    }
}