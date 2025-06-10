package ma.foodplus.ordering.system.order.model;

import java.math.BigDecimal;

public class OrderItemContext {
    private final OrderItem originalItem;
    private BigDecimal discountAmount;
    private String appliedPromotionCode;
    private int consumedQuantity;

    public OrderItemContext(OrderItem originalItem) {
        this.originalItem = originalItem;
        this.discountAmount = BigDecimal.ZERO;
        this.consumedQuantity = 0;
    }

    public OrderItem getOriginalItem() {
        return originalItem;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getAppliedPromotionCode() {
        return appliedPromotionCode;
    }

    public void setAppliedPromotionCode(String appliedPromotionCode) {
        this.appliedPromotionCode = appliedPromotionCode;
    }

    public BigDecimal getFinalPrice() {
        return originalItem.getUnitPrice()
                .multiply(new BigDecimal(originalItem.getQuantity()))
                .subtract(discountAmount);
    }

    public void applyDiscount(BigDecimal discount) {
        if (discount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount cannot be negative");
        }
        BigDecimal originalTotal = originalItem.getUnitPrice().multiply(new BigDecimal(originalItem.getQuantity()));
        if (discount.compareTo(originalTotal) > 0) {
            throw new IllegalArgumentException("Discount cannot exceed original price");
        }
        this.discountAmount = this.discountAmount.add(discount);
    }

    public BigDecimal getRemainingPrice() {
        return getFinalPrice();
    }

    public int getRemainingQuantityForRewards() {
        return originalItem.getQuantity() - consumedQuantity;
    }

    public void consumeQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (quantity > getRemainingQuantityForRewards()) {
            throw new IllegalArgumentException("Cannot consume more than remaining quantity");
        }
        this.consumedQuantity += quantity;
    }
} 