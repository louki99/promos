package ma.foodplus.ordering.system.inventory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Stock Transfer Request")
public class StockTransferRequest {
    @NotNull(message = "Destination depot ID is required")
    @Schema(description = "ID of the destination depot")
    private Long destinationDepotId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    @Schema(description = "Quantity to transfer")
    private BigDecimal quantity;

    @Schema(description = "Reason for transfer")
    private String reason;

    @Schema(description = "Reference number for the transfer")
    private String referenceNumber;

    @Schema(description = "Expected transfer date")
    private String expectedTransferDate;

    @Schema(description = "Special handling instructions")
    private String handlingInstructions;
} 