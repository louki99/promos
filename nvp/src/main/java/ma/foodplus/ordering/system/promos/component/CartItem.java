package ma.foodplus.ordering.system.promos.component;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A rich and immutable Domain Object representing a single line item in a shopping cart.
 *
 * This class is created from a CartItemDto after the initial data has been validated
 * and enriched with information from the database (e.g., product name, family, SKU points).
 *
 * Its immutability ensures that the initial state of a cart item cannot be accidentally
 * modified during the promotion calculation process. Any changes are tracked in a
 * separate 'CartItemContext' wrapper.
 */
public final class CartItem {

    private final Long productId;
    private final String productName;
    private final int quantity;
    private final BigDecimal unitPrice;

    // Enriched data from the database
    private final Long productFamilyId;
    private final BigDecimal skuPoints;

    public CartItem(Long productId, String productName, int quantity, BigDecimal unitPrice, Long productFamilyId, BigDecimal skuPoints) {
        // Perform rigorous validation to ensure the integrity of the domain object.
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price must be non-negative.");
        }

        this.productId = productId;
        this.productName = Objects.requireNonNullElse(productName, "Unknown Product");
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.productFamilyId = productFamilyId;
        this.skuPoints = Objects.requireNonNullElse(skuPoints, BigDecimal.ZERO);
    }

    // --- Business Logic Methods ---

    /**
     * Calculates the original total price for this line item before any discounts.
     * This is a core piece of business logic belonging to the CartItem.
     * @return The total price (quantity * unitPrice).
     */
    public BigDecimal getOriginalTotalPrice() {
        return this.unitPrice.multiply(new BigDecimal(this.quantity));
    }

    /**
     * Calculates the total SKU points for this line item.
     * @return The total points (quantity * skuPoints).
     */
    public BigDecimal getTotalSkuPoints() {
        return this.skuPoints.multiply(new BigDecimal(this.quantity));
    }


    // --- Getters ---
    // No setters are provided to enforce immutability.

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Long getProductFamilyId() {
        return productFamilyId;
    }

    public BigDecimal getSkuPoints() {
        return skuPoints;
    }

    // --- Standard Object Methods ---

    @Override
    public String toString() {
        return "CartItem{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return productId.equals(cartItem.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
