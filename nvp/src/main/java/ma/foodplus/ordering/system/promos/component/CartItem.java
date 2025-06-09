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

    private Long productId;
    private Long productFamilyId;
    private String productName;
    private BigDecimal price;
    private int quantity;
    private String sku;

    // Enriched data from the database
    private final BigDecimal skuPoints;

    public CartItem(Long productId,Long productFamilyId,String productName,BigDecimal price,int quantity,String sku,BigDecimal skuPoints) {
        // Perform rigorous validation to ensure the integrity of the domain object.
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be non-negative.");
        }

        this.productId = productId;
        this.productFamilyId = productFamilyId;
        this.productName=productName;
        this.price = price;
        this.quantity = quantity;
        this.sku = sku;
        this.skuPoints = Objects.requireNonNullElse(skuPoints, BigDecimal.ZERO);
    }

    // --- Business Logic Methods ---

    /**
     * Calculates the original total price for this line item before any discounts.
     * This is a core piece of business logic belonging to the CartItem.
     * @return The total price (quantity * unitPrice).
     */
    public BigDecimal getOriginalTotalPrice() {
        return this.price.multiply(new BigDecimal(this.quantity));
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

    public Long getProductFamilyId() {
        return productFamilyId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSku() {
        return sku;
    }

    // --- Standard Object Methods ---

    @Override
    public String toString() {
        return "CartItem{" +
                "productId=" + productId +
                ", productFamilyId=" + productFamilyId +
                ", name='" +productName+ '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", sku='" + sku + '\'' +
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
