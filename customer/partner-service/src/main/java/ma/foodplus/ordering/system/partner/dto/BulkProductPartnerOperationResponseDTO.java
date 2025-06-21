package ma.foodplus.ordering.system.partner.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class BulkProductPartnerOperationResponseDTO{
    private BulkProductPartnerOperationDTO.OperationType operationType;
    private int totalProcessed;
    private int successful;
    private int failed;
    private List<ProductPartnerDTO> successfulItems;
    private List<FailedOperation> failedOperations;

    @Data
    @Builder
    public static class FailedOperation {
        private Long productCustomerId;
        private String errorMessage;
        private String details;
    }
} 