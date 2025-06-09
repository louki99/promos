package ma.foodplus.ordering.system.inventory.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BulkProductStockRequest {
    @NotNull(message = "Operation type is required")
    private OperationType operationType;

    @NotEmpty(message = "At least one product stock request is required")
    @Valid
    private List<ProductStockRequest> productStocks;

    public enum OperationType {
        CREATE,
        UPDATE,
        DELETE,
        QUALITY_CHECK,
        EXPIRY_CHECK
    }
} 