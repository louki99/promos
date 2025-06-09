package ma.foodplus.ordering.system.promos.component;

import ma.foodplus.ordering.system.order.model.OrderItemContext;

import java.math.BigDecimal;
import java.util.List;

/**
 * A Data Transfer Object (DTO) that represents a promotion that has been
 * successfully applied to a shopping cart.
 *
 * This class provides a clear and structured summary of the outcome of a single
 * promotion application, including the discount amount and a human-readable
 * description. It is intended to be part of the final API response.
 */
public class AppliedPromotion {

    /**
     * The unique code of the promotion that was applied (e.g., "SUMMER2024").
     * This helps in tracking and referencing the promotion.
     */
    private final String promotionCode;

    /**
     * A human-readable description of the reward that was granted.
     * This can be displayed to the end-user.
     * Examples: "10% discount on summer collection", "Buy 2, Get 1 Free Applied", "Cumulative Slice Discount".
     */
    private final String description;

    /**
     * The total monetary value of the discount applied by this promotion.
     * For promotions that grant free items, this might be zero, as the value
     * is represented by the free item itself.
     */
    private final BigDecimal discountAmount;

    private final List<OrderItemContext> affectedItems;

    /**
     * Constructs a new AppliedPromotion record.
     *
     * @param promotionCode The unique code of the promotion.
     * @param description A description of the applied benefit.
     * @param discountAmount The total discount value provided by this promotion.
     * @param affectedItems The list of affected items by this promotion.
     */
    public AppliedPromotion(String promotionCode, String description, BigDecimal discountAmount, List<OrderItemContext> affectedItems) {
        this.promotionCode = promotionCode;
        this.description = description;
        this.discountAmount = discountAmount;
        this.affectedItems = affectedItems;
    }

    // --- Getters ---
    // No setters are provided as this object is intended to be immutable.

    public String getPromotionCode() {
        return promotionCode;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public List<OrderItemContext> getAffectedItems() {
        return affectedItems;
    }

    @Override
    public String toString() {
        return "AppliedPromotion{" +
                "promotionCode='" + promotionCode + '\'' +
                ", description='" + description + '\'' +
                ", discountAmount=" + discountAmount +
                ", affectedItems=" + affectedItems +
                '}';
    }
}
