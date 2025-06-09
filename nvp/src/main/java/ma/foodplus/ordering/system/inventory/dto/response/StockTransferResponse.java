package ma.foodplus.ordering.system.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Schema(description = "Stock Transfer Response")
public class StockTransferResponse {
    @Schema(description = "Transfer ID")
    private Long id;

    @Schema(description = "Source product stock ID")
    private Long sourceProductStockId;

    @Schema(description = "Destination product stock ID")
    private Long destinationProductStockId;

    @Schema(description = "Source depot ID")
    private Long sourceDepotId;

    @Schema(description = "Destination depot ID")
    private Long destinationDepotId;

    @Schema(description = "Quantity transferred")
    private BigDecimal quantity;

    @Schema(description = "Transfer status")
    private TransferStatus status;

    @Schema(description = "Transfer reason")
    private String reason;

    @Schema(description = "Reference number")
    private String referenceNumber;

    @Schema(description = "Expected transfer date")
    private ZonedDateTime expectedTransferDate;

    @Schema(description = "Actual transfer date")
    private ZonedDateTime actualTransferDate;

    @Schema(description = "User who initiated the transfer")
    private String initiatedBy;

    @Schema(description = "User who completed the transfer")
    private String completedBy;

    @Schema(description = "Special handling instructions")
    private String handlingInstructions;

    @Schema(description = "Transfer notes")
    private String notes;

    public enum TransferStatus {
        PENDING,
        IN_TRANSIT,
        COMPLETED,
        CANCELLED,
        FAILED
    }
} 