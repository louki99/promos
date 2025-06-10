package ma.foodplus.ordering.system.customer.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class BulkProductCustomerOperationResponseDTO {
    private BulkProductCustomerOperationDTO.OperationType operationType;
    private int totalProcessed;
    private int successful;
    private int failed;
    private List<ProductCustomerDTO> successfulItems;
    private List<FailedOperation> failedOperations;

    @Data
    @Builder
    public static class FailedOperation {
        private Long productCustomerId;
        private String errorMessage;
        private String details;
    }
} 