package ma.foodplus.ordering.system.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ma.foodplus.ordering.system.inventory.dto.request.BulkProductStockRequest.OperationType;

import java.util.List;

@Data
@Schema(description = "Bulk Product Stock Operation Response")
public class BulkProductStockResponse {
    @Schema(description = "Type of operation performed")
    private OperationType operationType;

    @Schema(description = "Total number of items processed")
    private int totalProcessed;

    @Schema(description = "Number of successful operations")
    private int successful;

    @Schema(description = "Number of failed operations")
    private int failed;

    @Schema(description = "List of successfully processed items")
    private List<ProductStockResponse> successfulItems;

    @Schema(description = "List of failed operations with error messages")
    private List<FailedOperation> failedOperations;

    @Data
    @Schema(description = "Failed Operation Details")
    public static class FailedOperation {
        @Schema(description = "Product stock request that failed")
        private Long productId;

        @Schema(description = "Depot ID where the operation failed")
        private Long depotId;

        @Schema(description = "Error message describing the failure")
        private String errorMessage;
    }
} 