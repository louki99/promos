package ma.foodplus.ordering.system.partner.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BulkProductPartnerOperationDTO{
    private OperationType operationType;
    private List<Long> productCustomerIds;
    private BigDecimal newPrice;
    private BigDecimal newDiscount;
    private String category;
    private BigDecimal coefficient;
    private Boolean active;
    private String reason;

    public enum OperationType {
        UPDATE_PRICE,
        UPDATE_DISCOUNT,
        UPDATE_CATEGORY,
        UPDATE_COEFFICIENT,
        ACTIVATE,
        DEACTIVATE,
        DELETE
    }
} 