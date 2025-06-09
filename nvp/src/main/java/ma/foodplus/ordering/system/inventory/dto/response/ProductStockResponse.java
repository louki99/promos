package ma.foodplus.ordering.system.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ma.foodplus.ordering.system.inventory.model.ProductStock.QualityStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "Product Stock Response")
public class ProductStockResponse {
    @Schema(description = "Unique identifier of the product stock")
    private Long id;

    @Schema(description = "Product identifier")
    private Long productId;

    @Schema(description = "Product name")
    private String productName;

    @Schema(description = "Depot identifier")
    private Long depotId;

    @Schema(description = "Depot name")
    private String depotName;

    @Schema(description = "Current quantity in stock")
    private BigDecimal quantity;

    @Schema(description = "Unit cost of the product")
    private BigDecimal unitCost;

    @Schema(description = "Total value of the stock (quantity * unit cost)")
    private BigDecimal totalValue;

    @Schema(description = "Expiry date of the product")
    private LocalDate expiryDate;

    @Schema(description = "Current quality status")
    private QualityStatus qualityStatus;

    @Schema(description = "Reserved quantity")
    private BigDecimal reservedQuantity;

    @Schema(description = "Available quantity (quantity - reserved quantity)")
    private BigDecimal availableQuantity;

    @Schema(description = "Notes about the stock")
    private String notes;

    @Schema(description = "Quality-related notes")
    private String qualityNotes;

    @Schema(description = "Whether the stock is low (below minimum threshold)")
    private boolean isLowStock;

    @Schema(description = "Whether the product is expired")
    private boolean isExpired;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    @Schema(description = "Minimum quantity threshold")
    private BigDecimal minimumQuantity;

    @Schema(description = "Maximum quantity threshold")
    private BigDecimal maximumQuantity;
} 