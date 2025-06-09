package ma.foodplus.ordering.system.promos.service;

import lombok.Getter;
import ma.foodplus.ordering.system.order.model.OrderItem;

import java.math.BigDecimal;

/**
 * A context object that wraps an OrderItem and tracks its state during promotion application.
 * This class is used by the promotion engine to track discounts and quantities consumed by promotions.
 */
@Getter
public class OrderItemContext {
    private final OrderItem originalItem;
    private BigDecimal discountAmount;
    private int consumedQuantityForRewards;

    public OrderItemContext(OrderItem originalItem) {
        if (originalItem == null) {
            throw new IllegalArgumentException("Original item cannot be null");
        }
        this.originalItem = originalItem;
        this.discountAmount = BigDecimal.ZERO;
        this.consumedQuantityForRewards = 0;
    }

    public BigDecimal getRemainingPrice() {
        BigDecimal totalPrice = originalItem.getPrice().multiply(new BigDecimal(originalItem.getQuantity()));
        return totalPrice.subtract(discountAmount);
    }

    public int getRemainingQuantityForRewards() {
        return originalItem.getQuantity() - consumedQuantityForRewards;
    }

    public void applyDiscount(BigDecimal discount) {
        if (discount == null || discount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount must be non-negative");
        }
        this.discountAmount = this.discountAmount.add(discount);
    }

    public void consumeQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be non-negative");
        }
        if (quantity > getRemainingQuantityForRewards()) {
            throw new IllegalArgumentException("Cannot consume more quantity than available");
        }
        this.consumedQuantityForRewards += quantity;
    }
} 