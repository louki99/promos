package ma.foodplus.ordering.system.order.model;

import jakarta.persistence.*;
import ma.foodplus.ordering.system.promos.dto.OrdertemDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A rich and immutable Domain Object representing a single line item in a shopping cart.
 *
 * This class is created from a OrdertemDto after the initial data has been validated
 * and enriched with information from the database (e.g., product name, family, SKU points).
 *
 * Its immutability ensures that the initial state of a cart item cannot be accidentally
 * modified during the promotion calculation process. Any changes are tracked in a
 * separate 'OrdertemContext' wrapper.
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_family_id")
    private Long productFamilyId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "sku")
    private String sku;

    @Column(name = "sku_points")
    private BigDecimal skuPoints;

    @Column(name = "notes")
    private String notes;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "applied_promotion_code")
    private String appliedPromotionCode;

    @ElementCollection
    @CollectionTable(name = "order_item_applied_promotions", 
        joinColumns = @JoinColumn(name = "order_item_id"))
    private List<String> appliedPromotions = new ArrayList<>();

    @Column(name = "consumed_quantity")
    private Integer consumedQuantity = 0;

    // Constructors
    public OrderItem() {
    }

    public OrderItem(Long productId, Long productFamilyId, String productName, BigDecimal price, Integer quantity, String sku, BigDecimal skuPoints) {
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
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.sku = sku;
        this.skuPoints = Objects.requireNonNullElse(skuPoints, BigDecimal.ZERO);
    }

    public OrderItem(OrdertemDto item){
        if (item == null) {
            throw new IllegalArgumentException("OrdertemDto cannot be null.");
        }
        if (item.getProductId() == null) {
            throw new IllegalArgumentException("Product ID cannot be null.");
        }
        if (item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        if (item.getUnitPrice() == null || item.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be non-negative.");
        }
    }

    // Business Logic Methods
    public BigDecimal getOriginalTotalPrice() {
        return this.price.multiply(BigDecimal.valueOf(this.quantity));
    }

    public BigDecimal getTotalSkuPoints() {
        return this.skuPoints.multiply(BigDecimal.valueOf(this.quantity));
    }

    public BigDecimal getFinalPrice() {
        return getOriginalTotalPrice().subtract(discountAmount);
    }

    public void applyDiscount(BigDecimal discount) {
        if (discount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount cannot be negative");
        }
        BigDecimal originalTotal = getOriginalTotalPrice();
        if (discount.compareTo(originalTotal) > 0) {
            throw new IllegalArgumentException("Discount cannot exceed original price");
        }
        this.discountAmount = this.discountAmount.add(discount);
    }

    public int getRemainingQuantityForRewards() {
        return quantity - consumedQuantity;
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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductFamilyId() {
        return productFamilyId;
    }

    public void setProductFamilyId(Long productFamilyId) {
        this.productFamilyId = productFamilyId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getSkuPoints() {
        return skuPoints;
    }

    public void setSkuPoints(BigDecimal skuPoints) {
        this.skuPoints = skuPoints;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public Integer getConsumedQuantity() {
        return consumedQuantity;
    }

    public void setConsumedQuantity(Integer consumedQuantity) {
        this.consumedQuantity = consumedQuantity;
    }

    public List<String> getAppliedPromotions() {
        return appliedPromotions;
    }

    public void setAppliedPromotions(List<String> appliedPromotions) {
        this.appliedPromotions = appliedPromotions;
    }

    public OrderItem getOriginalItem() {
        return this;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", productId=" + productId +
                ", productFamilyId=" + productFamilyId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", sku='" + sku + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}