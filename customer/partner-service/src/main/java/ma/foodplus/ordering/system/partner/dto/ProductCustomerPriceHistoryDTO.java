package ma.foodplus.ordering.system.partner.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class ProductCustomerPriceHistoryDTO {
    private Long id;
    private Long productCustomerId;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private BigDecimal oldDiscount;
    private BigDecimal newDiscount;
    private ZonedDateTime changeDate;
    private String changeReason;
    private String changedBy;
} 