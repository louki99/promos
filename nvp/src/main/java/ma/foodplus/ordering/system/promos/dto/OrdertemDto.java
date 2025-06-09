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
public class OrdertemDto{

    /**
     * The unique identifier for the product.
     * This field is mandatory.
     */
    @NotNull(message = "Product ID cannot be null.")
    private Long productId;

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
     * @param quantity The number of units.
     * @param unitPrice The price per unit.
     */
    public OrdertemDto(Long productId,Integer quantity,BigDecimal unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
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
}