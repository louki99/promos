package ma.foodplus.ordering.system.inventory.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import ma.foodplus.ordering.system.inventory.model.ProductStock.QualityStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductStockRequest {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Depot ID is required")
    private Long depotId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    @NotNull(message = "Unit cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit cost must be greater than 0")
    private BigDecimal unitCost;

    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;

    @NotNull(message = "Quality status is required")
    private QualityStatus qualityStatus;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    @Min(value = 0, message = "Reserved quantity must be greater than or equal to 0")
    private BigDecimal reservedQuantity;

    @Size(max = 1000, message = "Quality notes cannot exceed 1000 characters")
    private String qualityNotes;

    @Positive(message = "Minimum quantity must be positive")
    private BigDecimal minimumQuantity;

    @Positive(message = "Maximum quantity must be positive")
    private BigDecimal maximumQuantity;
} 