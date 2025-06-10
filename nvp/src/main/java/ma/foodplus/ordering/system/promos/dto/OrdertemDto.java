package ma.foodplus.ordering.system.promos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing a single item within a shopping cart.
 *
 * This class is specifically used for deserializing incoming API requests,
 * for example, the JSON body of a `POST /api/promotions/apply` call.
 * It includes validation annotations to ensure that the data received from the
 * client is well-formed and meets the basic requirements before any business
 * logic is executed.
 */
public class OrdertemDto {

    /**
     * The unique identifier for the product.
     * This field is mandatory.
     */
    @NotNull(message = "Product ID cannot be null.")
    private Long productId;

    /**
     * The product family ID for grouping related products.
     */
    private Long productFamilyId;

    /**
     * The name of the product.
     */
    private String productName;

    /**
     * The number of units of the product being purchased.
     * This field is mandatory and must be a positive integer.
     */
    @NotNull(message = "Quantity cannot be null.")
    @Positive(message = "Quantity must be a positive number.")
    private Integer quantity;

    /**
     * The price for a single unit of the product.
     * This field is mandatory and must be a positive number.
     * Using BigDecimal is crucial for financial calculations to avoid floating-point errors.
     */
    @NotNull(message = "Unit price cannot be null.")
    @Positive(message = "Unit price must be a positive number.")
    private BigDecimal unitPrice;

    /**
     * The Stock Keeping Unit (SKU) for the product.
     */
    private String sku;

    /**
     * The loyalty points associated with this product.
     */
    private BigDecimal skuPoints;

    // --- Constructors ---

    /**
     * A no-argument constructor is required for libraries like Jackson
     * to be able to deserialize JSON into this object.
     */
    public OrdertemDto() {
    }

    /**
     * A convenience constructor for creating instances of this DTO in code,
     * for example, during testing.
     *
     * @param productId The product's unique ID.
     * @param productFamilyId The product family ID.
     * @param productName The product's name.
     * @param quantity The number of units.
     * @param unitPrice The price per unit.
     * @param sku The product's SKU.
     * @param skuPoints The product's loyalty points.
     */
    public OrdertemDto(Long productId, Long productFamilyId, String productName, Integer quantity, BigDecimal unitPrice, String sku, BigDecimal skuPoints) {
        this.productId = productId;
        this.productFamilyId = productFamilyId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.sku = sku;
        this.skuPoints = skuPoints;
    }

    // --- Standard Getters and Setters ---
    // Getters and setters are required for the serialization/deserialization
    // process to work correctly.

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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
}