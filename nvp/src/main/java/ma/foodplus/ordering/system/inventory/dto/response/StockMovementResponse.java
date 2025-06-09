package ma.foodplus.ordering.system.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ma.foodplus.ordering.system.inventory.model.ProductStock.QualityStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Stock Movement Response")
public class StockMovementResponse {
    @Schema(description = "Unique identifier of the movement")
    private Long id;

    @Schema(description = "Product stock identifier")
    private Long productStockId;

    @Schema(description = "Movement type")
    private MovementType type;

    @Schema(description = "Quantity moved")
    private BigDecimal quantity;

    @Schema(description = "Previous quantity")
    private BigDecimal previousQuantity;

    @Schema(description = "New quantity")
    private BigDecimal newQuantity;

    @Schema(description = "Previous quality status")
    private QualityStatus previousQualityStatus;

    @Schema(description = "New quality status")
    private QualityStatus newQualityStatus;

    @Schema(description = "Reference number for the movement")
    private String referenceNumber;

    @Schema(description = "Notes about the movement")
    private String notes;

    @Schema(description = "User who performed the movement")
    private String performedBy;

    @Schema(description = "Movement timestamp")
    private LocalDateTime timestamp;

    public enum MovementType {
        ADJUSTMENT,
        TRANSFER,
        QUALITY_STATUS_CHANGE,
        RESERVATION,
        RELEASE
    }
} 